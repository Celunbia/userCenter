package com.userCenter.common;

/**
 * 返回工具列
 * @Author ye
 */
public class ResultUtils {
    /**
     * 成功
     *
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     *
     */
    public static  BaseResponse error(ErrorCode errorCode) {

        return new BaseResponse<>(errorCode);
    }
    /**
     * 失败
     *
     */

    public static  BaseResponse error(ErrorCode errorCode,String description) {
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMsg(),description) ;
    }
    /**
     * 失败
     *
     */
    public static  BaseResponse error(ErrorCode errorCode,String msg ,String description) {
        return new BaseResponse(errorCode.getCode(),null,msg,description) ;
    }

    /**
     * 失败
     *
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code,null,message,description) ;
    }
}
