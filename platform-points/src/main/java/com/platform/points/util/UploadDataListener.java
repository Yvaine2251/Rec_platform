package com.platform.points.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.platform.entity.ResponseResult;
import com.platform.points.dto.PointRelationDto;
import com.platform.points.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UploadDataListener implements ReadListener<PointRelationDto>{

    private String courseId;

    public UploadDataListener() {
    }

    public UploadDataListener(String courseId) {
        this.courseId = courseId;
    }


    public static UploadDataListener uploadDataListener;

    @PostConstruct
    public void init(){
        uploadDataListener = this;
    }

    @Autowired
    PointService pointService;

    //private static final int BATCH_COUNT = 1000;
    /**
     * 缓存的数据
     */
    //private List<PointRelationDto> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    private List<PointRelationDto> cachedDataList = new ArrayList<>();


    @Override
    public void invoke(PointRelationDto data, AnalysisContext context) {
        //log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        if(cachedDataList.size() != 0) {
            saveData();
        }
        //log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    void saveData() {

        //log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        ResponseResult responseResult = uploadDataListener.pointService.saveExcelFile(cachedDataList, courseId);

        log.info("存储数据库成功！");
    }
}