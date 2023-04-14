package com.platform.enums;


import lombok.Getter;

@Getter
public enum ResourceType {
    //视频1开头
    MP4(10,".mp4"),

    //文件2开头
    PPTX(20,".pptx"),
    PPT(21, ".ppt"),
    MD(22,".md"),
    TXT(23, ".txt"),
    XLSX(25, ".xlsx"),
    pdf(24, ".pdf"),

    //图片4开头
    PNG(40,".png"),
    JPG(41,".jpg"),

    //音频5开头
    MP3(50,".mp3"),
    ;

    private final Integer type;
    private final String typeName;

    ResourceType(Integer type,String typeName){
        this.typeName = typeName;
        this.type = type;
    }
}
