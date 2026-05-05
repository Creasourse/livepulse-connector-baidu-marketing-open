package com.cs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.entity.BaiduMarketingWebhookLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 百度营销 Webhook 事件日志表 Mapper
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Mapper
public interface BaiduMarketingWebhookLogMapper extends BaseMapper<BaiduMarketingWebhookLog> {
}
