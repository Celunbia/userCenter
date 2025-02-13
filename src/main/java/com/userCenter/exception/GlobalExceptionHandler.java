package com.userCenter.exception;

import com.userCenter.common.BaseResponse;
import com.userCenter.common.ErrorCode;
import com.userCenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @version 1.0
 * @Author 李嘉烨
 * @Date 2025/2/12 21:19
 * @注释
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"") ;
    }
    @ExceptionHandler(BuinessException.class)
    public BaseResponse buinessException(BuinessException e) {
        log.error("BusinessException{}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(),e.getMessage(),"") ;
    }
}
