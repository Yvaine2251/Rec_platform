package com.platform.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.course.entity.ClassUser;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.entity.ResponseResult;
import com.platform.entity.User;
import com.platform.mapper.UserMapper;
import com.platform.statistics.dto.LearningDataDTO;
import com.platform.statistics.service.LearningSituationService;
import com.platform.statistics.service.StatisticService;
import com.platform.statistics.utils.ScoreUtils;
import com.platform.statistics.vo.LearningSituationVO;
import com.platform.statistics.vo.QuestionRecordVO;
import com.platform.statistics.vo.StudyCountVO;
import com.platform.statistics.vo.UserPointVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.platform.statistics.enums.StatisticsEnum.ExportFail;

/**
 * @author Yvaine
 * @date 2023/3/30
 * @description
 */
@Service
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class LearningSituationServiceImpl implements LearningSituationService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    ClassUserMapper classUserMapper;

    @Autowired
    StatisticService statisticService;

    /**
     * 展示学生学习情况：视频播放时间，获取资源学习次数，获取做题记录
     *
     * @param learningDataDTO
     * @return
     */
    public ResponseResult showLearningSituation(LearningDataDTO learningDataDTO) {
        String courseId = learningDataDTO.getCourseId();
        String classId = learningDataDTO.getClassId();

        //通过 classId 查co_class_user表中 对应班级的所有user
        LambdaQueryWrapper<ClassUser> classUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        classUserLambdaQueryWrapper.eq(ClassUser::getClassId, classId);
        List<ClassUser> classUsers = classUserMapper.selectList(classUserLambdaQueryWrapper);

        LearningSituationVO learningSituationVO = new LearningSituationVO();
        HashMap<ClassUser, Time> videoTotalTimeMap = new HashMap<>();
        HashMap<ClassUser, StudyCountVO> studyCountVOMap = new HashMap<>();
        HashMap<ClassUser, QuestionRecordVO> questionRecordVOMap = new HashMap<>();

        //查询每个学生的学习情况并封装数据
        for (ClassUser user : classUsers) {
            String userId = user.getUserId();
            User tmpUser = userMapper.selectById(userId);
            learningSituationVO.setUserName(tmpUser.getName());
            //1、视频播放时间统计
            ResponseResult videoTotalTime = statisticService.getVideoTotalTime(courseId, userId);
            Time videoTime = (Time) videoTotalTime.getData();


            //2、获取资源学习次数
            ResponseResult studyCount = statisticService.getStudyCount(courseId, userId);
            StudyCountVO studyCountVO = (StudyCountVO) studyCount.getData();

            //3、获取做题记录
            ResponseResult questionRecord = statisticService.getQuestionRecord(courseId, userId);
            QuestionRecordVO questionRecordVO = (QuestionRecordVO) questionRecord.getData();

            videoTotalTimeMap.put(user, videoTime);
            studyCountVOMap.put(user, studyCountVO);
            questionRecordVOMap.put(user, questionRecordVO);
        }
        learningSituationVO.setVideoTotalTimeMap(videoTotalTimeMap);
        learningSituationVO.setStudyCountVOMap(studyCountVOMap);
        learningSituationVO.setQuestionRecordVOMap(questionRecordVOMap);

        return ResponseResult.okResult(learningSituationVO);
    }

    /**
     * 导出学生学习情况
     *
     * @param learningDataDTO
     * @return
     */
    @Override
    public ResponseResult exportLearningSituation(LearningDataDTO learningDataDTO) {
        String courseId = learningDataDTO.getCourseId();
        String classId = learningDataDTO.getClassId();

        //通过 classId 查co_class_user表中 对应班级的所有user
        LambdaQueryWrapper<ClassUser> classUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        classUserLambdaQueryWrapper.eq(ClassUser::getClassId, classId);
        List<ClassUser> classUsers = classUserMapper.selectList(classUserLambdaQueryWrapper);
        HashMap<ClassUser, Time> videoTotalTimeMap = new HashMap<>();
        HashMap<ClassUser, StudyCountVO> studyCountVOMap = new HashMap<>();
        HashMap<ClassUser, QuestionRecordVO> questionRecordVOMap = new HashMap<>();

        LearningSituationVO learningSituationVO = new LearningSituationVO();
        //查询每个学生的学习情况并封装数据
        for (ClassUser user : classUsers) {
            String userId = user.getUserId();

            //1、视频播放时间统计
            ResponseResult videoTotalTime = statisticService.getVideoTotalTime(courseId, userId);
            Time videoTime = (Time) videoTotalTime.getData();


            //2、获取资源学习次数
            ResponseResult studyCount = statisticService.getStudyCount(courseId, userId);
            StudyCountVO studyCountVO = (StudyCountVO) studyCount.getData();

            //3、获取做题记录
            ResponseResult questionRecord = statisticService.getQuestionRecord(courseId, userId);
            QuestionRecordVO questionRecordVO = (QuestionRecordVO) questionRecord.getData();

            videoTotalTimeMap.put(user, videoTime);
            studyCountVOMap.put(user, studyCountVO);
            questionRecordVOMap.put(user, questionRecordVO);
        }
        learningSituationVO.setVideoTotalTimeMap(videoTotalTimeMap);
        learningSituationVO.setStudyCountVOMap(studyCountVOMap);
        learningSituationVO.setQuestionRecordVOMap(questionRecordVOMap);
//      列名
        String[] headers = {"序号", "学号", "姓名", "视频播放时间", "资源学习次数", "做题记录"};
//      创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生学习情况表");
//      创建标题行
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(headers[i]);
        }


        // 按照学号分组
        //把三个map的entry放到一个stream里面
        Map<String, List<Map.Entry<ClassUser, ?>>> groupedData = Stream.of(
                videoTotalTimeMap.entrySet().stream(),
                studyCountVOMap.entrySet().stream(),
                questionRecordVOMap.entrySet().stream()
        )
                .flatMap(Function.identity())
                .collect(Collectors.groupingBy(entry -> entry.getKey().getUserId()));


        // 合并相同学号的记录,防止添加重复记录
        List<Map<String, Object>> mergedData = new ArrayList<>();
        groupedData.forEach((userId, entries) -> {
            Map<String, Object> record = new HashMap<>();
            record.put("学号", userId);
            User user = userMapper.selectById(userId);
            record.put("姓名", user.getName());
            //遍历该学号在三个 Map 中对应的 entry，根据 entry 中的 value 类型，将相应的 value 放入 Map 中。
            entries.forEach(entry -> {
                if (entry.getValue() instanceof Time) {
                    record.put("视频播放时间", entry.getValue());
                } else if (entry.getValue() instanceof StudyCountVO) {
                    record.put("资源学习次数", ((StudyCountVO) entry.getValue()).getCompletedCount());
                } else if (entry.getValue() instanceof QuestionRecordVO) {
                    record.put("做题记录", ((QuestionRecordVO) entry.getValue()).getCompletedQuestions());
                }
            });
            mergedData.add(record);
        });

        // 导出合并后的数据到Excel
        int rowNum = 1;
        for (Map<String, Object> record : mergedData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(record.get("学号").toString());
            row.createCell(2).setCellValue(record.get("姓名").toString());
            if (record.containsKey("视频播放时间")) {
                Cell cell = row.createCell(3);
                cell.setCellValue((Time) record.get("视频播放时间"));
            }
            if (record.containsKey("资源学习次数")) {
                Cell cell = row.createCell(4);
                cell.setCellValue((Integer) record.get("资源学习次数"));
            }
            if (record.containsKey("做题记录")) {
                Cell cell = row.createCell(5);
                cell.setCellValue((Integer) record.get("做题记录"));
            }
        }


        // 遍历每一列
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i); // 自适应列宽
            int columnWidth = sheet.getColumnWidth(i); // 获取当前列的宽度
            if (columnWidth < 15 * 256) { // 设置最小宽度为15个字符宽度
                columnWidth = 15 * 256;
            }
            sheet.setColumnWidth(i, columnWidth); // 设置当前列的宽度
            // 遍历每一行，设置单元格样式
            for (Row row : sheet) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    CellStyle style = workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.CENTER); // 设置居中
                    cell.setCellStyle(style); // 设置单元格样式
                }
            }
        }


//      输出到文件
        try {
            String exportFilePath = learningDataDTO.getPath();
            exportFilePath += "学生学习情况.xlsx";
            FileOutputStream fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("导出成功");
        } catch (IOException e) {
            System.out.println("导出失败：" + e.getMessage());
            return ResponseResult.errorResult(ExportFail.getCode(), ExportFail.getMessage());
        }
        return ResponseResult.okResult();
    }
}
