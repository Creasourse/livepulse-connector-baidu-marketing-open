package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.entity.BaiduMarketingWebhookLog;
import com.cs.service.BaiduMarketingWebhookLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 百度营销 Webhook 日志查询 Controller
 *
 * @author Livepulse
 * @since 1.0.0
 */
@RestController
@RequestMapping("/baidu-marketing/webhook-log")
@RequiredArgsConstructor
@Tag(name = "百度营销 Webhook 日志查询", description = "百度营销 Webhook 日志查询接口")
public class BaiduMarketingWebhookLogController {

    private final BaiduMarketingWebhookLogService webhookLogService;

    @GetMapping("/{id}")
    @Operation(summary = "获取 Webhook 日志详情", description = "根据 ID 获取 Webhook 日志详情")
    public ResponseEntity<BaiduMarketingWebhookLog> getWebhookLog(
            @Parameter(description = "日志 ID") @PathVariable Long id) {
        BaiduMarketingWebhookLog log = webhookLogService.getById(id);
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(log);
    }

    @GetMapping
    @Operation(summary = "分页查询 Webhook 日志列表", description = "分页查询 Webhook 日志列表")
    public ResponseEntity<Page<BaiduMarketingWebhookLog>> getWebhookLogList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "账户 ID") @RequestParam(required = false) Long accountId,
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "处理状态") @RequestParam(required = false) String processedStatus,
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType) {
        Page<BaiduMarketingWebhookLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingWebhookLog> wrapper = new LambdaQueryWrapper<>();
        if (accountId != null) {
            wrapper.eq(BaiduMarketingWebhookLog::getAccountId, accountId);
        }
        if (eventType != null) {
            wrapper.eq(BaiduMarketingWebhookLog::getEventType, eventType);
        }
        if (processedStatus != null) {
            wrapper.eq(BaiduMarketingWebhookLog::getProcessedStatus, processedStatus);
        }
        if (resourceType != null) {
            wrapper.eq(BaiduMarketingWebhookLog::getResourceType, resourceType);
        }
        wrapper.orderByDesc(BaiduMarketingWebhookLog::getReceivedTime);
        Page<BaiduMarketingWebhookLog> result = webhookLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/mark-processed")
    @Operation(summary = "标记为已处理", description = "将 Webhook 日志标记为已处理")
    public ResponseEntity<Void> markAsProcessed(@Parameter(description = "日志 ID") @PathVariable Long id) {
        webhookLogService.markAsProcessed(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/mark-failed")
    @Operation(summary = "标记为失败", description = "将 Webhook 日志标记为失败")
    public ResponseEntity<Void> markAsFailed(
            @Parameter(description = "日志 ID") @PathVariable Long id,
            @Parameter(description = "错误信息") @RequestParam(required = false) String errorMessage) {
        webhookLogService.markAsFailed(id, errorMessage);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/retry")
    @Operation(summary = "重试处理", description = "重试处理 Webhook 事件")
    public ResponseEntity<Void> retry(@Parameter(description = "日志 ID") @PathVariable Long id) {
        webhookLogService.incrementRetryCount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "获取账户的所有 Webhook 日志", description = "获取指定账户的所有 Webhook 日志")
    public ResponseEntity<Page<BaiduMarketingWebhookLog>> getWebhookLogsByAccount(
            @Parameter(description = "账户 ID") @PathVariable Long accountId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BaiduMarketingWebhookLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingWebhookLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingWebhookLog::getAccountId, accountId);
        wrapper.orderByDesc(BaiduMarketingWebhookLog::getReceivedTime);
        Page<BaiduMarketingWebhookLog> result = webhookLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近的 Webhook 日志", description = "获取最近收到的 Webhook 日志")
    public ResponseEntity<Page<BaiduMarketingWebhookLog>> getRecentWebhookLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime) {
        Page<BaiduMarketingWebhookLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingWebhookLog> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(BaiduMarketingWebhookLog::getReceivedTime, LocalDateTime.parse(startTime));
        }
        if (endTime != null) {
            wrapper.le(BaiduMarketingWebhookLog::getReceivedTime, LocalDateTime.parse(endTime));
        }
        wrapper.orderByDesc(BaiduMarketingWebhookLog::getReceivedTime);
        Page<BaiduMarketingWebhookLog> result = webhookLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }
}
