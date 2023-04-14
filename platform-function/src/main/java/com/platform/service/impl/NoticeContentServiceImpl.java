package com.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.config.websocket.SendMessageHandler;
import com.platform.course.entity.ClassUser;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.dto.NoticeContentDTO;
import com.platform.entity.NoticeContent;
import com.platform.entity.ResponseResult;
import com.platform.mapper.NoticeContentMapper;
import com.platform.service.NoticeContentService;
import com.platform.vo.NoticeExamVO;
import com.platform.vo.NoticeHeadlineVO;
import com.platform.vo.NoticeHomeworkVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NoticeContentServiceImpl extends ServiceImpl<NoticeContentMapper, NoticeContent> implements NoticeContentService {

    @Resource
    private ClassUserMapper classUserMapper;

    @Resource
    private NoticeContentService contentService;


    @Transactional
    @Override
    public ResponseResult sendNotice(String classId, NoticeContentDTO noticeContentDTO) {

        //获取每一个在线用户
        List<String> userIds = SendMessageHandler.clients.stream()
                .map(channel -> SendMessageHandler.channelUsernameMap.get(channel))
                .collect(Collectors.toList());

        //获取班级全部用户
        List<ClassUser> users = classUserMapper.selectList(new QueryWrapper<ClassUser>().eq("class_id", classId));
        //每一个用户保存信息
        users.stream().forEach(user->{
            NoticeContent content = BeanUtil.copyProperties(noticeContentDTO, NoticeContent.class);
            content.setUserId(user.getUserId());
            contentService.save(content);

            //如果该用户在线就发送消息
            if(userIds.contains(user.getUserId())){
                Channel channel = SendMessageHandler.usernameChannelMap.get(user.getUserId());
                channel.writeAndFlush(new TextWebSocketFrame("收到一条消息"));
            }
        });

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getNoticeContent(String userId) {

        //通过userId获取headline信息
        List<NoticeContent> contents = contentService.query().eq("user_id", userId).list();
        List<NoticeHeadlineVO> collect = contents.stream()
                .map(content -> BeanUtil.copyProperties(content, NoticeHeadlineVO.class))
                .collect(Collectors.toList());

        return ResponseResult.okResult(collect);
    }

    @Override
    public ResponseResult getExamNotice(String noticeId) {

        //通过noticeId获取exam信息
        NoticeContent content = contentService.query().eq("notice_id", noticeId).one();
        NoticeExamVO noticeExamVO = BeanUtil.copyProperties(content, NoticeExamVO.class);

        return ResponseResult.okResult(noticeExamVO);
    }

    @Override
    public ResponseResult getHomeworkNotice(String noticeId) {

        //通过noticeId获取homework信息
        NoticeContent content = contentService.query().eq("notice_id", noticeId).one();
        NoticeHomeworkVO noticeHomeworkVO = BeanUtil.copyProperties(content, NoticeHomeworkVO.class);

        return ResponseResult.okResult(noticeHomeworkVO);
    }

    @Override
    public ResponseResult deleteNotice(String noticeId) {
        contentService.removeById(noticeId);

        return ResponseResult.okResult();
    }
}
