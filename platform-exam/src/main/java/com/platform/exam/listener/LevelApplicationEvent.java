package com.platform.exam.listener;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

public class LevelApplicationEvent extends ApplicationEvent {

    public LevelApplicationEvent(Object source, String id, Integer type) {
        super(source);
        this.id = id;
        this.type = type;
    }

    //单个题目 questionId, 作业 paperId,
    private String id;

    //单个题目 0 ， 作业 1
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
