package com.cs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.entity.BaiduMarketingSyncLog;
import com.cs.mapper.BaiduMarketingSyncLogMapper;
import com.cs.service.BaiduMarketingSyncLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 百度营销同步日志表 Service 实现
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaiduMarketingSyncLogServiceImpl extends ServiceImpl<BaiduMarketingSyncLogMapper, BaiduMarketingSyncLog>
        implements BaiduMarketingSyncLogService {

    private final BaiduMarketingSyncLogMapper syncLogMapper;

    @Override
    public BaiduMarketingSyncLog createSyncLog(Long accountId, String syncType, String syncMethod) {
        BaiduMarketingSyncLog syncLog = new BaiduMarketingSyncLog();
        syncLog.setAccountId(accountId);
        syncLog.setSyncType(syncType);
        syncLog.setSyncMethod(syncMethod);
        syncLog.setSyncStatus("running");
        syncLog.setTotalCount(0);
        syncLog.setSuccessCount(0);
        syncLog.setFailureCount(0);
        syncLog.setStartTime(LocalDateTime.now());
        syncLog.setCreateTime(LocalDateTime.now());
        syncLogMapper.insert(syncLog);
        return syncLog;
    }

    @Override
    public boolean completeSyncLog(Long id, String syncStatus, Integer successCount, Integer failureCount, String errorMessage) {
        BaiduMarketingSyncLog syncLog = syncLogMapper.selectById(id);
        if (syncLog == null) {
            return false;
        }

        BaiduMarketingSyncLog log = new BaiduMarketingSyncLog();
        log.setId(id);
        log.setSyncStatus(syncStatus);
        log.setSuccessCount(successCount);
        log.setFailureCount(failureCount);
        log.setErrorMessage(errorMessage);
        log.setEndTime(LocalDateTime.now());

        // 计算耗时
        if (syncLog.getStartTime() != null) {
            long duration = Duration.between(syncLog.getStartTime(), LocalDateTime.now()).toMillis();
            log.setDuration(duration);
        }

        return syncLogMapper.updateById(log) > 0;
    }

    @Override
    public boolean updateSyncLog(Long id, Integer totalCount, Integer successCount, Integer failureCount) {
        BaiduMarketingSyncLog log = new BaiduMarketingSyncLog();
        log.setId(id);
        log.setTotalCount(totalCount);
        log.setSuccessCount(successCount);
        log.setFailureCount(failureCount);
        return syncLogMapper.updateById(log) > 0;
    }
}
