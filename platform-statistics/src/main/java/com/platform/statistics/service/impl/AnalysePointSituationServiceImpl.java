package com.platform.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.course.entity.ClassUser;
import com.platform.course.mapper.ClassUserMapper;
import com.platform.entity.ResponseResult;
import com.platform.entity.User;
import com.platform.exception.PlatformException;
import com.platform.mapper.UserMapper;
import com.platform.points.entity.UserPoint;
import com.platform.points.mapper.UserPointMapper;
import com.platform.statistics.service.AnalysePointSituationService;
import com.platform.statistics.vo.AnalysePointVO;
import com.platform.statistics.vo.UserPointVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.platform.statistics.enums.StatisticsEnum.*;

/**
 * @author Yvaine
 * @date 2023/3/30
 * @description
 */
@Service
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AnalysePointSituationServiceImpl implements AnalysePointSituationService {



    @Autowired
    UserMapper userMapper;

    @Autowired
    ClassUserMapper classUserMapper;
    @Autowired
    UserPointMapper userPointMapper;

    @Override
    public ResponseResult analysePoint(String classId) {
        //先用班级id，查出所有学生
        LambdaQueryWrapper<ClassUser> classUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        classUserLambdaQueryWrapper.eq(ClassUser::getClassId,classId);
        List<ClassUser> classUsers = classUserMapper.selectList(classUserLambdaQueryWrapper);

        //查出所有学生对应的知识点掌握度
        HashMap<ClassUser, List<UserPointVO>> userPointMap = new HashMap<>();
        List<UserPointVO> userPointVOs = new ArrayList<>();
        for (ClassUser classUser : classUsers) {
            String userId = classUser.getUserId();
            LambdaQueryWrapper<UserPoint> userPointLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userPointLambdaQueryWrapper.eq(UserPoint::getUserId,userId);
            List<UserPoint> userPoints = userPointMapper.selectList(userPointLambdaQueryWrapper);

            if (userPoints == null) {
                throw new PlatformException(NullUserPoints.getCode(),NullUserPoints.getMessage());
            }
            for (UserPoint point :userPoints) {
                String tmpUserId = point.getUserId();
                User tmpUser = userMapper.selectById(userId);
                UserPointVO userPointVO = new UserPointVO();
                userPointVO.setUserName(tmpUser.getName());
                BeanUtils.copyProperties(point,userPointVO);
                userPointVOs.add(userPointVO);
            }

            userPointMap.put(classUser,userPointVOs);
        }

        AnalysePointVO analysePointVO = new AnalysePointVO();
        analysePointVO.setUserPointMap(userPointMap);

        return ResponseResult.okResult(analysePointVO);
    }
}
