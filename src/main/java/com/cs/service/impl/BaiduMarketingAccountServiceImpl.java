package com.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.entity.BaiduMarketingAccount;
import com.cs.mapper.BaiduMarketingAccountMapper;
import com.cs.service.BaiduMarketingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 百度营销账户配置表 Service 实现
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaiduMarketingAccountServiceImpl extends ServiceImpl<BaiduMarketingAccountMapper, BaiduMarketingAccount>
        implements BaiduMarketingAccountService {

    private final BaiduMarketingAccountMapper accountMapper;

    @Override
    public BaiduMarketingAccount getByApiKey(String apiKey) {
        LambdaQueryWrapper<BaiduMarketingAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingAccount::getApiKey, apiKey);
        return accountMapper.selectOne(wrapper);
    }

    @Override
    public BaiduMarketingAccount getByAccountId(String accountId) {
        LambdaQueryWrapper<BaiduMarketingAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingAccount::getAccountId, accountId);
        return accountMapper.selectOne(wrapper);
    }

    @Override
    public boolean enableAccount(Long id) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setEnabled(true);
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean disableAccount(Long id) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setEnabled(false);
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean updateSyncStatus(Long id, String syncStatus) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setSyncStatus(syncStatus);
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean updateLastSyncTime(Long id) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setLastSyncTime(LocalDateTime.now());
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean enableWebhook(Long id) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setWebhookEnabled(true);
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean disableWebhook(Long id) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setWebhookEnabled(false);
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }

    @Override
    public boolean updateAccessToken(Long id, String accessToken, String refreshToken, String expiresAt) {
        BaiduMarketingAccount account = new BaiduMarketingAccount();
        account.setId(id);
        account.setAccessToken(accessToken);
        if (refreshToken != null) {
            account.setRefreshToken(refreshToken);
        }
        if (expiresAt != null) {
            account.setTokenExpiresAt(LocalDateTime.parse(expiresAt));
        }
        account.setUpdateTime(LocalDateTime.now());
        return accountMapper.updateById(account) > 0;
    }
}
