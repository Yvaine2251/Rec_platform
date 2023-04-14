package com.platform.points.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.entity.ResponseResult;
import com.platform.exception.PlatformException;
import com.platform.points.dto.*;
import com.platform.points.entity.PointRelation;
import com.platform.points.mapper.PointMapper;
import com.platform.points.entity.Point;
import com.platform.points.mapper.PointRelationMapper;
import com.platform.points.service.GraphService;
import com.platform.points.service.PointService;
import com.platform.points.util.ExcelUtil;
import com.platform.points.util.UploadDataListener;
import com.platform.points.vo.*;
import com.platform.utils.BeanCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.platform.points.enums.PointCodeEnum.*;
import static com.platform.points.constants.PointConstant.*;

@Slf4j
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {

    @Autowired
    PointMapper pointMapper;

    @Autowired
    PointRelationMapper pointRelationMapper;

    @Resource
    PointService pointService;

    @Resource
    GraphService graphService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addPoints(PointCreateDto pointDto) {
        //判断知识点名称是否重复
        LambdaQueryWrapper<Point> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Point::getPointName, pointDto.getPointName())
                .eq(Point::getCourseId, pointDto.getCourseId());
        Point point = pointMapper.selectOne(wrapper);
        if (point != null) {
            throw new PlatformException(POINT_REPEAT.getCode(), POINT_REPEAT.getMessage());
        }
        //添加
        Point newPoint = BeanCopyUtils.copyBean(pointDto, Point.class);
        pointMapper.insert(newPoint);
        return ResponseResult.okResult(newPoint.getPointId());
    }

    @Override
    public ResponseResult updatePointName(PointUpdateDto updateDto) {
        Point point = BeanCopyUtils.copyBean(updateDto, Point.class);
        pointMapper.updateById(point);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updatePointLevel(PointUpdateLevelDto updateDto) {
        LambdaQueryWrapper<Point> pointQuery = new LambdaQueryWrapper<>();
        pointQuery.eq(Point::getPointId, updateDto.getPointId());
        Point point = pointMapper.selectOne(pointQuery);
        if (point == null) {
            throw new PlatformException(POINT_NOT_EXIST.getCode(), POINT_NOT_EXIST.getMessage());
        }
        point.setPointPid(updateDto.getPid());
        pointMapper.updateById(point);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deletePoints(String[] ids) {
        //创建队列，添加删除
        Queue<String> deleteList = new LinkedList<>();

        //先添加需要删除的ids
        for (String id : ids) {
            deleteList.offer(id);
        }

        String pointId = deleteList.poll();
        while (pointId != null) {
            LambdaQueryWrapper<Point> lba = new LambdaQueryWrapper<>();
            lba.eq(Point::getPointPid, pointId);
            List<Point> points = pointMapper.selectList(lba);
            for (Point point : points) {
                deleteList.offer(point.getPointId());
            }

            //删除知识点关系表中记录
            LambdaQueryWrapper<PointRelation> pointWrapper = new LambdaQueryWrapper<>();
            pointWrapper.eq(PointRelation::getPointAId, pointId)
                    .or().eq(PointRelation::getPointBId, pointId);
            pointRelationMapper.delete(pointWrapper);

            //删除本身
            pointMapper.deleteById(pointId);

            //队列里下一个
            pointId = deleteList.poll();
        }

        return ResponseResult.okResult();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult getPoints(String courseId) {
        //1、查知识点表该课程下的所有知识点
        LambdaQueryWrapper<Point> pointLqw = new LambdaQueryWrapper<>();
        pointLqw.eq(Point::getCourseId, courseId)
                .select(Point::getPointId, Point::getPointName, Point::getPointPid);
        List<Point> points = pointMapper.selectList(pointLqw);

        //2、拷贝到vo中，第一种用工具类，第二种需要在vo中声明构造器
        List<PointShowVo> pointShowVoList = BeanCopyUtils.copyBeanList(points, PointShowVo.class);
        //List<PointShowVo> pointShowVoList = points.stream().map(PointShowVo::new).collect(Collectors.toList());

        //3、查询封装关联的前后序知识点名称列表

        Iterator<PointShowVo> iterator = pointShowVoList.iterator();
        while (iterator.hasNext()) {
            LambdaQueryWrapper<PointRelation> prLQW1 = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<PointRelation> prLQW2 = new LambdaQueryWrapper<>();
            PointShowVo next = iterator.next();
            String pointId = next.getPointId();
            //a）用id去关系表中找后序知识点id
            List<PointIdNameVo> pointAfterList = new ArrayList<>();
            prLQW1.eq(PointRelation::getPointAId, pointId)
                    .eq(PointRelation::getRelation, POINT_RELATION_PRE_AFTER)
                    .select(PointRelation::getPointBId);
            List<PointRelation> pointAfter = pointRelationMapper.selectList(prLQW1);

            for (PointRelation pointRelation : pointAfter) {
                //只包含知识点id和name的vo
                PointIdNameVo pointIdNameVo = new PointIdNameVo();
                pointIdNameVo.setPointId(pointRelation.getPointBId());
                //通过知识点id从知识点表获取对应的名称
                String pointBId = pointRelation.getPointBId();
                pointLqw = new LambdaQueryWrapper<>();
                pointLqw.eq(Point::getPointId, pointBId)
                        .select(Point::getPointName);
                Point pointName = pointMapper.selectOne(pointLqw);
                pointIdNameVo.setPointName(pointName.getPointName());
                pointAfterList.add(pointIdNameVo);
            }
            next.setAfterPoints(pointAfterList);

            //b）用id去关系表中找前序知识点id
            List<PointIdNameVo> pointPreList = new ArrayList<>();
            prLQW2.eq(PointRelation::getPointBId, pointId)
                    .eq(PointRelation::getRelation, POINT_RELATION_PRE_AFTER)
                    .select(PointRelation::getPointAId);
            List<PointRelation> pointPre = pointRelationMapper.selectList(prLQW2);

            for (PointRelation pointRelation : pointPre) {
                //只包含知识点id和name的vo
                PointIdNameVo pointIdNameVo = new PointIdNameVo();
                pointIdNameVo.setPointId(pointRelation.getPointAId());
                //通过知识点id从知识点表获取对应的名称
                String pointAId = pointRelation.getPointAId();
                pointLqw = new LambdaQueryWrapper<>();
                pointLqw.eq(Point::getPointId, pointAId)
                        .select(Point::getPointName);
                Point pointName = pointMapper.selectOne(pointLqw);
                pointIdNameVo.setPointName(pointName.getPointName());
                pointPreList.add(pointIdNameVo);
            }
            next.setPrePoints(pointPreList);
        }

        //4、子级知识点封装
        List<PointShowVo> levelOneList = setChildren(pointShowVoList);

        return ResponseResult.okResult(levelOneList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addPrePoint(PointAddPreDto pointAddPreDto) {
        String pointId = pointAddPreDto.getPointId();
        List<String> prePointId = pointAddPreDto.getPrePointId();

        LambdaQueryWrapper<PointRelation> prLqw = new LambdaQueryWrapper<>();
        prLqw.eq(PointRelation::getPointBId, pointId);
        pointRelationMapper.delete(prLqw);
        for (String s : prePointId) {
            PointRelation pointRelation = new PointRelation();
            pointRelation.setPointAId(s);
            pointRelation.setPointBId(pointId);
            pointRelation.setRelation(POINT_RELATION_PRE_AFTER);
            pointRelation.setCourseId(pointAddPreDto.getCourseId());
            pointRelationMapper.insert(pointRelation);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addAfterPoint(PointAddAfterDto pointAddAfterDto) {
        String pointId = pointAddAfterDto.getPointId();
        List<String> prePointId = pointAddAfterDto.getAfterPointId();

        LambdaQueryWrapper<PointRelation> afLqw = new LambdaQueryWrapper<>();
        afLqw.eq(PointRelation::getPointAId, pointId);
        pointRelationMapper.delete(afLqw);
        for (String s : prePointId) {
            PointRelation pointRelation = new PointRelation();
            pointRelation.setPointBId(s);
            pointRelation.setPointAId(pointId);
            pointRelation.setRelation(0);
            pointRelation.setCourseId(pointAddAfterDto.getCourseId());
            pointRelationMapper.insert(pointRelation);
        }
        return ResponseResult.okResult();
    }

    /**
     * 封装一级知识点的数据，也就是第一章、第二章......
     * @param pointShowVoList
     * @return
     */
    public List<PointShowVo> setChildren(List<PointShowVo> pointShowVoList) {
        //找一级菜单
        List<PointShowVo> levelOneList = pointShowVoList.stream()
                .filter(c -> c.getPointPid().equals(POINT_PARENT_ID))
                .collect(Collectors.toList());
        //根节点排序
        levelOneList.sort(new Comparator<PointShowVo>(){
            @Override
            public int compare(PointShowVo t1, PointShowVo t2) {
                //获取 第几章 中的数字字符串进行比较
                String name1 = t1.getPointName();
                String name1Sub = name1.substring(name1.indexOf("第") + 1, name1.indexOf("章"));
                String name2 = t2.getPointName();
                String name2Sub = name2.substring(name2.indexOf("第") + 1, name2.indexOf("章"));
                if(name1Sub.equals(name2Sub)) return 0;
                return Integer.parseInt(name1Sub) > Integer.parseInt(name2Sub) ? 1 : -1;
            }
        });

        for (PointShowVo pointShowVo : levelOneList) {
            recursion(pointShowVo, pointShowVoList);
        }
        return levelOneList;
    }

    /**
     * 递归查找
     * @param pointShowVo
     * @param pointShowVoList
     */
    public void recursion(PointShowVo pointShowVo, List<PointShowVo> pointShowVoList) {
        //找包含的子知识点
        List<PointShowVo> children = getChildren(pointShowVo, pointShowVoList);
        pointShowVo.setChildren(children);
        if (children.size() > 0) {
            for (PointShowVo child : children) {
                recursion(child, pointShowVoList);
            }
        }
    }

    //获取当前知识点包含的知识点列表
    public List<PointShowVo> getChildren(PointShowVo pointShowVo, List<PointShowVo> pointShowVoList) {
        List<PointShowVo> childrenList = new ArrayList<>();
        for (PointShowVo next : pointShowVoList) {
            if (next.getPointPid().equals(pointShowVo.getPointId())) {
                childrenList.add(next);
            }
        }
        return childrenList;
    }

    @Override
    public ResponseResult uploadFile(String courseId, MultipartFile file) throws Exception {
        //将excel文件内容读取为一个 list 集合
        List<PointRelationDto> list = ExcelUtil.readExcel(PointRelationDto.class, file);
        //处理第二列数据，去重存入知识点表中，第二列包含所有知识点
        HashSet<String> preAfterSet = new HashSet<>();
        HashSet<String> includeSet = new HashSet<>();
        for (PointRelationDto relationDto : list) {
//            pointSet.add(relationDto.getPointBId());
        }


        List<PointRelation> relationList = new ArrayList<>();
        LambdaQueryWrapper<Point> pointQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<PointRelation> relationQueryWrapper = new LambdaQueryWrapper<>();

        for (PointRelationDto relationDto : list) {
            pointQueryWrapper.clear();  //清空条件
            relationQueryWrapper.clear();
            //判断知识点A和B是否存在且是否不相等
            pointQueryWrapper.eq(Point::getPointId, relationDto.getPointAId())
                    .or()
                    .eq(Point::getPointId, relationDto.getPointBId());
            //将每个知识点存入知识点表

            relationQueryWrapper.eq(PointRelation::getPointAId, relationDto.getPointAId())
                    .eq(PointRelation::getPointBId, relationDto.getPointBId());
//            if (pointMapper.selectCount(pointQueryWrapper) == 2 &&
//                    pointRelationMapper.selectCount(relationQueryWrapper) == 0) {
//                relationList.add(new PointRelation(relationDto));
//            }
            relationList.add(new PointRelation(relationDto));
        }
//        saveBatch(relationList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult importExcelFile(String  courseId, MultipartFile file) throws IOException {

        EasyExcel.read( file.getInputStream(), PointRelationDto.class, new UploadDataListener(courseId)).sheet().headRowNumber(2).doRead();

        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult saveExcelFile(List<PointRelationDto> cachedDataList, String courseId) {

        //处理知识点表
        //处理章节包含关系(-1)
        List<PointRelationDto> collectFir = cachedDataList.stream().filter(item -> item.getRelation() == -1).distinct().collect(Collectors.toList());
        //章节去重
        Set<String> set = new HashSet<>();
        for (PointRelationDto pointRelationDto : collectFir) {
            set.add(pointRelationDto.getPointAId());
        }
        List<Point> setList = set.stream().filter(item->{
            Integer count = pointMapper.selectCount(new QueryWrapper<Point>()
                    .eq("name", item)
                    .eq("course_id", courseId));
            return count == 0;
        }).map(item -> {
            return new Point(item, POINT_PARENT_ID, courseId);
        }).collect(Collectors.toList());
        if(setList.size() > 0){
            pointMapper.insertBatchPoint(setList);
        }

        //BID插入
        List<Point> listBId = collectFir.stream().filter(item->{
            Integer count = pointMapper.selectCount(new QueryWrapper<Point>().eq("name", item.getPointBId()).eq("course_id", courseId));
            return count == 0;
        }).map(item -> {
            Point parent = pointMapper.selectOne(new QueryWrapper<Point>().eq("name", item.getPointAId()).eq("course_id", courseId));
            return new Point(item.getPointBId(), parent.getPointId(), courseId);
        }).collect(Collectors.toList());
        if(listBId.size() > 0){
            pointMapper.insertBatchPoint(listBId);
        }

        //获取a, b名字的point集合
        //判断父节点相同则插入
        //collectFir
        //处理关系表  章节包含关系(-1)
        for(PointRelationDto collect : collectFir){
            Point pointA = query().eq("name", collect.getPointAId()).one();
            List<Point> pointB = query().eq("name", collect.getPointBId()).list();
            for(Point point : pointB) {
                if (pointA.getPointId().equals(point.getPointPid())) {
                    PointRelation pointRelation = new PointRelation(pointA.getPointId(), point.getPointId(), courseId, -1);
                    graphService.save(pointRelation);
                    break;
                }
            }
        }

        //处理关系表  前后续(0)
        List<PointRelationDto> collects = cachedDataList.stream().filter(item -> item.getRelation() == 0).collect(Collectors.toList());
        for(PointRelationDto collect : collects){
            List<Point> listA = query().eq("name", collect.getPointAId()).list();
            List<Point> listB = query().eq("name", collect.getPointBId()).list();
            for(Point a : listA){
                for(Point b : listB){
                    if(a.getPointPid().equals(b.getPointPid())){
                        PointRelation pointRelation = new PointRelation(a.getPointId(), b.getPointId(), courseId, 0);
                        graphService.save(pointRelation);
                    }
                }
            }
        }

        return ResponseResult.okResult();
    }

    //存储学习路径
    private static final List<String> pathId = new ArrayList<>();

    //递归得到推荐路径
    public void getFront(String id, String courseId, int count){

        //得到全部的前序 用关系表进行查询
        //a_id 就是前序节点的id, 过滤掉章节节点，否则章节重要性最高
        List<PointRelation> frontList = graphService.lambdaQuery()
                .eq(PointRelation::getPointBId, id).eq(PointRelation::getRelation, 0).list();
        //如果为空则退出查找
        if(frontList.size() == 0) return;
        String str = "";
        double importantPoint = 0;
        //进行重要性排序
        for(PointRelation front : frontList) {
            //找出知识点相连的边的数量
            //找前序 通过关系表进行查找
            int frontCount = graphService.lambdaQuery()
                    .eq(PointRelation::getPointBId, front.getPointAId())
                    .eq(PointRelation::getRelation, 0).count();
            //找后序
            int backCount = graphService.lambdaQuery()
                    .eq(PointRelation::getPointAId, front.getPointAId())
                    .eq(PointRelation::getRelation, 0).count();
            //计算重要性
            double cal = (double) (frontCount + backCount) / count;
            if (importantPoint < cal) {
                //更新重要性大的知识点id
                str = front.getPointAId();
            }
        }
        pathId.add(str);
        //递归下一个point
        getFront(str, courseId, count);
    }

    @Override
    public ResponseResult getPath(String id, String courseId) {

        //通过当前节点获得当前课程，得到总知识点数
        int count = lambdaQuery().eq(Point::getCourseId, courseId).count();

        pathId.clear();
        pathId.add(id);
        //知识点，课程号，总知识点数 获取到path
        getFront(id, courseId, count);

        //进行数据封装 知识点 和 前后序
        PointGraphVo pointGraphVo = new PointGraphVo();
        //存储知识点
        List<PointNodeVo> pointNodeVos = new ArrayList<>();
        //存储前后序
        List<PointLinkVo> pointLinkVos = new ArrayList<>();
        for (int i = 0; i < pathId.size(); i++) {
            //查询知识点
            Point pointNode = lambdaQuery().eq(Point::getPointId, pathId.get(i)).eq(Point::getCourseId, courseId).one();
            PointNodeVo pointNodeVo = new PointNodeVo(pointNode.getPointName(), pointNode.getPointId(), 1);
            pointNodeVos.add(pointNodeVo);

            //查询前后序
            //判断是否到最后一个
            if(i == pathId.size() - 1){
                break;
            }
            PointRelation pointRelation = graphService.lambdaQuery()
                    .eq(PointRelation::getPointAId, pathId.get(i + 1))
                    .eq(PointRelation::getPointBId, pathId.get(i))
                    .one();
            PointLinkVo pointLinkVo = new PointLinkVo(pointRelation.getPointAId(), pointRelation.getPointBId());
            pointLinkVos.add(pointLinkVo);
        }

        //pointNode储存对应的章节点
        //通过关系表找当前节点的前序并且relation==-1的(章节--知识点)
        PointRelation chapterRelation = graphService.lambdaQuery()
                .eq(PointRelation::getPointBId, id)
                .eq(PointRelation::getRelation, -1)
                .one();
        Point chapterPoint = lambdaQuery().eq(Point::getPointId, chapterRelation.getPointAId()).one();
        PointNodeVo pointNodeVo = new PointNodeVo(chapterPoint.getPointName(), chapterPoint.getPointId(), 0);
        pointNodeVos.add(pointNodeVo);

        //pointLink存储每一个节点跟章节的前后序关系
        pathId.forEach(sonId->{
            PointRelation pointRelation = graphService.lambdaQuery()
                    .eq(PointRelation::getPointAId, chapterPoint.getPointId())
                    .eq(PointRelation::getPointBId, sonId)
                    .one();
            PointLinkVo pointLinkVo = new PointLinkVo(pointRelation.getPointAId(), pointRelation.getPointBId());
            pointLinkVos.add(pointLinkVo);
        });

        pointGraphVo.setNodeData(pointNodeVos);
        pointGraphVo.setLinkData(pointLinkVos);

        return ResponseResult.okResult(pointGraphVo);
    }
}
