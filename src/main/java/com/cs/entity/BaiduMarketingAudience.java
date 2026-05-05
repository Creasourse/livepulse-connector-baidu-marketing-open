package com.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 百度营销人群包表
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Data
@TableName("baidu_marketing_audience")
@Schema(description = "百度营销人群包")
public class BaiduMarketingAudience implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("account_id")
    @Schema(description = "关联的账户 ID")
    private Long accountId;

    @TableField("audience_id")
    @Schema(description = "人群包唯一标识")
    private String audienceId;

    @TableField("audience_name")
    @Schema(description = "人群包名称")
    private String audienceName;

    @TableField("audience_type")
    @Schema(description = "人群包类型: CUSTOM/LOOKALIKE/EXCLUDE")
    private String audienceType;

    @TableField("audience_description")
    @Schema(description = "人群包描述")
    private String audienceDescription;

    @TableField("user_count")
    @Schema(description = "用户数量")
    private Integer userCount;

    @TableField("audience_status")
    @Schema(description = "人群包状态: pending/active/inactive/deleted")
    private String audienceStatus;

    @TableField("baidu_audience_id")
    @Schema(description = "百度平台人群包 ID")
    private Long baiduAudienceId;

    @TableField("baidu_audience_name")
    @Schema(description = "百度平台人群包名称")
    private String baiduAudienceName;

    @TableField("sync_status")
    @Schema(description = "同步状态: pending/syncing/success/failed")
    private String syncStatus;

    @TableField("sync_time")
    @Schema(description = "同步时间")
    private LocalDateTime syncTime;

    @TableField("error_message")
    @Schema(description = "错误信息")
    private String errorMessage;

    @TableField("id_type")
    @Schema(description = "ID 类型: PHONE/IMEI/IDFA/OAID/MD5_PHONE/MD5_IMEI/MD5_IDFA/MD5_OAID")
    private String idType;

    @TableField("encryption_type")
    @Schema(description = "加密类型: MD5/SHA256/PLAIN")
    private String encryptionType;

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
