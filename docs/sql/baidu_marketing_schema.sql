-- 百度营销平台连接器 PostgreSQL 表结构
-- 版本: 1.0
-- 说明: 推送人群包到百度营销平台，支持MD5加密手机号、IMEI、IDFA、OAID等多种ID类型及格式验证

-- ============================================
-- 清理已存在的表（按依赖关系顺序）
-- ============================================
DROP TABLE IF EXISTS baidu_marketing_user_data CASCADE;
DROP TABLE IF EXISTS baidu_marketing_audience CASCADE;
DROP TABLE IF EXISTS baidu_marketing_webhook_log CASCADE;
DROP TABLE IF EXISTS baidu_marketing_sync_log CASCADE;
DROP TABLE IF EXISTS baidu_marketing_account CASCADE;

-- ============================================
-- 1. 百度营销账户配置表
-- ============================================
CREATE TABLE baidu_marketing_account (
    id BIGSERIAL PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    account_name VARCHAR(500),
    api_key VARCHAR(500) NOT NULL,
    access_token VARCHAR(500) NOT NULL,
    refresh_token VARCHAR(500),
    token_expires_at TIMESTAMP,
    account_type VARCHAR(50),
    industry_type VARCHAR(100),
    company_name VARCHAR(200),
    enabled BOOLEAN DEFAULT TRUE,
    sync_status VARCHAR(50) DEFAULT 'pending',
    last_sync_time TIMESTAMP,
    webhook_enabled BOOLEAN DEFAULT FALSE,
    webhook_url VARCHAR(500),
    last_error_message TEXT,
    retry_count INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(100),
    update_by VARCHAR(100),
    CONSTRAINT uk_baidu_marketing_account_id UNIQUE (account_id)
);

-- 表注释
COMMENT ON TABLE baidu_marketing_account IS '百度营销账户配置表';
COMMENT ON COLUMN baidu_marketing_account.account_id IS '账户唯一标识';
COMMENT ON COLUMN baidu_marketing_account.account_name IS '账户名称';
COMMENT ON COLUMN baidu_marketing_account.api_key IS 'API Key';
COMMENT ON COLUMN baidu_marketing_account.access_token IS '访问令牌';
COMMENT ON COLUMN baidu_marketing_account.refresh_token IS '刷新令牌';
COMMENT ON COLUMN baidu_marketing_account.token_expires_at IS '令牌过期时间';
COMMENT ON COLUMN baidu_marketing_account.account_type IS '账户类型: SEARCH/FEED/BRAND';
COMMENT ON COLUMN baidu_marketing_account.industry_type IS '行业类型';
COMMENT ON COLUMN baidu_marketing_account.company_name IS '公司名称';
COMMENT ON COLUMN baidu_marketing_account.enabled IS '是否启用';
COMMENT ON COLUMN baidu_marketing_account.sync_status IS '同步状态: pending/syncing/success/failed';
COMMENT ON COLUMN baidu_marketing_account.last_sync_time IS '最后同步时间';
COMMENT ON COLUMN baidu_marketing_account.webhook_enabled IS '是否启用 Webhook';
COMMENT ON COLUMN baidu_marketing_account.webhook_url IS 'Webhook 回调 URL';
COMMENT ON COLUMN baidu_marketing_account.last_error_message IS '最后错误信息';
COMMENT ON COLUMN baidu_marketing_account.retry_count IS '重试次数';
COMMENT ON COLUMN baidu_marketing_account.create_time IS '创建时间';
COMMENT ON COLUMN baidu_marketing_account.update_time IS '更新时间';
COMMENT ON COLUMN baidu_marketing_account.create_by IS '创建人';
COMMENT ON COLUMN baidu_marketing_account.update_by IS '更新人';

