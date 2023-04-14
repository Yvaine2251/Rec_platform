package com.platform.course.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.course.entity.StudyRecord;
import com.platform.course.mapper.StudyRecordMapper;
import com.platform.entity.ResponseResult;
import com.platform.course.dto.ChapterAddDTO;
import com.platform.course.dto.ChapterUpdateDTO;
import com.platform.course.entity.Chapter;
import com.platform.course.entity.ClassTime;
import com.platform.course.entity.ClassTimeResource;
import com.platform.course.mapper.ChapterMapper;
import com.platform.course.mapper.ClassTimeMapper;
import com.platform.course.mapper.ClassTimeResourceMapper;
import com.platform.course.service.ChapterService;
import com.platform.course.service.ClassTimeService;
import com.platform.course.vo.ChapterShowVo;
import com.platform.course.vo.ClassTimeVo;
import com.platform.exception.PlatformException;
import com.platform.resources.mapper.ResourcesMapper;
import com.platform.resources.entity.Resources;
import com.platform.resources.vo.ClassTimeResourceVo;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import com.platform.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.platform.course.enums.CourseCodeEnum.*;
import static com.platform.course.constants.CourseConstant.*;

@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    ChapterMapper chapterMapper;

    @Autowired
    ClassTimeMapper classTimeMapper;

    @Autowired
    ClassTimeService classTimeService;

    @Autowired
    ClassTimeResourceMapper classTimeResourceMapper;

    @Autowired
    ResourcesMapper resourcesMapper;

    @Autowired
    private StudyRecordMapper studyRecordMapper;

    @Autowired
    public RedisCache redisCache;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult addChapter(ChapterAddDTO chapterAddDto) {

        // 创建一个章节对象
        Chapter chapter = BeanCopyUtils.copyBean(chapterAddDto, Chapter.class);

        // 判断顺序，并添加序列位置
        LambdaQueryWrapper<Chapter> lba = new LambdaQueryWrapper();
        lba.eq(Chapter::getCourseId, chapterAddDto.getCourseId());
        lba.eq(Chapter::getPid, chapter.getPid());
        int count = chapterMapper.selectCount(lba);

        // 设置章节属性
        chapter.setHasCoursetime(NULL_COURSE_TIME);
        chapter.setChapterOrder(count + 1);
        int flag = chapterMapper.insert(chapter);
        if (flag == 0) {
            throw new PlatformException(CHAPTER_INSERT_ERROR.getCode(), CHAPTER_INSERT_ERROR.getMessage());
        }
        String chapterId = chapter.getId();
        //删除redis中存的章节信息
        redisCache.deleteObject(CACHE_CHAPTER_KEY + chapterAddDto.getCourseId());
        return ResponseResult.okResult(chapterId);
    }

    @Override
    public ResponseResult updateChapterName(ChapterUpdateDTO chapterUpdateDto) {
        Chapter chapter = chapterMapper.selectById(chapterUpdateDto.getChapterId());
        chapter.setName(chapterUpdateDto.getNewName());
        int count = chapterMapper.updateById(chapter);
        if (count == 0) {
            throw new PlatformException(CHAPTER_UPDATE_ERROR.getCode(), CHAPTER_UPDATE_ERROR.getMessage());
        }
        redisCache.deleteObject(CACHE_CHAPTER_KEY + chapter.getCourseId());
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteChapter(String chapterId) {

        LambdaQueryWrapper<Chapter> lba01 = new LambdaQueryWrapper<>();
        //只查询id，是否有课时，父id，顺序三个属性
        lba01.select(Chapter::getId, Chapter::getPid, Chapter::getHasCoursetime,
                Chapter::getChapterOrder).eq(Chapter::getId, chapterId);
        Chapter chapter = chapterMapper.selectOne(lba01);

        if (chapter == null) {
            throw new PlatformException(CHAPTER_NOT_EXIST.getCode(), CHAPTER_NOT_EXIST.getMessage());
        }

        // 调整顺序，顺序大于删除章节，全部减一
        LambdaQueryWrapper<Chapter> lba02 = new LambdaQueryWrapper<>();
        lba02.eq(Chapter::getPid, chapter.getPid());
        lba02.gt(Chapter::getChapterOrder, chapter.getChapterOrder());
        List<Chapter> chapters = chapterMapper.selectList(lba02);
        for (Chapter c : chapters) {
            c.setChapterOrder(c.getChapterOrder() - 1);
            chapterMapper.updateById(c);
        }

        /// 3.删除子章节及其课时
        Deque<Chapter> stack = new ArrayDeque<>();
        Deque<Chapter> queue = new ArrayDeque<>();
        queue.offer(chapter);
        while (!queue.isEmpty()) {
            Chapter poll = queue.poll();
            stack.offer(poll);
            List<Chapter> childChapters = getChildChapters(poll);
            for (Chapter childChapter : childChapters) {
                queue.offer(childChapter);
            }
        }
        while (!stack.isEmpty()) {
            Chapter poll = stack.pollLast();
            deleteCourseTimes(poll);
            LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Chapter::getId, poll.getId());
            chapterMapper.delete(wrapper);
        }
        redisCache.deleteObject(CACHE_CHAPTER_KEY + chapter.getCourseId());
        return ResponseResult.okResult();
    }


    /**
     * 根据课程号获取信息，返回对象
     *
     * @param courseId
     * @return
     */
    @Override
    public ResponseResult getChapter(String courseId) {
        // redis 中缓存章节的key
//        String key = CACHE_CHAPTER_KEY + courseId;
//        Object OneChapter = redisCache.getCacheObject(key);
//        if (OneChapter != null) {
//            return ResponseResult.okResult(OneChapter);
//        }

        // 加入互斥锁，解决缓存击穿问题，代码步骤用小写字母表示
        // a 获取互斥锁
//        String lockKey = "lock:shop:" + courseId;
//        boolean isLock = tryLock(lockKey);
        // b 判断是否获取成功

        //1、先一次性查出该课程下所有章节
        LambdaQueryWrapper<Chapter> chapterLqw = new LambdaQueryWrapper<>();
        chapterLqw.eq(Chapter::getCourseId, courseId);
        List<Chapter> allChapter = chapterMapper.selectList(chapterLqw);
        List<ChapterShowVo> chapterShowVos = BeanCopyUtils.copyBeanList(allChapter, ChapterShowVo.class);
        //2、查询课时封装到章节vo里
        for (ChapterShowVo chapterVo : chapterShowVos) {
            String chapterId = chapterVo.getId();
            LambdaQueryWrapper<ClassTime> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ClassTime::getChapterId, chapterId);
            List<ClassTime> classTimes = classTimeMapper.selectList(wrapper);
            List<ClassTimeVo> classTimeVos = BeanCopyUtils.copyBeanList(classTimes, ClassTimeVo.class);
            //3、封装课时资源关系到课时vo中
            for (ClassTimeVo classTimeVo : classTimeVos) {
                String classTimeId = classTimeVo.getClassTimeId();
                LambdaQueryWrapper<ClassTimeResource> lqw = new LambdaQueryWrapper<>();
                lqw.eq(ClassTimeResource::getClassTimeId, classTimeId);
                List<ClassTimeResource> classTimeResources = classTimeResourceMapper.selectList(lqw);
                //4、获取资源信息进行封装
                ArrayList<ClassTimeResourceVo> classTimeResourceList = new ArrayList<>();
                for (ClassTimeResource resource : classTimeResources) {
                    String resourceId = resource.getResourceId();
                    Resources resources = resourcesMapper.selectById(resourceId);
                    ClassTimeResourceVo classTimeResourceVo = BeanCopyUtils.copyBean(resources, ClassTimeResourceVo.class);

                    // TODO 添加学习记录
                    // 1.获取用户id
                    String userId = SecurityUtils.getUserId();
                    // 2.判断是否已经添加过学习记录
                    LambdaQueryWrapper<StudyRecord> lqw01 = new LambdaQueryWrapper<>();
                    lqw01.eq(StudyRecord::getUserId, userId);
                    lqw01.eq(StudyRecord::getResourceId, resourceId);
                    StudyRecord studyRecord = studyRecordMapper.selectOne(lqw01);
                    classTimeResourceVo.setIsStudy(studyRecord != null ? 1 : 0);

                    classTimeResourceList.add(classTimeResourceVo);

                }
                classTimeVo.setResource(classTimeResourceList);
            }
            chapterVo.setCourTimes(classTimeVos);
        }
        List<ChapterShowVo> levelOneChapter = setChildren(chapterShowVos);
        // 设置缓存失效时间，防止多线程下数据不一致问题
