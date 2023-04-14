package com.platform.exam.controller;

import com.platform.entity.ResponseResult;
import com.platform.exam.constants.LevelType;
import com.platform.exam.dto.*;
import com.platform.exam.listener.LevelApplicationEvent;
import com.platform.exam.service.TchQuestionService;
import com.platform.exam.service.StuQuestionService;
import com.platform.exam.utils.GetMulticaster;
import com.platform.resolver.PostRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private TchQuestionService tchQuestionService;

    @Autowired
    private StuQuestionService stuQuestionService;

    // 添加题目还需要选择知识点
    @PostMapping("/teacher/create")
    public ResponseResult addQuestion(@RequestBody QuestionAddDTO questionAddDto){
        return tchQuestionService.createNewQuestion(questionAddDto);
    }

    // 显示试题库
    @GetMapping("/teacher/show-all")
    public ResponseResult listQuestion(String courseId){
        return tchQuestionService.showAllQuestion(courseId);
    }

    // 根据题目id删除题目
    @DeleteMapping("/teacher/delete")
    public ResponseResult deleteQuestion(String questionId){
        return tchQuestionService.deleteQuestionById(questionId);
    }

    // 修改题目
    @PutMapping("/teacher/update")
    public ResponseResult updateQuestion(@RequestBody QuestionUpdateDTO questionUpdateDto) {
        return tchQuestionService.updateQuestion(questionUpdateDto);
    }

    // 展示单个题目
    @GetMapping("/teacher/show-one")
    public ResponseResult showQuestion(String questionId){
        return tchQuestionService.showQuestion(questionId);
    }

    // 学生提交单个题目
    @PostMapping("/stu/submit")
    public ResponseResult submitQuestion(@RequestBody QuestionSubmitDTO questionSubmitDto){
        return stuQuestionService.submitQuestion(questionSubmitDto);
    }


    // 展示学生题目库（缩略）
    @GetMapping("/stu/show-all")
    public ResponseResult showStuAllQuestion(String courseId){
        return stuQuestionService.showAllQuestion(courseId);
    }

    // 展示单个题目(学生)
    @GetMapping("/stu/show/{questionId}")
    public ResponseResult showStuOneQuestion(@PathVariable String questionId){
        return stuQuestionService.showOneQuestion(questionId);
    }

    // 展示错题集
    @GetMapping("/stu/show-wrong")
    public ResponseResult showWrongQuestion(ShowQuestionDTO showQuestionDto){
        return stuQuestionService.showWrongQuestion(showQuestionDto);
    }

    // 展示收藏题目
    @GetMapping("/stu/show-collect")
    public ResponseResult showCollectQuestion(ShowQuestionDTO showQuestionDto){
        return stuQuestionService.showCollectQuestion(showQuestionDto);
    }

    // 收藏题目
    @PostMapping("/stu/collect")
    public ResponseResult collectQuestion(@PostRequestParam String questionId){
        return stuQuestionService.collectQuestion(questionId);
    }

    // 取消收藏题目
    @DeleteMapping("/stu/collect/{questionId}")
    public ResponseResult cancelCollectQuestion(@PathVariable String questionId){
        return stuQuestionService.cancelCollect(questionId);
    }

    // 推荐题目
    @GetMapping("/stu/recommend")
    public ResponseResult recommendQuestion(String courseId){
        return stuQuestionService.recommendQuestion(courseId);
    }

    // 删除错题记录
    @DeleteMapping("/stu/delete-wrong/{questionId}")
    public ResponseResult deleteWrongQuestion(@PathVariable String questionId){
        return stuQuestionService.deleteWrongQuestion(questionId);
    }

    // 删除所有错题记录
    @DeleteMapping("/stu/delete-all-wrong/{courseId}")
    public ResponseResult deleteAllWrongQuestion(@PathVariable String courseId){
        return stuQuestionService.deleteAllWrongQuestion(courseId);
    }
}
