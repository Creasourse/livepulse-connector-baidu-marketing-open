package com.cs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs.entity.BaiduMarketingUserData;
import com.cs.mapper.BaiduMarketingUserDataMapper;
import com.cs.service.BaiduMarketingUserDataService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * 百度营销用户数据表 Service 实现
 *
 * @author Livepulse
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaiduMarketingUserDataServiceImpl extends ServiceImpl<BaiduMarketingUserDataMapper, BaiduMarketingUserData>
        implements BaiduMarketingUserDataService {

    private final BaiduMarketingUserDataMapper userDataMapper;

    // MD5 加密后的手机号格式：32 位小写十六进制
    private static final Pattern MD5_PATTERN = Pattern.compile("^[a-f0-9]{32}$");
    // 手机号明文格式：11 位数字，以 1 开头
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    // IMEI 格式：15 位数字
    private static final Pattern IMEI_PATTERN = Pattern.compile("^\\d{15}$");
    // IDFA 格式：8-4-4-4-12 的十六进制，用连字符分隔
    private static final Pattern IDFA_PATTERN = Pattern.compile("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$", Pattern.CASE_INSENSITIVE);
    // OAID 格式：16 位十六进制
    private static final Pattern OAID_PATTERN = Pattern.compile("^[a-f0-9]{16}$", Pattern.CASE_INSENSITIVE);

    @Override
    public int markProcessedByAudienceId(Long audienceId) {
        BaiduMarketingUserData userData = new BaiduMarketingUserData();
        userData.setProcessed(true);
        userData.setProcessedTime(LocalDateTime.now());
        userData.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<BaiduMarketingUserData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingUserData::getAudienceId, audienceId)
                .eq(BaiduMarketingUserData::getProcessed, false);

        return userDataMapper.update(userData, wrapper);
    }

    @Override
    public boolean validateAndSave(BaiduMarketingUserData userData) {
        boolean isValid = validateUserData(userData);
        userData.setIsValid(isValid);
        userData.setCreateTime(LocalDateTime.now());
        userData.setUpdateTime(LocalDateTime.now());
        userData.setProcessed(false);
        return userDataMapper.insert(userData) > 0;
    }

    @Override
    public int getValidUserCountByAudienceId(Long audienceId) {
        LambdaQueryWrapper<BaiduMarketingUserData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingUserData::getAudienceId, audienceId)
                .eq(BaiduMarketingUserData::getIsValid, true);
        return Math.toIntExact(userDataMapper.selectCount(wrapper));
    }

    @Override
    public int getUnprocessedCountByAudienceId(Long audienceId) {
        LambdaQueryWrapper<BaiduMarketingUserData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaiduMarketingUserData::getAudienceId, audienceId)
                .eq(BaiduMarketingUserData::getProcessed, false);
        return Math.toIntExact(userDataMapper.selectCount(wrapper));
    }

    /**
     * 验证用户数据格式
     *
     * @param userData 用户数据
     * @return 是否有效
     */
    private boolean validateUserData(BaiduMarketingUserData userData) {
        if (StringUtils.isBlank(userData.getIdValue())) {
            userData.setValidationError("ID 值不能为空");
            return false;
        }

        String idType = userData.getIdType();
        String idValue = userData.getIdValue().trim().toLowerCase();

        switch (idType) {
            case "PHONE":
                if (!PHONE_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("手机号格式错误，应为 11 位数字，以 1 开头");
                    return false;
                }
                // 自动转换为 MD5
                userData.setIdValue(DigestUtils.md5Hex(idValue));
                userData.setIdType("MD5_PHONE");
                userData.setEncryptionType("MD5");
                break;

            case "MD5_PHONE":
                if (!MD5_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("MD5 加密后的手机号格式错误，应为 32 位小写十六进制");
                    return false;
                }
                break;

            case "IMEI":
                if (!IMEI_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("IMEI 格式错误，应为 15 位数字");
                    return false;
                }
                // 自动转换为 MD5
                userData.setIdValue(DigestUtils.md5Hex(idValue));
                userData.setIdType("MD5_IMEI");
                userData.setEncryptionType("MD5");
                break;

            case "MD5_IMEI":
                if (!MD5_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("MD5 加密后的 IMEI 格式错误，应为 32 位小写十六进制");
                    return false;
                }
                break;

            case "IDFA":
                if (!IDFA_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("IDFA 格式错误，应为 8-4-4-4-12 格式的十六进制");
                    return false;
                }
                // 自动转换为 MD5（去掉连字符）
                userData.setIdValue(DigestUtils.md5Hex(idValue.replace("-", "")));
                userData.setIdType("MD5_IDFA");
                userData.setEncryptionType("MD5");
                break;

            case "MD5_IDFA":
                if (!MD5_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("MD5 加密后的 IDFA 格式错误，应为 32 位小写十六进制");
                    return false;
                }
                break;

            case "OAID":
                if (!OAID_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("OAID 格式错误，应为 16 位十六进制");
                    return false;
                }
                // 自动转换为 MD5
                userData.setIdValue(DigestUtils.md5Hex(idValue));
                userData.setIdType("MD5_OAID");
                userData.setEncryptionType("MD5");
                break;

            case "MD5_OAID":
                if (!MD5_PATTERN.matcher(idValue).matches()) {
                    userData.setValidationError("MD5 加密后的 OAID 格式错误，应为 32 位小写十六进制");
                    return false;
                }
                break;

            default:
                userData.setValidationError("不支持的 ID 类型: " + idType);
                return false;
        }

        return true;
    }
}
