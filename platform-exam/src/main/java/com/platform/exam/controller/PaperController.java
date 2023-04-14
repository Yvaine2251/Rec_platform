package com.platform.exam.controller;

import com.platform.entity.ResponseResult;
import com.platform.exam.dto.*;
import com.platform.exam.entity.PaperStudentAnswer;
import com.platform.exam.service.TchPaperService;
import com.platform.exam.service.StuPaperService;
import com.platform.resolver.PostRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private TchPaperService tchPaperService;

    @Autowired
    private StuPaperService stuPaperService;

    // 发布作业
    @PostMapping("/teacher/release-homework")
    public ResponseResult releaseHomework(@RequestBody ReleaseHomeworkDTO releaseHomeworkDto) {
        return tchPaperService.releaseHomework(releaseHomeworkDto);
    }

    // 新建试卷
    @PostMapping("/teacher/create")
    public ResponseResult addPaper(@RequestBody PaperAddDTO paperAddDto) {
        return tchPaperService.createPaper(paperAddDto);
    }

    // 修改试卷
    @PutMapping("/teacher/update")
    public ResponseResult updatePaper(@RequestBody PaperUpdateDTO paperUpdateDto) {
        return tchPaperService.updatePaper(paperUpdateDto);
    }

    //删除试卷
    @DeleteMapping("/teacher/delete")
    public ResponseResult deletePaper(String paperId) {
        return tchPaperService.deletePaper(paperId);
    }

    // 发布考试
    @PostMapping("/teacher/release-exam")
    public ResponseResult releaseExam(@RequestBody ReleasePaperDTO releasePaperDto) {
        return tchPaperService.releaseExam(releasePaperDto);
    }

    // 查看试卷详情（教师）
    @GetMapping("/teacher/paper-preview")
    public ResponseResult showPaperPreview(String id) {
        return tchPaperService.showPaperPreview(id);
    }

    // 显示所有试卷
    @GetMapping("/teacher/show-all")
    public ResponseResult showAllPaper(String courseId) {
        return tchPaperService.showAllPaper(courseId);
    }

    // 显示发放对象，班级和学生
    @GetMapping("/teacher/get-target")
    public ResponseResult getTarget(String courseId, String paperId) {
        return tchPaperService.getTarget(courseId, paperId);
    }

    // 查看试卷详情(学生）
    @GetMapping("/stu/paper-detail/{paperId}")
    public ResponseResult showPaperDetail(@PathVariable String paperId) {
        return stuPaperService.showPaperDetail(paperId);
    }

    // 查看考试列表(学生）
    @GetMapping("/stu/exams")
    public ResponseResult showExamList(ShowPaperDTO showPaperDto) {
        return stuPaperService.showAllExam(showPaperDto);
    }

    // 查看作业列表(学生）
    @GetMapping("/stu/homeworks")
    public ResponseResult showHomeworkList(ShowPaperDTO showPaperDto) {
        return stuPaperService.showAllHomework(showPaperDto);
    }

    // 提交作业(学生）
    @PostMapping("/stu/submit")
    public ResponseResult submitPaper(@RequestBody HomeworkSubmitDTO homeworkSubmitDto) {
        return stuPaperService.submitHomework(homeworkSubmitDto);
    }

    // 保存作业(学生）
    @PostMapping("/stu/save")
    public ResponseResult saveHomework(@RequestBody HomeworkSubmitDTO homeworkSubmitDto) {
        return stuPaperService.savePaper(homeworkSubmitDto);
    }

    // 考试单题保存(学生）
    @PostMapping("/stu/save-exam")
    public ResponseResult saveSingle(@RequestBody PaperStudentAnswer paperStudentAnswer) {
        return stuPaperService.saveSingleQuestion(paperStudentAnswer);
    }

    // 考试提交(学生）
    @PostMapping("/stu/submit-exam")
    public ResponseResult submitExam(@PostRequestParam String paperId) {
        return stuPaperService.submitExam(paperId);
    }

    // 查看已做完试卷详情(学生）
    @GetMapping("/stu/done/{paperId}")
    public ResponseResult showDonePaperDetail(@PathVariable String paperId) {
        return stuPaperService.showDonePaperDetail(paperId);
    }

    // 撤回发布的试卷
    @DeleteMapping("/teacher/revoke/{paperId}")
    public ResponseResult revokePaper(@PathVariable String paperId) {
        return tchPaperService.revokePaper(paperId);
    }
}
