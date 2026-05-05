package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.entity.BaiduMarketingSyncLog;
import com.cs.service.BaiduMarketingSyncLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 百度营销同步日志查询 Controller
 *
 * @author Livepulse
 * @since 1.0.0
 */
@RestController
@RequestMapping("/baidu-marketing/sync-log")
@RequiredArgsConstructor
@Tag(name = "百度营销同步日志查询", description = "百度营销同步日志查询接口")
public class BaiduMarketingSyncLogController {

    private final BaiduMarketingSyncLogService syncLogService;

    @GetMapping("/{id}")
    @Operation(summary = "获取同步日志详情", description = "根据 ID 获取同步日志详情")
    public ResponseEntity<BaiduMarketingSyncLog> getSyncLog(
            @Parameter(description = "日志 ID") @PathVariable Long id) {
        BaiduMarketingSyncLog log = syncLogService.getById(id);
        if (log == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(log);
    }

    @GetMapping
    @Operation(summary = "分页查询同步日志列表", description = "分页查询同步日志列表")
    public ResponseEntity<Page<BaiduMarketingSyncLog>> getSyncLogList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "账户 ID") @RequestParam(required = false) Long accountId,
            @Parameter(description = "同步类型") @RequestParam(required = false) String syncType,
            @Parameter(description = "同步状态") @RequestParam(required = false) String syncStatus,
            @Parameter(description = "人群包 ID") @RequestParam(required = false) Long audienceId) {
        Page<BaiduMarketingSyncLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingSyncLog> wrapper = new LambdaQueryWrapper<>();
        if (accountId != null) {
            wrapper.eq(BaiduMarketingSyncLog::getAccountId, accountId);
        }
        if (syncType != null) {
            wrapper.eq(BaiduMarketingSyncLog::getSyncType, syncType);
        }
        if (syncStatus != null) {
            wrapper.eq(BaiduMarketingSyncLog::getSyncStatus, syncStatus);
        }
        if (audienceId != null) {
            wrapper.eq(BaiduMarketingSyncLog::getAudienceId, audienceId);
        }
        wrapper.orderByDesc(BaiduMarketingSyncLog::getStartTime);
        Page<BaiduMarketingSyncLog> result = syncLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "获取账户的同步日志", description = "获取指定账户的同步日志")
    public ResponseEntity<Page<BaiduMarketingSyncLog>> getSyncLogsByAccount(
            @Parameter(description = "账户 ID") @PathVariable Long accountId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BaiduMarketingSyncLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingSyncLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingSyncLog::getAccountId, accountId);
        wrapper.orderByDesc(BaiduMarketingSyncLog::getStartTime);
        Page<BaiduMarketingSyncLog> result = syncLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/audience/{audienceId}")
    @Operation(summary = "获取人群包的同步日志", description = "获取指定人群包的同步日志")
    public ResponseEntity<Page<BaiduMarketingSyncLog>> getSyncLogsByAudience(
            @Parameter(description = "人群包 ID") @PathVariable Long audienceId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BaiduMarketingSyncLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingSyncLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingSyncLog::getAudienceId, audienceId);
        wrapper.orderByDesc(BaiduMarketingSyncLog::getStartTime);
        Page<BaiduMarketingSyncLog> result = syncLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近的同步日志", description = "获取最近的同步日志")
    public ResponseEntity<Page<BaiduMarketingSyncLog>> getRecentSyncLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) String endTime) {
        Page<BaiduMarketingSyncLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingSyncLog> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(BaiduMarketingSyncLog::getStartTime, LocalDateTime.parse(startTime));
        }
        if (endTime != null) {
            wrapper.le(BaiduMarketingSyncLog::getStartTime, LocalDateTime.parse(endTime));
        }
        wrapper.orderByDesc(BaiduMarketingSyncLog::getStartTime);
        Page<BaiduMarketingSyncLog> result = syncLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/running")
    @Operation(summary = "获取正在运行的同步任务", description = "获取所有正在运行的同步任务")
    public ResponseEntity<Page<BaiduMarketingSyncLog>> getRunningSyncLogs(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BaiduMarketingSyncLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingSyncLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingSyncLog::getSyncStatus, "running");
        wrapper.orderByDesc(BaiduMarketingSyncLog::getStartTime);
        Page<BaiduMarketingSyncLog> result = syncLogService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }
}
