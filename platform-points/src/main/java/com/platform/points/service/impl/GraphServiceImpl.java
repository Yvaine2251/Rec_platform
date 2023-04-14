package com.platform.points.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.entity.ResponseResult;
import com.platform.points.entity.UserPoint;
import com.platform.points.mapper.GraphSolveMapper;
import com.platform.points.mapper.UserPointMapper;
import com.platform.points.util.CSVUtil;
import com.platform.points.vo.*;
import com.platform.points.entity.Point;
import com.platform.points.entity.PointRelation;
import com.platform.points.mapper.PointMapper;
import com.platform.points.mapper.PointRelationMapper;
import com.platform.points.service.GraphService;
import com.platform.points.util.ExcelUtil;
import com.platform.util.SecurityUtils;
import com.platform.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import static com.platform.points.constants.PointConstant.POINT_PARENT_ID;

@Service
public class GraphServiceImpl extends ServiceImpl<PointRelationMapper, PointRelation> implements GraphService {

    @Autowired
    private PointRelationMapper pointRelationMapper;

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private UserPointMapper userPointMapper;

    @Autowired
    private GraphSolveMapper graphSolveMapper;

    @Override
    public ResponseResult showGraph(String courseId) {
        PointGraphVo pointGraphVo = new PointGraphVo();
        //封装知识点信息，并设置层次
        LambdaQueryWrapper<Point> pointLqw = new LambdaQueryWrapper<>();
        pointLqw.select(Point::getPointId, Point::getPointName, Point::getPointPid)
                .eq(Point::getCourseId, courseId);
        List<Point> pointList = pointMapper.selectList(pointLqw);
        List<PointNodeTempVo> pointNodeTempVos = BeanCopyUtils.copyBeanList(pointList, PointNodeTempVo.class);
        setLevel(pointNodeTempVos);
        List<PointNodeVo> pointNodeVoList = new ArrayList<>();
        for (PointNodeTempVo pointNodeTempVo : pointNodeTempVos) {
            PointNodeVo pointNodeVo = new PointNodeVo();
            pointNodeVo.setId(pointNodeTempVo.getPointId());
            pointNodeVo.setName(pointNodeTempVo.getPointName());
            pointNodeVo.setCategory(pointNodeTempVo.getLevel());
            pointNodeVoList.add(pointNodeVo);
        }
        pointGraphVo.setNodeData(pointNodeVoList);

        //封装知识点前后序关系信息
        LambdaQueryWrapper<PointRelation> relationLqw = new LambdaQueryWrapper<>();
        relationLqw.select(PointRelation::getPointAId, PointRelation::getPointBId)
                .eq(PointRelation::getCourseId, courseId);
        List<PointRelation> pointRelations = pointRelationMapper.selectList(relationLqw);
        ArrayList<PointLinkVo> pointLinkVos = new ArrayList<>();
        for (PointRelation pointRelation : pointRelations) {
            PointLinkVo pointLinkVo = new PointLinkVo();
            pointLinkVo.setSource(pointRelation.getPointAId());
            pointLinkVo.setTarget(pointRelation.getPointBId());
            pointLinkVos.add(pointLinkVo);
        }
        pointGraphVo.setLinkData(pointLinkVos);
        return ResponseResult.okResult(pointGraphVo);
    }

    /**
     * 设置知识点的层级，显示图谱里的子方法
     *
     * @param pointNodeTempVoList
     * @return
     */
    public List<PointNodeTempVo> setLevel(List<PointNodeTempVo> pointNodeTempVoList) {
        //找一级菜单
        List<PointNodeTempVo> levelList = pointNodeTempVoList.stream()
                .filter(c -> c.getPointPid().equals("555"))
                .collect(Collectors.toList());
        Deque<PointNodeTempVo> queue = new ArrayDeque<>();
        for (PointNodeTempVo pointNodeTempVo : levelList) {
            queue.offer(pointNodeTempVo);
            pointNodeTempVo.setLevel(1);
        }
        int level = 0;
        while (!queue.isEmpty()) {
            int len = queue.size();
            for (int i = 0; i < len; i++) {
                PointNodeTempVo poll = queue.poll();
                assert poll != null;
                poll.setLevel(level);
                for (PointNodeTempVo pointNodeTempVo : pointNodeTempVoList) {
                    if (poll.getPointId().equals(pointNodeTempVo.getPointPid())) {
                        queue.offer(pointNodeTempVo);
                    }
                }
            }
            level++;
        }
        return pointNodeTempVoList;
    }

    @Override
    public ResponseResult downloadRelationFile() throws Exception {
        List<PointRelation> relationList = pointRelationMapper.selectList(null);
        File file = ExcelUtil.writeExcel(PointRelation.class, relationList);

        return new ResponseResult(200, "成功", file);
    }

