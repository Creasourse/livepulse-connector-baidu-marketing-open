package com.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 百度营销账户配置表
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Data
@TableName("baidu_marketing_account")
@Schema(description = "百度营销账户配置")
public class BaiduMarketingAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("account_id")
    @Schema(description = "账户唯一标识")
    private String accountId;

    @TableField("account_name")
    @Schema(description = "账户名称")
    private String accountName;

    @TableField("api_key")
    @Schema(description = "API Key")
    private String apiKey;

    @TableField("access_token")
    @Schema(description = "访问令牌")
    private String accessToken;

    @TableField("refresh_token")
    @Schema(description = "刷新令牌")
    private String refreshToken;

    @TableField("token_expires_at")
    @Schema(description = "令牌过期时间")
    private LocalDateTime tokenExpiresAt;

    @TableField("account_type")
    @Schema(description = "账户类型: SEARCH/FEED/BRAND")
    private String accountType;

    @TableField("industry_type")
    @Schema(description = "行业类型")
    private String industryType;

    @TableField("company_name")
    @Schema(description = "公司名称")
    private String companyName;

    @TableField("enabled")
    @Schema(description = "是否启用")
    private Boolean enabled;

    @TableField("sync_status")
    @Schema(description = "同步状态: pending/syncing/success/failed")
    private String syncStatus;

    @TableField("last_sync_time")
    @Schema(description = "最后同步时间")
    private LocalDateTime lastSyncTime;

    @TableField("webhook_enabled")
    @Schema(description = "是否启用 Webhook")
    private Boolean webhookEnabled;

    @TableField("webhook_url")
    @Schema(description = "Webhook 回调 URL")
    private String webhookUrl;

    @TableField("last_error_message")
    @Schema(description = "最后错误信息")
    private String lastErrorMessage;

    @TableField("retry_count")
    @Schema(description = "重试次数")
    private Integer retryCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @TableField("create_by")
    @Schema(description = "创建人")
    private String createBy;

    @TableField("update_by")
    @Schema(description = "更新人")
    private String updateBy;
}
