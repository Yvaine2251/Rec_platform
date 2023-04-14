package com.platform.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.entity.ResponseResult;
import com.platform.entity.User;
import com.platform.exam.entity.PaperStudent;
import com.platform.exam.mapper.PaperStudentMapper;
import com.platform.mapper.UserMapper;
import com.platform.statistics.vo.PaperStudentWithNameVO;
import com.platform.statistics.dto.ExportDataDTO;
import com.platform.statistics.service.AnalyseExamSituationService;
import com.platform.statistics.vo.AnalyseExamSituationVO;
import com.platform.statistics.utils.ScoreUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import static com.platform.statistics.enums.StatisticsEnum.*;

/**
 * @author Yvaine
 * @date 2023/3/28
 * @description
 */
@Service
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AnalyseExamSituationServiceImpl implements AnalyseExamSituationService {

    @Autowired
    PaperStudentMapper paperStudentMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * @param paperId
     * @return 学生分数，各分数段的人数
     */


    @Override
    public ResponseResult analyseExam(String paperId) {
//        1、通过试卷id，查到本次考试有成绩的所有学生id
        LambdaQueryWrapper<PaperStudent> ptQueryWrapper = new LambdaQueryWrapper<>();
        ptQueryWrapper.eq(PaperStudent::getPaperId, paperId);
        List<PaperStudent> students = paperStudentMapper.selectList(ptQueryWrapper);

        AnalyseExamSituationVO analyseExamSituationVO = new AnalyseExamSituationVO();

//      2、通过试卷id，学生id，查询到所有学生的试卷成绩——ex_paper_student
//      定义一个List来保存paper_score属性
        HashMap<String,Integer> scores = new HashMap<>();
        ArrayList<String> userIds = new ArrayList<>();

//        3、根据成绩分类，分层级，存入map
//      定义一个HashMap来存储成绩区间和人数的对应关系
        HashMap<String, Integer> scoreMap = new HashMap<>();
        scoreMap.put("优秀", 0);
        scoreMap.put("良好", 0);
        scoreMap.put("达标", 0);
        scoreMap.put("合格", 0);
        scoreMap.put("不合格", 0);

//      遍历PaperStudent对象的列表，根据每个对象的paper_score属性来统计成绩区间和人数的对应关系
        //scoreMap.getOrDefault(scoreRange, 0)，这个方法会尝试从 scoreMap 中获取 scoreRange 对应的值，
        // 如果该键不存在，则返回默认值 0。+ 1，表示在获取到的值上加 1，即统计该层次学生人数加 1。

        students.forEach(paperStudent -> {
            String userId = paperStudent.getStudentId();
            User user = userMapper.selectById(userId);
            int score = paperStudent.getPaperScore();
            String scoreRange = ScoreUtils.getScoreRange(score);
            scoreMap.put(scoreRange, scoreMap.getOrDefault(scoreRange, 0) + 1);
            scores.put(user.getName(),score);
            userIds.add(paperStudent.getStudentId());
        });

        analyseExamSituationVO.setScores(scores);
        analyseExamSituationVO.setScoreLevelNumbers(scoreMap);
        analyseExamSituationVO.setUserIds(userIds);
        return ResponseResult.okResult(analyseExamSituationVO);
    }


    /**
     * 导出为excel，数据有————序号，学号，学生成绩，成绩区间
     *
     * @param exportDataDTO
     * @return
     */

    @Override
    public ResponseResult exportExamData(ExportDataDTO exportDataDTO) {
        //1、通过试卷id，查到本次考试有成绩的所有学生id
        String paperId = exportDataDTO.getPaperId();
        List<PaperStudent> students = paperStudentMapper.selectList(new LambdaQueryWrapper<PaperStudent>()
                .eq(PaperStudent::getPaperId, paperId)
                .select(PaperStudent::getStudentId, PaperStudent::getPaperScore));

        List<PaperStudentWithNameVO> studentsWithName = students.stream()
                .map(ps -> {
                    PaperStudentWithNameVO studentWithName = new PaperStudentWithNameVO();
                    studentWithName.setUserId(ps.getStudentId());
                    studentWithName.setPaperScore(ps.getPaperScore());
                    studentWithName.setScoreRange(ScoreUtils.getScoreRange(ps.getPaperScore()));
                    User user = userMapper.selectById(ps.getStudentId());
                    studentWithName.setUserName(user.getName());
                    return studentWithName;
                })
                .collect(Collectors.toList());

//      列名
        String[] headers = {"序号", "学号", "姓名", "学生分数", "分数区间"};
//      创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("成绩表");
//      创建标题行
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(headers[i]);
        }


//      创建标题行
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }


//      填充数据
        int rowNum = 1;
        for (PaperStudentWithNameVO student : studentsWithName) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(student.getUserId());
            row.createCell(2).setCellValue(student.getUserName());
            row.createCell(3).setCellValue(student.getPaperScore());
            row.createCell(4).setCellValue(ScoreUtils.getScoreRange(student.getPaperScore()));
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
            String exportFilePath = exportDataDTO.getPath();
            exportFilePath += "学生分数统计表.xlsx";
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
