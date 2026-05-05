package com.cs.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 百度营销用户数据表
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Data
@TableName("baidu_marketing_user_data")
@Schema(description = "百度营销用户数据")
public class BaiduMarketingUserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("audience_id")
    @Schema(description = "关联的人群包 ID")
    private Long audienceId;

    @TableField("id_type")
    @Schema(description = "ID 类型: PHONE/IMEI/IDFA/OAID/MD5_PHONE/MD5_IMEI/MD5_IDFA/MD5_OAID")
    private String idType;

    @TableField("id_value")
    @Schema(description = "ID 值（加密或明文）")
    private String idValue;

    @TableField("encryption_type")
    @Schema(description = "加密类型: MD5/SHA256/PLAIN")
    private String encryptionType;

    @TableField("is_valid")
    @Schema(description = "是否有效（格式验证结果）")
    private Boolean isValid;

    @TableField("validation_error")
    @Schema(description = "验证错误信息")
    private String validationError;

    @TableField("processed")
    @Schema(description = "是否已处理")
    private Boolean processed;

    @TableField("processed_time")
    @Schema(description = "处理时间")
    private LocalDateTime processedTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
