package com.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cs.entity.BaiduMarketingAccount;
import com.cs.service.BaiduMarketingAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 百度营销账户配置管理 Controller
 *
 * @author Livepulse
 * @since 1.0.0
 */
@RestController
@RequestMapping("/baidu-marketing/account")
@RequiredArgsConstructor
@Tag(name = "百度营销账户管理", description = "百度营销账户配置管理接口")
public class BaiduMarketingAccountController {

    private final BaiduMarketingAccountService accountService;

    @PostMapping
    @Operation(summary = "添加账户配置", description = "添加新的百度营销账户配置")
    public ResponseEntity<BaiduMarketingAccount> addAccount(@RequestBody BaiduMarketingAccount account) {
        account.setCreateTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());
        account.setEnabled(true);
        account.setSyncStatus("pending");
        account.setRetryCount(0);
        accountService.save(account);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新账户配置", description = "更新百度营销账户配置")
    public ResponseEntity<BaiduMarketingAccount> updateAccount(
            @Parameter(description = "账户 ID") @PathVariable Long id,
            @RequestBody BaiduMarketingAccount account) {
        account.setId(id);
        account.setUpdateTime(LocalDateTime.now());
        accountService.updateById(account);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除账户配置", description = "删除百度营销账户配置")
    public ResponseEntity<Void> deleteAccount(@Parameter(description = "账户 ID") @PathVariable Long id) {
        accountService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取账户配置", description = "根据 ID 获取账户配置详情")
    public ResponseEntity<BaiduMarketingAccount> getAccount(@Parameter(description = "账户 ID") @PathVariable Long id) {
        BaiduMarketingAccount account = accountService.getById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping
    @Operation(summary = "分页查询账户列表", description = "分页查询账户配置列表")
    public ResponseEntity<Page<BaiduMarketingAccount>> getAccountList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "账户名称") @RequestParam(required = false) String accountName,
            @Parameter(description = "是否启用") @RequestParam(required = false) Boolean enabled) {
        Page<BaiduMarketingAccount> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BaiduMarketingAccount> wrapper = new LambdaQueryWrapper<>();
        if (accountName != null) {
            wrapper.like(BaiduMarketingAccount::getAccountName, accountName);
        }
        if (enabled != null) {
            wrapper.eq(BaiduMarketingAccount::getEnabled, enabled);
        }
        wrapper.orderByDesc(BaiduMarketingAccount::getCreateTime);
        Page<BaiduMarketingAccount> result = accountService.page(page, wrapper);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/enable")
    @Operation(summary = "启用账户", description = "启用指定的账户")
    public ResponseEntity<Void> enableAccount(@Parameter(description = "账户 ID") @PathVariable Long id) {
        accountService.enableAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/disable")
    @Operation(summary = "禁用账户", description = "禁用指定的账户")
    public ResponseEntity<Void> disableAccount(@Parameter(description = "账户 ID") @PathVariable Long id) {
        accountService.disableAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/enable-webhook")
    @Operation(summary = "启用 Webhook", description = "启用账户的 Webhook 功能")
    public ResponseEntity<Void> enableWebhook(@Parameter(description = "账户 ID") @PathVariable Long id) {
        accountService.enableWebhook(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/disable-webhook")
    @Operation(summary = "禁用 Webhook", description = "禁用账户的 Webhook 功能")
    public ResponseEntity<Void> disableWebhook(@Parameter(description = "账户 ID") @PathVariable Long id) {
        accountService.disableWebhook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list-all")
    @Operation(summary = "获取所有启用的账户", description = "获取所有启用的账户列表")
    public ResponseEntity<List<BaiduMarketingAccount>> getAllEnabledAccounts() {
        LambdaQueryWrapper<BaiduMarketingAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingAccount::getEnabled, true);
        List<BaiduMarketingAccount> accounts = accountService.list(wrapper);
        return ResponseEntity.ok(accounts);
    }
}