    @Override
    public ResponseResult showStudentKG(String courseId) {
        String userId = SecurityUtils.getUserId();
        StudentKGVo studentKGVo = new StudentKGVo();
        //封装知识点信息，掌握程度,并设置层次
        LambdaQueryWrapper<Point> pointLqw = new LambdaQueryWrapper<>();
        pointLqw.select(Point::getPointId, Point::getPointName, Point::getPointPid)
                .eq(Point::getCourseId, courseId);
        List<Point> pointList = pointMapper.selectList(pointLqw);
        List<PointNodeTempVo> pointNodeTempVos = BeanCopyUtils.copyBeanList(pointList, PointNodeTempVo.class);
        setLevel(pointNodeTempVos);
        List<PointLevelVo> pointNodeVoList = new ArrayList<>();
        for (PointNodeTempVo pointNodeTempVo : pointNodeTempVos) {
            PointLevelVo pointNodeVo = new PointLevelVo();

            //如果是不是根知识点就封装掌握度level
            String pointPid = pointNodeTempVo.getPointPid();
            String pointId = pointNodeTempVo.getPointId();
            Integer level = 100;
            if (!POINT_PARENT_ID.equals(pointPid)) {

                //通过用户知识点id和用户id去用户知识点中间表中查掌握度level
                LambdaQueryWrapper<UserPoint> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UserPoint::getUserId, userId)
                        .eq(UserPoint::getPointId, pointId)
                        .select(UserPoint::getLevel);
                UserPoint userPoint = userPointMapper.selectOne(wrapper);
                level = userPoint.getLevel();
            }


            pointNodeVo.setId(pointId);
            pointNodeVo.setName(pointNodeTempVo.getPointName());
            pointNodeVo.setCategory(pointNodeTempVo.getLevel());
            pointNodeVo.setLevel(level);
            pointNodeVoList.add(pointNodeVo);
        }
        studentKGVo.setNodeData(pointNodeVoList);

