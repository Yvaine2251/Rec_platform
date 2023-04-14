package com.platform.exam.constants;

/**
 * @author ErrorRua
 * @date 2022/10/24
 * @description: 试卷类型
 */

public enum PaperType {

	//考试
	EXAM(2,"考试"),
	//作业
	HOMEWORK(1,"作业");

	private final Integer type;
	private final String typeName;

	PaperType(Integer type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}

	public static String findPaperType(Integer type){
		if(type == 0){
			return EXAM.getTypeName();
		}
		if(type == 1){
			return HOMEWORK.getTypeName();
		}
		return "wrong";
	}

	public Integer getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}
}
