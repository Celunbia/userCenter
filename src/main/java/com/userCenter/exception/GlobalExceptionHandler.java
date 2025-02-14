package com.userCenter.exception;

import com.userCenter.common.BaseResponse;
import com.userCenter.common.ErrorCode;
import com.userCenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version 1.0
 * @Author 李嘉烨
 * @Date 2025/2/12 21:19
 * @注释
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public BaseResponse exception(Exception e) {
        log.error("Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"") ;
    }
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e) {
        log.error("BusinessException{}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(),e.getMessage(),"") ;
    }
}
