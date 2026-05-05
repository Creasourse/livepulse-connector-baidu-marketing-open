package com.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.entity.BaiduMarketingWebhookLog;

/**
 * 百度营销 Webhook 事件日志表 Service
 *
 * @author Livepulse
 * @since 1.0.0
 */
public interface BaiduMarketingWebhookLogService extends IService<BaiduMarketingWebhookLog> {

    /**
     * 标记为已处理
     *
     * @param id 日志 ID
     * @return 是否成功
     */
    boolean markAsProcessed(Long id);

    /**
     * 标记为失败
     *
     * @param id           日志 ID
     * @param errorMessage 错误信息
     * @return 是否成功
     */
    boolean markAsFailed(Long id, String errorMessage);

    /**
     * 增加重试次数
     *
     * @param id 日志 ID
     * @return 是否成功
     */
    boolean incrementRetryCount(Long id);
}
