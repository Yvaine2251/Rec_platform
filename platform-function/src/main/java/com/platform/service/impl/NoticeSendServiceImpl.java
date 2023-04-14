package com.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.entity.NoticeSend;
import com.platform.entity.NoticeSendUser;
import com.platform.entity.ResponseResult;
import com.platform.entity.User;
import com.platform.mapper.NoticeSendMapper;
import com.platform.mapper.UserMapper;
import com.platform.service.NoticeSendService;
import com.platform.service.NoticeSendUserService;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import com.platform.vo.NoticeSendVO;
import kotlin.jvm.internal.Lambda;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeSendServiceImpl extends ServiceImpl<NoticeSendMapper, NoticeSend> implements NoticeSendService {

    @Resource
    private NoticeSendUserService sendUserService;

    @Resource
    private UserMapper userMapper;

    @Override
    public ResponseResult getSendNotice() {
        //获取当前用户
        String userId = SecurityUtils.getUserId();
        //查询所有该用户的发送的邮件
        List<NoticeSend> noticeSends = lambdaQuery().eq(NoticeSend::getSendNoticeId, userId).list();
        List<NoticeSendVO> noticeSendVOS = new ArrayList<>();
        //遍历所有发送的邮件
        noticeSends.forEach(noticeSend -> {
            NoticeSendVO noticeSendVO = BeanCopyUtils.copyBean(noticeSend, NoticeSendVO.class);
            String noticeId = noticeSend.getNoticeId();
            List<NoticeSendUser> noticeSendUsers = sendUserService.lambdaQuery().eq(NoticeSendUser::getNoticeId, noticeId).list();
            //获取收到的用户名字数组
            List<String> receiveName = new ArrayList<>();
            noticeSendUsers.forEach(noticeSendUser -> {
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getUserId, noticeSendUser.getUserId());
                User user = userMapper.selectOne(queryWrapper);
                receiveName.add(user.getName());
            });
            noticeSendVO.setReceiveNames(receiveName);
            noticeSendVOS.add(noticeSendVO);
        });
        return ResponseResult.okResult(noticeSendVOS);
    }

    @Transactional
    @Override
    public ResponseResult deleteSend(String noticeId) {
        //删除发送了的信息
        LambdaQueryWrapper<NoticeSend> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeSend::getNoticeId, noticeId);
        remove(wrapper);

        //删除信息id对应的用户
        LambdaQueryWrapper<NoticeSendUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoticeSendUser::getNoticeId, noticeId);
        sendUserService.remove(queryWrapper);

        return ResponseResult.okResult();
    }
}
