package com.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 百度营销 Webhook 事件日志表
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Data
@TableName("baidu_marketing_webhook_log")
@Schema(description = "百度营销 Webhook 事件日志")
public class BaiduMarketingWebhookLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("account_id")
    @Schema(description = "关联的账户 ID")
    private Long accountId;

    @TableField("event_type")
    @Schema(description = "事件类型: audience_sync/user_data_sync/status_change")
    private String eventType;

    @TableField("event_id")
    @Schema(description = "事件 ID")
    private Long eventId;

    @TableField("resource_type")
    @Schema(description = "资源类型: account/audience/user_data")
    private String resourceType;

    @TableField("resource_id")
    @Schema(description = "资源 ID")
    private Long resourceId;

    @TableField("payload")
    @Schema(description = "Webhook 负载")
    private String payload;

    @TableField("headers")
    @Schema(description = "请求头")
    private String headers;

    @TableField("processed_status")
    @Schema(description = "处理状态: pending/success/failed")
    private String processedStatus;

    @TableField("error_message")
    @Schema(description = "错误信息")
    private String errorMessage;

    @TableField("received_time")
    @Schema(description = "接收时间")
    private LocalDateTime receivedTime;

    @TableField("processed_time")
    @Schema(description = "处理时间")
    private LocalDateTime processedTime;

    @TableField("retry_count")
    @Schema(description = "重试次数")
    private Integer retryCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
