-- データベース初期化スクリプト
-- テーブル作成とテストデータ投入
-- ユーザーテーブル
CREATE TABLE
  IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- ユーザープロフィールテーブル
CREATE TABLE
  IF NOT EXISTS user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(100),
    position VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- スキルマッピングテーブル
CREATE TABLE
  IF NOT EXISTS skill_mappings (
    id BIGSERIAL PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL UNIQUE,
    skill_category VARCHAR(50) NOT NULL,
    skill_level VARCHAR(20) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- 従業員テーブル
CREATE TABLE
  IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    employment_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    experience_years INTEGER NOT NULL DEFAULT 0,
    hourly_rate DECIMAL(10, 2),
    profile TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- 従業員スキルテーブル
CREATE TABLE
  IF NOT EXISTS employee_skills (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    skill_mapping_id BIGINT NOT NULL REFERENCES skill_mappings (id) ON DELETE CASCADE,
    skill_level VARCHAR(20) NOT NULL,
    experience_years INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (employee_id, skill_mapping_id)
  );

-- 案件要件テーブル
CREATE TABLE
  IF NOT EXISTS job_requirements (
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(200) NOT NULL,
    client_name VARCHAR(200) NOT NULL,
    start_date DATE,
    end_date DATE,
    work_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    experience_years INTEGER NOT NULL DEFAULT 0,
    budget DECIMAL(15, 2),
    required_skills TEXT,
    preferred_skills TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- マッチング結果テーブル
CREATE TABLE
  IF NOT EXISTS matching_results (
    id BIGSERIAL PRIMARY KEY,
    job_requirement_id BIGINT NOT NULL REFERENCES job_requirements (id) ON DELETE CASCADE,
    employee_id BIGINT NOT NULL REFERENCES employees (id) ON DELETE CASCADE,
    matching_score DECIMAL(5, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    matched_skills TEXT,
    missing_skills TEXT,
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (job_requirement_id, employee_id)
  );

-- システムログテーブル
CREATE TABLE
  IF NOT EXISTS system_logs (
    id BIGSERIAL PRIMARY KEY,
    log_level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    details TEXT,
    user_id BIGINT REFERENCES users (id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

-- インデックスの作成
CREATE INDEX IF NOT EXISTS idx_employees_status ON employees (status);

CREATE INDEX IF NOT EXISTS idx_employees_employment_type ON employees (employment_type);

CREATE INDEX IF NOT EXISTS idx_job_requirements_status ON job_requirements (status);

CREATE INDEX IF NOT EXISTS idx_job_requirements_work_type ON job_requirements (work_type);

CREATE INDEX IF NOT EXISTS idx_matching_results_status ON matching_results (status);

CREATE INDEX IF NOT EXISTS idx_matching_results_job_requirement ON matching_results (job_requirement_id);

CREATE INDEX IF NOT EXISTS idx_matching_results_employee ON matching_results (employee_id);

CREATE INDEX IF NOT EXISTS idx_employee_skills_employee ON employee_skills (employee_id);

CREATE INDEX IF NOT EXISTS idx_employee_skills_skill ON employee_skills (skill_mapping_id);

CREATE INDEX IF NOT EXISTS idx_system_logs_level ON system_logs (log_level);

CREATE INDEX IF NOT EXISTS idx_system_logs_created_at ON system_logs (created_at);
