package com.userCenter.common;

import lombok.Getter;

/**
 * 错误码
 * @Author ye
 */
@Getter
public enum ErrorCode {
    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","")

    ;
    private final int code ;
    /**
     * 状态码信息
     */
    private final String msg ;
    /**
     * 状态码描述
     */
    private final String description ;
    ErrorCode(int code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.description = desc;
    }

}