-- ============================================
-- 2. 百度营销人群包表
-- ============================================
CREATE TABLE baidu_marketing_audience (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    audience_id VARCHAR(255) NOT NULL,
    audience_name VARCHAR(500) NOT NULL,
    audience_type VARCHAR(50) NOT NULL,
    audience_description TEXT,
    user_count INT DEFAULT 0,
    audience_status VARCHAR(50) DEFAULT 'pending',
    baidu_audience_id BIGINT,
    baidu_audience_name VARCHAR(500),
    sync_status VARCHAR(50) DEFAULT 'pending',
    sync_time TIMESTAMP,
    error_message TEXT,
    id_type VARCHAR(50) NOT NULL,
    encryption_type VARCHAR(50) DEFAULT 'MD5',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(100),
    update_by VARCHAR(100),
    CONSTRAINT fk_baidu_marketing_audience_account FOREIGN KEY (account_id) REFERENCES baidu_marketing_account(id) ON DELETE CASCADE,
    CONSTRAINT uk_baidu_marketing_audience_id UNIQUE (account_id, audience_id)
);

CREATE INDEX idx_baidu_marketing_audience_account_id ON baidu_marketing_audience(account_id);
CREATE INDEX idx_baidu_marketing_audience_type ON baidu_marketing_audience(audience_type);
CREATE INDEX idx_baidu_marketing_audience_status ON baidu_marketing_audience(audience_status);
CREATE INDEX idx_baidu_marketing_audience_sync_status ON baidu_marketing_audience(sync_status);

-- 表注释
COMMENT ON TABLE baidu_marketing_audience IS '百度营销人群包表';
COMMENT ON COLUMN baidu_marketing_audience.account_id IS '关联的账户 ID';
COMMENT ON COLUMN baidu_marketing_audience.audience_id IS '人群包唯一标识';
COMMENT ON COLUMN baidu_marketing_audience.audience_name IS '人群包名称';
COMMENT ON COLUMN baidu_marketing_audience.audience_type IS '人群包类型: CUSTOM/LOOKALIKE/EXCLUDE';
COMMENT ON COLUMN baidu_marketing_audience.audience_description IS '人群包描述';
COMMENT ON COLUMN baidu_marketing_audience.user_count IS '用户数量';
COMMENT ON COLUMN baidu_marketing_audience.audience_status IS '人群包状态: pending/active/inactive/deleted';
COMMENT ON COLUMN baidu_marketing_audience.baidu_audience_id IS '百度平台人群包 ID';
COMMENT ON COLUMN baidu_marketing_audience.baidu_audience_name IS '百度平台人群包名称';
COMMENT ON COLUMN baidu_marketing_audience.sync_status IS '同步状态: pending/syncing/success/failed';
COMMENT ON COLUMN baidu_marketing_audience.sync_time IS '同步时间';
COMMENT ON COLUMN baidu_marketing_audience.error_message IS '错误信息';
COMMENT ON COLUMN baidu_marketing_audience.id_type IS 'ID 类型: PHONE/IMEI/IDFA/OAID/MD5_PHONE/MD5_IMEI/MD5_IDFA/MD5_OAID';
COMMENT ON COLUMN baidu_marketing_audience.encryption_type IS '加密类型: MD5/SHA256/PLAIN';
COMMENT ON COLUMN baidu_marketing_audience.create_time IS '创建时间';
COMMENT ON COLUMN baidu_marketing_audience.update_time IS '更新时间';
COMMENT ON COLUMN baidu_marketing_audience.create_by IS '创建人';
COMMENT ON COLUMN baidu_marketing_audience.update_by IS '更新人';

-- ============================================
-- 3. 百度营销用户数据表
-- ============================================
CREATE TABLE baidu_marketing_user_data (
    id BIGSERIAL PRIMARY KEY,
    audience_id BIGINT NOT NULL,
    id_type VARCHAR(50) NOT NULL,
    id_value VARCHAR(500) NOT NULL,
    encryption_type VARCHAR(50) DEFAULT 'MD5',
    is_valid BOOLEAN DEFAULT TRUE,
    validation_error TEXT,
    processed BOOLEAN DEFAULT FALSE,
    processed_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_baidu_marketing_user_data_audience FOREIGN KEY (audience_id) REFERENCES baidu_marketing_audience(id) ON DELETE CASCADE
);