        //封装知识点前后序关系
        LambdaQueryWrapper<PointRelation> relationLqw = new LambdaQueryWrapper<>();
        relationLqw.select(PointRelation::getPointAId, PointRelation::getPointBId)
                .eq(PointRelation::getCourseId, courseId);
        List<PointRelation> pointRelations = pointRelationMapper.selectList(relationLqw);
        ArrayList<PointLinkVo> pointLinkVos = new ArrayList<>();
        for (PointRelation pointRelation : pointRelations) {
            PointLinkVo pointLinkVo = new PointLinkVo();
            pointLinkVo.setSource(pointRelation.getPointAId());
            pointLinkVo.setTarget(pointRelation.getPointBId());
            pointLinkVos.add(pointLinkVo);
        }
        studentKGVo.setLinkData(pointLinkVos);
        return ResponseResult.okResult(studentKGVo);
    }

    //用于调用运行中的python线程
    @Override
    public ResponseResult connectSocket() {
        String content = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        String str = jsonObject.toJSONString();
        str = "我发送了一堆数据";
        // 访问服务进程的套接字
        Socket socket = null;

        String HOST = "192.168.137.1";
        int PORT = 44444;

        List<String> questions = new ArrayList<>();

        System.out.println("调用远程接口:host=>" + HOST + ",port=>" + PORT);
        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(HOST, PORT);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while ((tmp = br.readLine()) != null)
                sb.append(tmp).append(' ');
            // 解析结果
            System.out.println(sb);

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {

            }
            System.out.println("远程调用结束");
        }
        return null;
    }

    //TODO 写一个能够读取数据库中数据的方法，将两个表的数据写入到两个CSV文件中
    @Override
    public JSONObject createCSV() {


        return null;
    }

    @Override
    public JSONObject createPoint2Question() {

        //传入一门课的课程号，然后对该课程下的所有题目进行一遍核查
        //生成一个该课程下 试题-知识点表csv文件
        String courseId = "1518868937486368710";
        List<JSONObject> jsonObjectList = graphSolveMapper.selectQuestionPoint(courseId);

        //存放知识点集合
        HashSet<String> pointsSet = new HashSet<>();
        //存放试题集合
        HashSet<String> questionsSet = new HashSet<>();

        for (JSONObject jsonObject : jsonObjectList) {
            if (jsonObject.get("point_id") != null)
                pointsSet.add(jsonObject.get("point_id").toString());

            if (jsonObject.get("question_id") != null)
                questionsSet.add(jsonObject.get("question_id").toString());

        }


        List<Object> pointList = Arrays.asList(pointsSet.toArray());
        List<Object> questionList = Arrays.asList(questionsSet.toArray());

        //试题号作为表头(列号)
        LinkedHashMap<Integer, Object> map = new LinkedHashMap();
        map.put(1, "pointId-questionId");
        for (int i = 0; i < questionList.size(); i++) {
            map.put(i + 2, questionList.get(i));
        }

        Map<String, String> currentRow;


        //导出数据表的内容
        List exportData = new ArrayList<Map>();

        for (int i = 0; i < pointList.size(); i++) {

            currentRow = new HashMap<>();

            currentRow.put("1", pointList.get(i).toString());

            for (int j = 0; j < questionList.size(); j++) {

                //判断pointList[i]和questionList[j]是否有联系
                for (JSONObject jsonObject : jsonObjectList) {
                    //在jsonObject中找到他们的关系
                    //如果pointid和questionid都不为空
                    if (jsonObject.get("point_id") != null && jsonObject.get("question_id") != null) {
                        //如果pointid和questionid都对应相等，那么说明他们之间存在关系，那就在表格中相应位置填1
                        if (jsonObject.get("point_id").equals(pointList.get(i)) && jsonObject.get("question_id").equals(questionList.get(j))) {
                            currentRow.put(Integer.toString(j + 2), "1");
                            //由于相同关系不会出现第二次，可以在这里break
                            break;
                        } else {
                            currentRow.put(Integer.toString(j + 2), "0");
                        }


                    } else if (jsonObject.get("point_id") != null && jsonObject.get("point_id").equals(pointList.get(i))) {
                        //point_id 与当前jsonObject的pointid相等，questionid找不到，说明他们没有关系，在表格相应位置填0
                        currentRow.put(Integer.toString(j + 2), "0");
                        //同样的，相同id不会出现第二次，在这里可以break掉
                        break;
                    }


                }


            }

            exportData.add(currentRow);

        }

        String path = "d:/export";
        String fileName = "point2question_";
        File file = CSVUtil.createCSVFile(exportData, map, path, fileName);
        String fileNameNew = file.getName();
        String pathNew = file.getPath();
        System.out.println("文件名称：" + fileNameNew);
        System.out.println("文件路径：" + pathNew);

        JSONObject returnFileMsg = new JSONObject();
        returnFileMsg.put("fileName", fileNameNew);
        returnFileMsg.put("path", pathNew);

        return returnFileMsg;
    }

    @Override
    public JSONObject createStudent2Question() {
        //传入一门课的课程号，然后对该课程下的所有题目进行一遍核查
        //生成一个该课程下 试题-知识点表csv文件
        String courseId = "1518868937486368710";
        List<JSONObject> jsonObjectList = graphSolveMapper.selectQuestionStudent(courseId);

        //存放知识点集合
        HashSet<String> studentsSet = new HashSet<>();
        //存放试题集合
        HashSet<String> questionsSet = new HashSet<>();

        for (JSONObject jsonObject : jsonObjectList) {
            if (jsonObject.get("student_id") != null) {
                studentsSet.add(jsonObject.get("student_id").toString());
            }
            if (jsonObject.get("question_id") != null) {
                questionsSet.add(jsonObject.get("question_id").toString());
            }

        }

        System.out.println(jsonObjectList);

        List<Object> studentList = Arrays.asList(studentsSet.toArray());
        List<Object> questionList = Arrays.asList(questionsSet.toArray());

        System.out.println("studentList:");
        System.out.println(studentList);
        System.out.println("questionList");
        System.out.println(questionList);


        //试题号作为表头(列号)
        LinkedHashMap<Integer, Object> map = new LinkedHashMap();
        map.put(1, "studentId-questionId");
        for (int i = 0; i < questionList.size(); i++) {
            map.put(i + 2, questionList.get(i));
        }

        Map<String, String> currentRow;

        //导出数据表的内容
        List exportData = new ArrayList<Map>();

        for (int i = 0; i < studentList.size(); i++) {

            currentRow = new HashMap<>();

            currentRow.put("1", studentList.get(i).toString());

            for (int j = 0; j < questionList.size(); j++) {

                //判断pointList[i]和questionList[j]是否有联系
                for (JSONObject jsonObject : jsonObjectList) {
                    //在jsonObject中找到他们的关系
                    //如果student_id和questionid都不为空
                    if (jsonObject.get("student_id") != null && jsonObject.get("question_id") != null) {
                        //如果student_id和questionid都对应相等，那么说明他们之间存在关系，那就在表格中相应位置填上该jsonObject对应的is_right
                        if (jsonObject.get("student_id").equals(studentList.get(i)) && jsonObject.get("question_id").equals(questionList.get(j))) {
                            currentRow.put(Integer.toString(j + 2), jsonObject.get("is_right").toString());
                        }
                    }
                }


            }

            exportData.add(currentRow);

        }

        String path = "d:/export";
        String fileName = "student2question_";
        File file = CSVUtil.createCSVFile(exportData, map, path, fileName);
        String fileNameNew = file.getName();
        String pathNew = file.getPath();
        System.out.println("文件名称：" + fileNameNew);
        System.out.println("文件路径：" + pathNew);

        JSONObject returnFileMsg = new JSONObject();
        returnFileMsg.put("fileName", fileNameNew);
        returnFileMsg.put("path", pathNew);

        return returnFileMsg;
    }

    //TODO 写一个能够调用python线程去读取两个表数据并且返回一些东西的方法


}
