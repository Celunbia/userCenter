package com.userCenter.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.userCenter.anno.OperateLogAnno;
import com.userCenter.common.ErrorCode;
import com.userCenter.exception.BusinessException;
import com.userCenter.model.domain.OperateLog;
import com.userCenter.model.domain.User;
import com.userCenter.service.OperateLogService;
import com.userCenter.utils.JsonFormatter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static com.userCenter.contant.userContant.USER_LOGIN_STATUS;


/**
 * @version 1.0
 * @Author ye
 */
@Component
@Aspect
public class LogAop {
    @Resource
    public OperateLogService operateLogService;
    @Resource
    public HttpServletRequest request;
    @Around("@annotation(com.userCenter.anno.OperateLogAnno)")
    public void log(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        OperateLog operateLog = new OperateLog();
        //操作开始时间
        long startTime = System.currentTimeMillis();

        //操作人id
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) attribute;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
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
        operateLog.setArgs(JsonFormatter.getInstance().writeValueAsString(joinPoint.getArgs()));
        //执行方法
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        //执行时间
        operateLog.setUsedTime((int) (System.currentTimeMillis() - startTime));

        //返回值
        operateLog.setMethodReturn(JsonFormatter.getInstance().writeValueAsString(result));
        if(!operateLogService.save(operateLog) ){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"操作日志保存失败");
        }

    }
}
