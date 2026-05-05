package com.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.entity.BaiduMarketingAccount;

/**
 * 百度营销账户配置表 Service
 *
 * @author Livepulse
 * @since 1.0.0
 */
public interface BaiduMarketingAccountService extends IService<BaiduMarketingAccount> {

    /**
     * 根据 API Key 获取账户
     *
     * @param apiKey API Key
     * @return 账户信息
     */
    BaiduMarketingAccount getByApiKey(String apiKey);

    /**
     * 根据 Account ID 获取账户
     *
     * @param accountId Account ID
     * @return 账户信息
     */
    BaiduMarketingAccount getByAccountId(String accountId);

    /**
     * 启用账户
     *
     * @param id 账户 ID
     * @return 是否成功
     */
    boolean enableAccount(Long id);

    /**
     * 禁用账户
     *
     * @param id 账户 ID
     * @return 是否成功
     */
    boolean disableAccount(Long id);

    /**
     * 更新同步状态
     *
     * @param id         账户 ID
     * @param syncStatus 同步状态
     * @return 是否成功
     */
    boolean updateSyncStatus(Long id, String syncStatus);

    /**
     * 更新最后同步时间
     *
     * @param id 账户 ID
     * @return 是否成功
     */
    boolean updateLastSyncTime(Long id);

    /**
     * 启用 Webhook
     *
     * @param id 账户 ID
     * @return 是否成功
     */
    boolean enableWebhook(Long id);

    /**
     * 禁用 Webhook
     *
     * @param id 账户 ID
     * @return 是否成功
     */
    boolean disableWebhook(Long id);

    /**
     * 更新访问令牌
     *
     * @param id           账户 ID
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param expiresAt    过期时间
     * @return 是否成功
     */
    boolean updateAccessToken(Long id, String accessToken, String refreshToken, String expiresAt);
}
