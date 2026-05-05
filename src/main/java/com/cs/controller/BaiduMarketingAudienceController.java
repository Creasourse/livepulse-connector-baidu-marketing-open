package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.entity.BaiduMarketingAudience;
import com.cs.service.BaiduMarketingAudienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 百度营销人群包管理 Controller
 *
 * @author Livepulse
 * @since 1.0.0
 */
@RestController
@RequestMapping("/baidu-marketing/audience")
@RequiredArgsConstructor
@Tag(name = "百度营销人群包管理", description = "百度营销人群包管理接口")
public class BaiduMarketingAudienceController {

    private final BaiduMarketingAudienceService audienceService;

    @PostMapping
    @Operation(summary = "添加人群包", description = "添加新的人群包")
    public ResponseEntity<BaiduMarketingAudience> addAudience(@RequestBody BaiduMarketingAudience audience) {
        audience.setCreateTime(LocalDateTime.now());
        audience.setUpdateTime(LocalDateTime.now());
        audience.setAudienceStatus("pending");
        audience.setSyncStatus("pending");
        audience.setUserCount(0);
        audience.setEncryptionType("MD5");
        audienceService.save(audience);
        return ResponseEntity.ok(audience);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新人群包", description = "更新人群包信息")
    public ResponseEntity<BaiduMarketingAudience> updateAudience(
            @Parameter(description = "人群包 ID") @PathVariable Long id,
            @RequestBody BaiduMarketingAudience audience) {
        audience.setId(id);
        audience.setUpdateTime(LocalDateTime.now());
        audienceService.updateById(audience);
        return ResponseEntity.ok(audience);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除人群包", description = "删除指定的人群包")
    public ResponseEntity<Void> deleteAudience(@Parameter(description = "人群包 ID") @PathVariable Long id) {
        audienceService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取人群包详情", description = "根据 ID 获取人群包详情")
    public ResponseEntity<BaiduMarketingAudience> getAudience(@Parameter(description = "人群包 ID") @PathVariable Long id) {
        BaiduMarketingAudience audience = audienceService.getById(id);
        if (audience == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(audience);
    }

    @GetMapping
    @Operation(summary = "分页查询人群包列表", description = "分页查询人群包列表")
    public ResponseEntity<Page<BaiduMarketingAudience>> getAudienceList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "账户 ID") @RequestParam(required = false) Long accountId,
            @Parameter(description = "人群包名称") @RequestParam(required = false) String audienceName,
            @Parameter(description = "人群包类型") @RequestParam(required = false) String audienceType,
            @Parameter(description = "人群包状态") @RequestParam(required = false) String audienceStatus,
            @Parameter(description = "同步状态") @RequestParam(required = false) String syncStatus) {
        Page<BaiduMarketingAudience> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingAudience> wrapper = new LambdaQueryWrapper<>();
        if (accountId != null) {
            wrapper.eq(BaiduMarketingAudience::getAccountId, accountId);
        }
        if (audienceName != null) {
            wrapper.like(BaiduMarketingAudience::getAudienceName, audienceName);
        }
        if (audienceType != null) {
            wrapper.eq(BaiduMarketingAudience::getAudienceType, audienceType);
        }
        if (audienceStatus != null) {
            wrapper.eq(BaiduMarketingAudience::getAudienceStatus, audienceStatus);
        }
        if (syncStatus != null) {
            wrapper.eq(BaiduMarketingAudience::getSyncStatus, syncStatus);
        }
        wrapper.orderByDesc(BaiduMarketingAudience::getCreateTime);
        Page<BaiduMarketingAudience> result = audienceService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "标记为已处理", description = "将人群包标记为已处理")
    public ResponseEntity<Void> markAsProcessed(@Parameter(description = "人群包 ID") @PathVariable Long id) {
        audienceService.markAsProcessed(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sync")
    @Operation(summary = "同步人群包", description = "将人群包同步到百度营销平台")
    public ResponseEntity<Void> syncAudience(@Parameter(description = "人群包 ID") @PathVariable Long id) {
        audienceService.updateSyncStatus(id, "syncing");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "获取账户的所有人群包", description = "获取指定账户的所有人群包")
    public ResponseEntity<List<BaiduMarketingAudience>> getAudiencesByAccount(
            @Parameter(description = "账户 ID") @PathVariable Long accountId) {
        LambdaQueryWrapper<BaiduMarketingAudience> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingAudience::getAccountId, accountId);
        List<BaiduMarketingAudience> audiences = audienceService.list(wrapper);
        return ResponseEntity.ok(audiences);
    }
}
