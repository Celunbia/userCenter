package com.userCenter.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.userCenter.common.BaseResponse;
import com.userCenter.common.ErrorCode;
import com.userCenter.common.ResultUtils;
import com.userCenter.utils.JsonFormatter;
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
    public String exception(Exception e) throws JsonProcessingException {
        log.error("Exception", e);
        return JsonFormatter.getInstance().writeValueAsString(ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),""))  ;
    }
    @ExceptionHandler(BusinessException.class)
    public String businessException(BusinessException e) throws JsonProcessingException {
        log.error("BusinessException{}", e.getMessage(), e);
        return JsonFormatter.getInstance().writeValueAsString(ResultUtils.error(e.getCode(),e.getMessage(),"")) ;
    }
}