//        redisCache.setCacheObject(key, levelOneChapter, CACHE_CHAPTER_TTL, TimeUnit.MINUTES);
        return ResponseResult.okResult(levelOneChapter);
    }

    private List<ChapterShowVo> setChildren(List<ChapterShowVo> chapterShowVos) {
        //找一级章节
        List<ChapterShowVo> levelOneList = chapterShowVos.stream()
                .filter(c -> c.getPid().equals(ROOT_CHAPTER_PRE))
                .collect(Collectors.toList());
        for (ChapterShowVo chapterShowVo : levelOneList) {
            recursion(chapterShowVo, chapterShowVos);
        }
        return levelOneList;
    }

    private void recursion(ChapterShowVo chapterShowVo, List<ChapterShowVo> chapterShowVos) {
        List<ChapterShowVo> childrenList = new ArrayList<>();
        for (ChapterShowVo showVo : chapterShowVos) {
            if (chapterShowVo.getId().equals(showVo.getPid())) {
                childrenList.add(showVo);
            }
        }
        chapterShowVo.setChildChapters(childrenList);
        if (childrenList.isEmpty()) {
            return;
        }
        for (ChapterShowVo showVo : childrenList) {
            recursion(showVo, chapterShowVos);
        }
    }

    /**
     * 删除章节时，将章节下的课时删除
     *
     * @param chapter
     */
    private void deleteCourseTimes(Chapter chapter) {
        if (chapter.getHasCoursetime() != 0) {
            LambdaQueryWrapper<ClassTime> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ClassTime::getChapterId, chapter.getId())
                    .select(ClassTime::getClassTimeId);
            List<ClassTime> classTimes = classTimeMapper.selectList(wrapper);
            for (int i = 0; i < classTimes.size(); i++) {
                classTimeService.deleteClassTime(classTimes.get(i).getClassTimeId());
            }
        }
    }

    /**
     * 根据条件获取章节部分信息
     *
     * @param chapter
     * @return
     */
    private List<Chapter> getChildChapters(Chapter chapter) {
        LambdaQueryWrapper<Chapter> condition = new LambdaQueryWrapper<>();
        condition.eq(Chapter::getPid, chapter.getId());
        condition.select(Chapter::getId, Chapter::getHasCoursetime);
        return chapterMapper.selectList(condition);
    }

    /**
     * 解决缓存击穿问题，获取锁
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        //使用工具类，拆箱过程中，防止空指针异常
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放锁
     * @param key
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
