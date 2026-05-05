package com.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 百度营销同步日志表
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Data
@TableName("baidu_marketing_sync_log")
@Schema(description = "百度营销同步日志")
public class BaiduMarketingSyncLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("account_id")
    @Schema(description = "关联的账户 ID")
    private Long accountId;

    @TableField("sync_type")
    @Schema(description = "同步类型: audience/user_data/full")
    private String syncType;

    @TableField("sync_method")
    @Schema(description = "同步方式: scheduled/manual/webhook")
    private String syncMethod;

    @TableField("audience_id")
    @Schema(description = "人群包 ID")
    private Long audienceId;

    @TableField("sync_status")
    @Schema(description = "同步状态: running/success/failed")
    private String syncStatus;

    @TableField("total_count")
    @Schema(description = "总记录数")
    private Integer totalCount;

    @TableField("success_count")
    @Schema(description = "成功数量")
    private Integer successCount;

    @TableField("failure_count")
    @Schema(description = "失败数量")
    private Integer failureCount;

    @TableField("error_message")
    @Schema(description = "错误信息")
    private String errorMessage;

    @TableField("start_time")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @TableField("end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @TableField("duration")
    @Schema(description = "耗时（毫秒）")
    private Long duration;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
