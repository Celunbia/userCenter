package com.userCenter.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 操作日志
 * @TableName operate_log
 */
@TableName(value ="operate_log")
@Data
public class OperateLog implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作人
     */
    private Long operatorId;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 类名
     */
    private String className;

    /**
     * 操作方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String args;

    /**
     * 耗时 ms
     */
    private Integer usedTime;

    /**
     * 返回值
     */
    private String methodReturn;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}