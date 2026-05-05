package com.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.entity.BaiduMarketingAudience;
import com.cs.mapper.BaiduMarketingAudienceMapper;
import com.cs.service.BaiduMarketingAudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 百度营销人群包表 Service 实现
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaiduMarketingAudienceServiceImpl extends ServiceImpl<BaiduMarketingAudienceMapper, BaiduMarketingAudience>
        implements BaiduMarketingAudienceService {

    private final BaiduMarketingAudienceMapper audienceMapper;

    @Override
    public BaiduMarketingAudience getByAudienceId(Long accountId, String audienceId) {
        LambdaQueryWrapper<BaiduMarketingAudience> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingAudience::getAccountId, accountId)
                .eq(BaiduMarketingAudience::getAudienceId, audienceId);
        return audienceMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateSyncStatus(Long id, String syncStatus) {
        BaiduMarketingAudience audience = new BaiduMarketingAudience();
        audience.setId(id);
        audience.setSyncStatus(syncStatus);
        audience.setSyncTime(LocalDateTime.now());
        audience.setUpdateTime(LocalDateTime.now());
        return audienceMapper.updateById(audience) > 0;
    }

    @Override
    public boolean updateAudienceStatus(Long id, String audienceStatus) {
        BaiduMarketingAudience audience = new BaiduMarketingAudience();
        audience.setId(id);
        audience.setAudienceStatus(audienceStatus);
        audience.setUpdateTime(LocalDateTime.now());
        return audienceMapper.updateById(audience) > 0;
    }

    @Override
    public boolean updateBaiduAudienceInfo(Long id, Long baiduAudienceId, String baiduAudienceName) {
        BaiduMarketingAudience audience = new BaiduMarketingAudience();
        audience.setId(id);
        audience.setBaiduAudienceId(baiduAudienceId);
        audience.setBaiduAudienceName(baiduAudienceName);
        audience.setUpdateTime(LocalDateTime.now());
        return audienceMapper.updateById(audience) > 0;
    }

    @Override
    public boolean updateUserCount(Long id, Integer userCount) {
        BaiduMarketingAudience audience = new BaiduMarketingAudience();
        audience.setId(id);
        audience.setUserCount(userCount);
        audience.setUpdateTime(LocalDateTime.now());
        return audienceMapper.updateById(audience) > 0;
    }

    @Override
    public boolean markAsProcessed(Long id) {
        BaiduMarketingAudience audience = new BaiduMarketingAudience();
        audience.setId(id);
        audience.setSyncStatus("success");
        audience.setAudienceStatus("active");
        audience.setUpdateTime(LocalDateTime.now());
        return audienceMapper.updateById(audience) > 0;
    }
}