CREATE INDEX idx_baidu_marketing_user_data_audience_id ON baidu_marketing_user_data(audience_id);
CREATE INDEX idx_baidu_marketing_user_data_id_type ON baidu_marketing_user_data(id_type);
CREATE INDEX idx_baidu_marketing_user_data_is_valid ON baidu_marketing_user_data(is_valid);
CREATE INDEX idx_baidu_marketing_user_data_processed ON baidu_marketing_user_data(processed);

-- 表注释
COMMENT ON TABLE baidu_marketing_user_data IS '百度营销用户数据表';
COMMENT ON COLUMN baidu_marketing_user_data.audience_id IS '关联的人群包 ID';
COMMENT ON COLUMN baidu_marketing_user_data.id_type IS 'ID 类型: PHONE/IMEI/IDFA/OAID/MD5_PHONE/MD5_IMEI/MD5_IDFA/MD5_OAID';
COMMENT ON COLUMN baidu_marketing_user_data.id_value IS 'ID 值（加密或明文）';
COMMENT ON COLUMN baidu_marketing_user_data.encryption_type IS '加密类型: MD5/SHA256/PLAIN';
COMMENT ON COLUMN baidu_marketing_user_data.is_valid IS '是否有效（格式验证结果）';
COMMENT ON COLUMN baidu_marketing_user_data.validation_error IS '验证错误信息';
COMMENT ON COLUMN baidu_marketing_user_data.processed IS '是否已处理';
COMMENT ON COLUMN baidu_marketing_user_data.processed_time IS '处理时间';
COMMENT ON COLUMN baidu_marketing_user_data.create_time IS '创建时间';
COMMENT ON COLUMN baidu_marketing_user_data.update_time IS '更新时间';

-- ============================================
-- 4. 百度营销 Webhook 日志表
-- ============================================
CREATE TABLE baidu_marketing_webhook_log (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT,
    event_type VARCHAR(255) NOT NULL,
    event_id BIGINT,
    resource_type VARCHAR(100),
    resource_id BIGINT,
    payload JSONB,
    headers JSONB,
    processed_status VARCHAR(50) DEFAULT 'pending',
    error_message TEXT,
    received_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_time TIMESTAMP,
    retry_count INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_baidu_marketing_webhook_account FOREIGN KEY (account_id) REFERENCES baidu_marketing_account(id) ON DELETE SET NULL
);

CREATE INDEX idx_baidu_marketing_webhook_account_id ON baidu_marketing_webhook_log(account_id);
CREATE INDEX idx_baidu_marketing_webhook_event_type ON baidu_marketing_webhook_log(event_type);
CREATE INDEX idx_baidu_marketing_webhook_status ON baidu_marketing_webhook_log(processed_status);
CREATE INDEX idx_baidu_marketing_webhook_resource ON baidu_marketing_webhook_log(resource_type, resource_id);
CREATE INDEX idx_baidu_marketing_webhook_received_time ON baidu_marketing_webhook_log(received_time);

