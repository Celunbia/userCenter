package com.userCenter.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.userCenter.anno.OperateLogAnno;
import com.userCenter.common.ErrorCode;
import com.userCenter.exception.BusinessException;
import com.userCenter.model.domain.OperateLog;
import com.userCenter.model.domain.User;
import com.userCenter.service.OperateLogService;
import com.userCenter.utils.JsonFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.userCenter.contant.userContant.USER_LOGIN_STATUS;


/**
 * @version 1.0
 * @Author ye
 */
@Slf4j
@Component
@Aspect
public class LogAop {
    @Resource
    public OperateLogService operateLogService;
    @Resource
    public HttpServletRequest request;
    @Around("@annotation(com.userCenter.anno.OperateLogAnno)")
    public Object log(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        OperateLog operateLog = new OperateLog();
        //操作开始时间
        long startTime = System.currentTimeMillis();

        //操作人id
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) attribute;
        if (user == null) {
            operateLog.setOperatorId(null);
        } else {
            operateLog.setOperatorId(user.getId());
        }
        //操作时间
        operateLog.setOperateTime(LocalDateTime.now());
        //操作类名
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        //操作方法名
        operateLog.setMethodName(joinPoint.getSignature().getName());
        //参数
        operateLog.setArgs(filterArgs(joinPoint.getArgs()));
        //执行方法
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        //执行时间
        operateLog.setUsedTime((int) (System.currentTimeMillis() - startTime));

        //返回值
        try {
            operateLog.setMethodReturn(JsonFormatter.getInstance().writeValueAsString(result));
        } catch (JsonProcessingException e) {
            // 记录错误日志
            log.error("日志序列化失败", e);
            operateLog.setMethodReturn("序列化错误");
        }
        if(!operateLogService.save(operateLog) ){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"操作日志保存失败");
        }
        return result;
    }
    private String filterArgs(Object[] args) throws JsonProcessingException {
        List<Object> filteredArgs = new ArrayList<>();
        for (Object arg : args) {
            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                filteredArgs.add(arg);
            }
        }
        return JsonFormatter.getInstance().writeValueAsString(filteredArgs);
    }
}
