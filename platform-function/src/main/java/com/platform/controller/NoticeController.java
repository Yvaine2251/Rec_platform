package com.platform.controller;

import com.platform.dto.NoticeSendDTO;
import com.platform.entity.ResponseResult;
import com.platform.service.NoticeReceiveService;
import com.platform.service.NoticeSendService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeReceiveService receiveService;

    @Resource
    private NoticeSendService sendService;

    @ApiOperation(value = "发送消息")
    @PostMapping("/send")
    public ResponseResult sendNotice(@RequestBody NoticeSendDTO noticeSendDTO){
        return receiveService.sendNotice(noticeSendDTO);
    }

    @ApiOperation(value = "查看收到邮件")
    @GetMapping("/myreceive")
    public ResponseResult getReceiveNotice(){
        return receiveService.getReceiveNotice();
    }

    @ApiOperation(value = "查看已发送的邮件")
    @GetMapping("/mysend")
    public ResponseResult getSendNotice(){
        return sendService.getSendNotice();
    }

    @ApiOperation(value = "删除收到的邮件")
    @GetMapping("/deletereceive")
    public ResponseResult deleteReceive(@RequestParam("noticeId") String noticeId){
        return receiveService.deleteReceive(noticeId);
    }

    @ApiOperation(value = "删除已发送的邮件")
    @GetMapping("/deletesend")
    public ResponseResult deleteSend(@RequestParam("noticeId") String noticeId){
        return sendService.deleteSend(noticeId);
    }

    @ApiOperation(value = "邮件已读")
    @GetMapping("/isread")
    public ResponseResult readNotice(@RequestParam("noticeId") String noticeId){
        return receiveService.readNotice(noticeId);
    }
}