-- 表注释
COMMENT ON TABLE baidu_marketing_webhook_log IS '百度营销 Webhook 事件日志表';
COMMENT ON COLUMN baidu_marketing_webhook_log.account_id IS '关联的账户 ID';
COMMENT ON COLUMN baidu_marketing_webhook_log.event_type IS '事件类型: audience_sync/user_data_sync/status_change';
COMMENT ON COLUMN baidu_marketing_webhook_log.event_id IS '事件 ID';
COMMENT ON COLUMN baidu_marketing_webhook_log.resource_type IS '资源类型: account/audience/user_data';
COMMENT ON COLUMN baidu_marketing_webhook_log.resource_id IS '资源 ID';
COMMENT ON COLUMN baidu_marketing_webhook_log.payload IS 'Webhook 负载';
COMMENT ON COLUMN baidu_marketing_webhook_log.headers IS '请求头';
COMMENT ON COLUMN baidu_marketing_webhook_log.processed_status IS '处理状态: pending/success/failed';
COMMENT ON COLUMN baidu_marketing_webhook_log.error_message IS '错误信息';
COMMENT ON COLUMN baidu_marketing_webhook_log.received_time IS '接收时间';
COMMENT ON COLUMN baidu_marketing_webhook_log.processed_time IS '处理时间';
COMMENT ON COLUMN baidu_marketing_webhook_log.retry_count IS '重试次数';
COMMENT ON COLUMN baidu_marketing_webhook_log.create_time IS '创建时间';

-- ============================================
-- 5. 百度营销同步日志表
-- ============================================
CREATE TABLE baidu_marketing_sync_log (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    sync_type VARCHAR(50) NOT NULL,
    sync_method VARCHAR(50) NOT NULL,
    audience_id BIGINT,
    sync_status VARCHAR(50) DEFAULT 'running',
    total_count INT DEFAULT 0,
    success_count INT DEFAULT 0,
    failure_count INT DEFAULT 0,
    error_message TEXT,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    duration BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_baidu_marketing_sync_account FOREIGN KEY (account_id) REFERENCES baidu_marketing_account(id) ON DELETE CASCADE
);

CREATE INDEX idx_baidu_marketing_sync_account_id ON baidu_marketing_sync_log(account_id);
CREATE INDEX idx_baidu_marketing_sync_type ON baidu_marketing_sync_log(sync_type);
CREATE INDEX idx_baidu_marketing_sync_status ON baidu_marketing_sync_log(sync_status);
CREATE INDEX idx_baidu_marketing_sync_start_time ON baidu_marketing_sync_log(start_time);
CREATE INDEX idx_baidu_marketing_sync_audience_id ON baidu_marketing_sync_log(audience_id);

-- 表注释
COMMENT ON TABLE baidu_marketing_sync_log IS '百度营销同步日志表';
COMMENT ON COLUMN baidu_marketing_sync_log.account_id IS '关联的账户 ID';
COMMENT ON COLUMN baidu_marketing_sync_log.sync_type IS '同步类型: audience/user_data/full';
COMMENT ON COLUMN baidu_marketing_sync_log.sync_method IS '同步方式: scheduled/manual/webhook';
COMMENT ON COLUMN baidu_marketing_sync_log.audience_id IS '人群包 ID';
COMMENT ON COLUMN baidu_marketing_sync_log.sync_status IS '同步状态: running/success/failed';
COMMENT ON COLUMN baidu_marketing_sync_log.total_count IS '总记录数';
COMMENT ON COLUMN baidu_marketing_sync_log.success_count IS '成功数量';
COMMENT ON COLUMN baidu_marketing_sync_log.failure_count IS '失败数量';
COMMENT ON COLUMN baidu_marketing_sync_log.error_message IS '错误信息';
COMMENT ON COLUMN baidu_marketing_sync_log.start_time IS '开始时间';
COMMENT ON COLUMN baidu_marketing_sync_log.end_time IS '结束时间';
COMMENT ON COLUMN baidu_marketing_sync_log.duration IS '耗时（毫秒）';
COMMENT ON COLUMN baidu_marketing_sync_log.create_time IS '创建时间';

-- ============================================
-- 初始化数据
-- ============================================

-- 创建示例账户配置（开发环境）
-- INSERT INTO baidu_marketing_account (account_id, account_name, api_key, access_token, enabled, create_by)
-- VALUES ('baidu_marketing_123456789', '测试百度营销账户', 'your-api-key-here', 'your-access-token-here', TRUE, 'system');
