package com.userCenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.userCenter.model.domain.OperateLog;
import com.userCenter.service.OperateLogService;
import com.userCenter.mapper.OperateLogMapper;
import org.springframework.stereotype.Service;

/**
* @author Ye
* @description 针对表【operate_log(操作日志)】的数据库操作Service实现
* @createDate 2025-02-17 21:39:12
*/
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog>
    implements OperateLogService{

}




