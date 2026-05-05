package com.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.entity.BaiduMarketingAudience;

/**
 * 百度营销人群包表 Service
 *
 * @author Livepulse
 * @since 1.0.0
 */
public interface BaiduMarketingAudienceService extends IService<BaiduMarketingAudience> {

    /**
     * 根据 Audience ID 获取人群包
     *
     * @param accountId   账户 ID
     * @param audienceId 人群包 ID
     * @return 人群包信息
     */
    BaiduMarketingAudience getByAudienceId(Long accountId, String audienceId);

    /**
     * 更新同步状态
     *
     * @param id         人群包 ID
     * @param syncStatus 同步状态
     * @return 是否成功
     */
    boolean updateSyncStatus(Long id, String syncStatus);

    /**
     * 更新人群包状态
     *
     * @param id             人群包 ID
     * @param audienceStatus 人群包状态
     * @return 是否成功
     */
    boolean updateAudienceStatus(Long id, String audienceStatus);

    /**
     * 更新百度平台人群包信息
     *
     * @param id                人群包 ID
     * @param baiduAudienceId   百度平台人群包 ID
     * @param baiduAudienceName 百度平台人群包名称
     * @return 是否成功
     */
    boolean updateBaiduAudienceInfo(Long id, Long baiduAudienceId, String baiduAudienceName);

    /**
     * 更新用户数量
     *
     * @param id        人群包 ID
     * @param userCount 用户数量
     * @return 是否成功
     */
    boolean updateUserCount(Long id, Integer userCount);

    /**
     * 标记为已处理
     *
     * @param id 人群包 ID
     * @return 是否成功
     */
    boolean markAsProcessed(Long id);
}
