package com.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.entity.BaiduMarketingUserData;

/**
 * 百度营销用户数据表 Service
 *
 * @author Livepulse
 * @since 1.0.0
 */
public interface BaiduMarketingUserDataService extends IService<BaiduMarketingUserData> {

    /**
     * 批量标记为已处理
     *
     * @param audienceId 人群包 ID
     * @return 处理数量
     */
    int markProcessedByAudienceId(Long audienceId);

    /**
     * 验证并保存用户数据
     *
     * @param userData 用户数据
     * @return 是否成功
     */
    boolean validateAndSave(BaiduMarketingUserData userData);

    /**
     * 获取人群包的有效用户数量
     *
     * @param audienceId 人群包 ID
     * @return 有效用户数量
     */
    int getValidUserCountByAudienceId(Long audienceId);

    /**
     * 获取人群包的未处理用户数量
     *
     * @param audienceId 人群包 ID
     * @return 未处理用户数量
     */
    int getUnprocessedCountByAudienceId(Long audienceId);
}
