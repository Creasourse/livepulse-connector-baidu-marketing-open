package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.entity.BaiduMarketingUserData;
import com.cs.service.BaiduMarketingUserDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 百度营销用户数据管理 Controller
 *
 * @author Livepulse
 * @since 1.0.0
 */
@RestController
@RequestMapping("/baidu-marketing/user-data")
@RequiredArgsConstructor
@Tag(name = "百度营销用户数据管理", description = "百度营销用户数据管理接口")
public class BaiduMarketingUserDataController {

    private final BaiduMarketingUserDataService userDataService;

    @PostMapping
    @Operation(summary = "添加用户数据", description = "添加新的用户数据（会自动验证并加密）")
    public ResponseEntity<BaiduMarketingUserData> addUserData(@RequestBody BaiduMarketingUserData userData) {
        boolean success = userDataService.validateAndSave(userData);
        if (success) {
            return ResponseEntity.ok(userData);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/batch")
    @Operation(summary = "批量添加用户数据", description = "批量添加用户数据（会自动验证并加密）")
    public ResponseEntity<List<BaiduMarketingUserData>> batchAddUserData(
            @RequestBody List<BaiduMarketingUserData> userDataList) {
        for (BaiduMarketingUserData userData : userDataList) {
            userDataService.validateAndSave(userData);
        }
        return ResponseEntity.ok(userDataList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户数据详情", description = "根据 ID 获取用户数据详情")
    public ResponseEntity<BaiduMarketingUserData> getUserData(@Parameter(description = "用户数据 ID") @PathVariable Long id) {
        BaiduMarketingUserData userData = userDataService.getById(id);
        if (userData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userData);
    }

    @GetMapping
    @Operation(summary = "分页查询用户数据", description = "分页查询用户数据列表")
    public ResponseEntity<Page<BaiduMarketingUserData>> getUserDataList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "人群包 ID") @RequestParam(required = false) Long audienceId,
            @Parameter(description = "ID 类型") @RequestParam(required = false) String idType,
            @Parameter(description = "是否有效") @RequestParam(required = false) Boolean isValid,
            @Parameter(description = "是否已处理") @RequestParam(required = false) Boolean processed) {
        Page<BaiduMarketingUserData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingUserData> wrapper = new LambdaQueryWrapper<>();
        if (audienceId != null) {
            wrapper.eq(BaiduMarketingUserData::getAudienceId, audienceId);
        }
        if (idType != null) {
            wrapper.eq(BaiduMarketingUserData::getIdType, idType);
        }
        if (isValid != null) {
            wrapper.eq(BaiduMarketingUserData::getIsValid, isValid);
        }
        if (processed != null) {
            wrapper.eq(BaiduMarketingUserData::getProcessed, processed);
        }
        wrapper.orderByDesc(BaiduMarketingUserData::getCreateTime);
        Page<BaiduMarketingUserData> result = userDataService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "标记为已处理", description = "将用户数据标记为已处理")
    public ResponseEntity<Void> markAsProcessed(@Parameter(description = "用户数据 ID") @PathVariable Long id) {
        BaiduMarketingUserData userData = new BaiduMarketingUserData();
        userData.setId(id);
        userData.setProcessed(true);
        userDataService.updateById(userData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/audience/{audienceId}/valid-count")
    @Operation(summary = "获取有效用户数量", description = "获取指定人群包的有效用户数量")
    public ResponseEntity<Integer> getValidUserCount(
            @Parameter(description = "人群包 ID") @PathVariable Long audienceId) {
        int count = userDataService.getValidUserCountByAudienceId(audienceId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/audience/{audienceId}/unprocessed-count")
    @Operation(summary = "获取未处理用户数量", description = "获取指定人群包的未处理用户数量")
    public ResponseEntity<Integer> getUnprocessedCount(
            @Parameter(description = "人群包 ID") @PathVariable Long audienceId) {
        int count = userDataService.getUnprocessedCountByAudienceId(audienceId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/audience/{audienceId}/mark-all-processed")
    @Operation(summary = "标记所有未处理数据为已处理", description = "将指定人群包的所有未处理数据标记为已处理")
    public ResponseEntity<Integer> markAllAsProcessed(
            @Parameter(description = "人群包 ID") @PathVariable Long audienceId) {
        int count = userDataService.markProcessedByAudienceId(audienceId);
        return ResponseEntity.ok(count);
    }
}
