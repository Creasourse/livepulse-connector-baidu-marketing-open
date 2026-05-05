package com.cs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cs.entity.BaiduMarketingSyncLog;

/**
 * 百度营销同步日志表 Service
 *
 * @author Livepulse
 * @since 1.0.0
 */
public interface BaiduMarketingSyncLogService extends IService<BaiduMarketingSyncLog> {

    /**
     * 创建同步日志
     *
     * @param accountId 账户 ID
     * @param syncType  同步类型
     * @param syncMethod 同步方式
     * @return 同步日志
     */
    BaiduMarketingSyncLog createSyncLog(Long accountId, String syncType, String syncMethod);

    /**
     * 完成同步日志
     *
     * @param id          同步日志 ID
     * @param syncStatus  同步状态
     * @param successCount 成功数量
     * @param failureCount 失败数量
     * @param errorMessage 错误信息
     * @return 是否成功
     */
    boolean completeSyncLog(Long id, String syncStatus, Integer successCount, Integer failureCount, String errorMessage);

    /**
     * 更新同步日志
     *
     * @param id          同步日志 ID
     * @param totalCount  总记录数
     * @param successCount 成功数量
     * @param failureCount 失败数量
     * @return 是否成功
     */
    boolean updateSyncLog(Long id, Integer totalCount, Integer successCount, Integer failureCount);
}
