package com.platform.resources.dto;

import lombok.Data;

import java.util.List;


@Data
public class ProjectTree {

    private Integer id;

    private String fileName;
    
    private String fileSize;

    private List<ProjectTree> children;

    public ProjectTree addChild(ProjectTree child){
        if(children == null){
            this.children.add(child);
        }


        return  null;
    }
}
