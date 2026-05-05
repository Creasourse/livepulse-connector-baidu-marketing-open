package com.cs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.entity.BaiduMarketingWebhookLog;
import com.cs.mapper.BaiduMarketingWebhookLogMapper;
import com.cs.service.BaiduMarketingWebhookLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 百度营销 Webhook 事件日志表 Service 实现
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaiduMarketingWebhookLogServiceImpl extends ServiceImpl<BaiduMarketingWebhookLogMapper, BaiduMarketingWebhookLog>
        implements BaiduMarketingWebhookLogService {

    private final BaiduMarketingWebhookLogMapper webhookLogMapper;

    @Override
    public boolean markAsProcessed(Long id) {
        BaiduMarketingWebhookLog log = new BaiduMarketingWebhookLog();
        log.setId(id);
        log.setProcessedStatus("success");
        log.setProcessedTime(LocalDateTime.now());
        return webhookLogMapper.updateById(log) > 0;
    }

    @Override
    public boolean markAsFailed(Long id, String errorMessage) {
        BaiduMarketingWebhookLog log = new BaiduMarketingWebhookLog();
        log.setId(id);
        log.setProcessedStatus("failed");
        log.setErrorMessage(errorMessage);
        log.setProcessedTime(LocalDateTime.now());
        return webhookLogMapper.updateById(log) > 0;
    }

    @Override
    public boolean incrementRetryCount(Long id) {
        BaiduMarketingWebhookLog webhookLog = webhookLogMapper.selectById(id);
        if (webhookLog != null) {
            BaiduMarketingWebhookLog log = new BaiduMarketingWebhookLog();
            log.setId(id);
            log.setRetryCount(webhookLog.getRetryCount() + 1);
            return webhookLogMapper.updateById(log) > 0;
        }
        return false;
    }
}
