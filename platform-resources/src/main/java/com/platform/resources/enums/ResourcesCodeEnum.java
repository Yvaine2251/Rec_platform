package com.platform.resources.enums;

import com.platform.constants.BaseCode;
import lombok.Getter;

/**
 * Created by 廖师兄
 */
@Getter
public enum ResourcesCodeEnum implements BaseCode {

	RESOURCES_RENAME_ERROR(501, "资源重命名失败"),

	RESOURCES_UPLOAD_ERROR(502, "资源上传失败"),

	RESOURCES_DELETE_ERROR(503, "资源删除失败"),

	RESOURCES_DOWNLOAD_ERROR(504, "资源下载失败"),

	RESOURCES_NOTFOUND_ERROR(505, "资源未找到"),

	FILE_FORMAT_NOT_SUPPORT(506, "不支持该文件格式"),
	;

	private Integer code;

	private String message;

	ResourcesCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
