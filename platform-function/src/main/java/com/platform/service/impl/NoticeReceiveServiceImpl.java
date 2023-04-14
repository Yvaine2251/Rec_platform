package com.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.constant.NoticeConstant;
import com.platform.dto.NoticeSendDTO;
import com.platform.entity.*;
import com.platform.mapper.NoticeReceiveMapper;
import com.platform.mapper.UserMapper;
import com.platform.service.NoticeReceiveService;
import com.platform.service.NoticeSendService;
import com.platform.service.NoticeSendUserService;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import com.platform.vo.NoticeReceiveVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NoticeReceiveServiceImpl extends ServiceImpl<NoticeReceiveMapper, NoticeReceive> implements NoticeReceiveService {

    @Resource
    private NoticeSendService sendService;

    @Resource
    private NoticeSendUserService sendUserService;

    @Resource
    private UserMapper userMapper;

    @Transactional
    @Override
    public ResponseResult sendNotice(NoticeSendDTO noticeReceiveDTO) {
        //获取发送人
        String sendId = SecurityUtils.getUserId();
        //消息id
        String noticeId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 19);
        //存储已发送邮件
        NoticeSend noticeSend = new NoticeSend(noticeId, sendId, noticeReceiveDTO.getTheme(), noticeReceiveDTO.getContent());
        sendService.save(noticeSend);

        noticeReceiveDTO.getIds().forEach(receiveId ->{
            //存储收件箱
            NoticeReceive noticeReceive = BeanCopyUtils.copyBean(noticeReceiveDTO, NoticeReceive.class);
            noticeReceive.setNoticeId(noticeId);
            noticeReceive.setSendNoticeId(sendId);
            noticeReceive.setReceiveNoticeId(receiveId);
            noticeReceive.setIsRead(NoticeConstant.UNREAD);
            save(noticeReceive);

            //存储中间表
            NoticeSendUser noticeSendUser = new NoticeSendUser();
            noticeSendUser.setNoticeId(noticeId);
            noticeSendUser.setUserId(receiveId);
            sendUserService.save(noticeSendUser);

        });

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getReceiveNotice() {
        //获取当前用户
        String userId = SecurityUtils.getUserId();
        //查询所有该用户的邮件
        List<NoticeReceive> noticeReceives = lambdaQuery().eq(NoticeReceive::getReceiveNoticeId, userId).list();
        List<NoticeReceiveVO> noticeReceiveVOS = new ArrayList<>();
        noticeReceives.forEach(noticeReceive -> {
            NoticeReceiveVO noticeReceiveVO = BeanCopyUtils.copyBean(noticeReceive, NoticeReceiveVO.class);
            //查询用户名
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserId, userId);
            User user = userMapper.selectOne(queryWrapper);
            noticeReceiveVO.setSendName(user.getName());
            noticeReceiveVOS.add(noticeReceiveVO);
        });

        return ResponseResult.okResult(noticeReceiveVOS);
    }

    @Transactional
    @Override
    public ResponseResult deleteReceive(String noticeId) {
        //获取当前用户
        String userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<NoticeReceive> queryWrapper = new LambdaQueryWrapper<NoticeReceive>();
        queryWrapper.eq(NoticeReceive::getNoticeId, noticeId).eq(NoticeReceive::getReceiveNoticeId, userId);
        remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult readNotice(String noticeId) {
        //获取当前用户
        String userId = SecurityUtils.getUserId();

        NoticeReceive noticeReceive = lambdaQuery()
                .eq(NoticeReceive::getNoticeId, noticeId)
                .eq(NoticeReceive::getReceiveNoticeId, userId).one();
        noticeReceive.setIsRead(NoticeConstant.READ);
        updateById(noticeReceive);
        return ResponseResult.okResult();
    }

}
