-- テストデータ投入スクリプト
-- ユーザーテストデータ
INSERT INTO
  users (email, password_hash, role, is_active)
VALUES
  (
    'admin@ses.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    'ADMIN',
    true
  ),
  (
    'user@ses.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    'USER',
    true
  ),
  (
    'manager@ses.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    'MANAGER',
    true
  );

-- ユーザープロフィールテストデータ
INSERT INTO
  user_profiles (
    user_id,
    first_name,
    last_name,
    phone,
    department,
    position
  )
VALUES
  (1, '管理者', '太郎', '090-1234-5678', 'IT部', 'システム管理者'),
  (2, '一般', '花子', '090-2345-6789', '営業部', '営業担当'),
  (
    3,
    'マネージャー',
    '次郎',
    '090-3456-7890',
    'IT部',
    'プロジェクトマネージャー'
  );

-- スキルマッピングテストデータ
INSERT INTO
  skill_mappings (
    skill_name,
    skill_category,
    skill_level,
    description
  )
VALUES
  ('Java', 'PROGRAMMING', 'EXPERT', 'Java開発経験5年以上'),
  (
    'Spring Boot',
    'FRAMEWORK',
    'EXPERT',
    'Spring Boot開発経験3年以上'
  ),
  (
    'PostgreSQL',
    'DATABASE',
    'INTERMEDIATE',
    'PostgreSQL運用・開発経験2年以上'
  ),
  (
    'React',
    'FRONTEND',
    'INTERMEDIATE',
    'React開発経験2年以上'
  ),
  (
    'Vue.js',
    'FRONTEND',
    'BEGINNER',
    'Vue.js開発経験1年以上'
  ),
  (
    'Docker',
    'INFRASTRUCTURE',
    'INTERMEDIATE',
    'Docker運用経験2年以上'
  ),
  ('AWS', 'CLOUD', 'INTERMEDIATE', 'AWS運用経験2年以上'),
  (
    'Python',
    'PROGRAMMING',
    'INTERMEDIATE',
    'Python開発経験2年以上'
  ),
  (
    'JavaScript',
    'PROGRAMMING',
    'EXPERT',
    'JavaScript開発経験5年以上'
  ),
  (
    'TypeScript',
    'PROGRAMMING',
    'INTERMEDIATE',
    'TypeScript開発経験2年以上'
  ),
  (
    'Node.js',
    'FRAMEWORK',
    'INTERMEDIATE',
    'Node.js開発経験2年以上'
  ),
  (
    'MySQL',
    'DATABASE',
    'INTERMEDIATE',
    'MySQL運用・開発経験2年以上'
  ),
  (
    'MongoDB',
    'DATABASE',
    'BEGINNER',
    'MongoDB開発経験1年以上'
  ),
  (
    'Kubernetes',
    'INFRASTRUCTURE',
    'INTERMEDIATE',
    'Kubernetes運用経験2年以上'
  ),
  ('Git', 'TOOL', 'EXPERT', 'Git運用経験5年以上');

-- 従業員テストデータ
INSERT INTO
  employees (
    name,
    email,
    phone,
    address,
    employment_type,
    status,
    experience_years,
    hourly_rate,
    profile
  )
VALUES
  (
    '田中 太郎',
    'tanaka@ses.com',
    '090-1111-2222',
    '東京都渋谷区',
    'FULL_TIME',
    'ACTIVE',
    5,
    8000,
    'Java開発のエキスパート。Spring Boot、PostgreSQLを使用したWebアプリケーション開発が得意。'
  ),
  (
    '佐藤 花子',
    'sato@ses.com',
    '090-3333-4444',
    '東京都新宿区',
    'CONTRACT',
    'ACTIVE',
    3,
    7000,
    'フロントエンド開発が専門。React、Vue.jsを使用したSPA開発が得意。'
  ),
  (
    '鈴木 次郎',
    'suzuki@ses.com',
    '090-5555-6666',
    '東京都港区',
    'FULL_TIME',
    'ACTIVE',
    7,
    9000,
    'フルスタックエンジニア。Java、Python、JavaScriptを使用したWebアプリケーション開発が得意。'
  ),
  (
    '高橋 美咲',
    'takahashi@ses.com',
    '090-7777-8888',
    '東京都世田谷区',
    'PART_TIME',
    'ACTIVE',
    2,
    6000,
    'データベース設計・運用が専門。PostgreSQL、MySQLの運用経験が豊富。'
  ),
  (
    '山田 健一',
    'yamada@ses.com',
    '090-9999-0000',
    '東京都目黒区',
    'FULL_TIME',
    'ACTIVE',
    4,
    7500,
    'インフラエンジニア。Docker、Kubernetes、AWSを使用したクラウドインフラ構築が得意。'
  ),
  (
    '中村 由美',
    'nakamura@ses.com',
    '090-1111-3333',
    '東京都品川区',
    'CONTRACT',
    'INACTIVE',
    6,
    8500,
    'バックエンド開発が専門。Java、Spring Boot、PostgreSQLを使用したAPI開発が得意。'
  ),
  (
    '小林 正樹',
    'kobayashi@ses.com',
    '090-2222-4444',
    '東京都大田区',
    'FULL_TIME',
    'ACTIVE',
    3,
    7000,
    'フロントエンド開発が専門。React、TypeScriptを使用したモダンなWebアプリケーション開発が得意。'
  ),
  (
    '加藤 恵子',
    'kato@ses.com',
    '090-3333-5555',
    '東京都杉並区',
    'PART_TIME',
    'ACTIVE',
    1,
    5500,
    '新卒エンジニア。Java、Spring Bootの学習中。基本的なWebアプリケーション開発が可能。'
  );

-- 従業員スキルテストデータ
INSERT INTO
  employee_skills (
    employee_id,
    skill_mapping_id,
    skill_level,
    experience_years
  )
VALUES
  -- 田中 太郎のスキル
  (1, 1, 'EXPERT', 5), -- Java
  (1, 2, 'EXPERT', 3), -- Spring Boot
  (1, 3, 'INTERMEDIATE', 2), -- PostgreSQL
  (1, 15, 'EXPERT', 5), -- Git
  -- 佐藤 花子のスキル
  (2, 4, 'INTERMEDIATE', 2), -- React
  (2, 5, 'BEGINNER', 1), -- Vue.js
  (2, 9, 'INTERMEDIATE', 2), -- JavaScript
  (2, 10, 'INTERMEDIATE', 1), -- TypeScript
  (2, 15, 'INTERMEDIATE', 3), -- Git
  -- 鈴木 次郎のスキル
  (3, 1, 'EXPERT', 7), -- Java
  (3, 2, 'EXPERT', 4), -- Spring Boot
  (3, 8, 'INTERMEDIATE', 3), -- Python
  (3, 9, 'EXPERT', 5), -- JavaScript
  (3, 11, 'INTERMEDIATE', 2), -- Node.js
  (3, 15, 'EXPERT', 7), -- Git
  -- 高橋 美咲のスキル
  (4, 3, 'INTERMEDIATE', 2), -- PostgreSQL
  (4, 12, 'INTERMEDIATE', 2), -- MySQL
  (4, 13, 'BEGINNER', 1), -- MongoDB
  (4, 15, 'INTERMEDIATE', 2), -- Git
  -- 山田 健一のスキル
  (5, 6, 'INTERMEDIATE', 2), -- Docker
  (5, 7, 'INTERMEDIATE', 2), -- AWS
  (5, 14, 'INTERMEDIATE', 1), -- Kubernetes
  (5, 15, 'INTERMEDIATE', 4), -- Git
  -- 中村 由美のスキル
  (6, 1, 'EXPERT', 6), -- Java
  (6, 2, 'EXPERT', 4), -- Spring Boot
  (6, 3, 'INTERMEDIATE', 3), -- PostgreSQL
  (6, 15, 'EXPERT', 6), -- Git
  -- 小林 正樹のスキル
  (7, 4, 'INTERMEDIATE', 2), -- React
  (7, 9, 'INTERMEDIATE', 3), -- JavaScript
  (7, 10, 'INTERMEDIATE', 2), -- TypeScript
  (7, 15, 'INTERMEDIATE', 3), -- Git
  -- 加藤 恵子のスキル
  (8, 1, 'BEGINNER', 1), -- Java
  (8, 2, 'BEGINNER', 1), -- Spring Boot
  (8, 15, 'BEGINNER', 1);

-- Git
-- 案件要件テストデータ
INSERT INTO
  job_requirements (
    project_name,
    client_name,
    start_date,
    end_date,
    work_type,
    priority,
    status,
    experience_years,
    budget,
    required_skills,
    preferred_skills,
    description
  )
VALUES
  (
    'ECサイト開発',
    'ABC商事',
    '2024-02-01',
    '2024-06-30',
    'REMOTE',
    'HIGH',
    'OPEN',
    3,
    5000000,
    'Java,Spring Boot,PostgreSQL',
    'React,TypeScript',
    'ECサイトのバックエンドAPI開発。Spring Bootを使用したRESTful APIの設計・実装。'
  ),
  (
    'モバイルアプリ開発',
    'XYZ株式会社',
    '2024-03-01',
    '2024-08-31',
    'HYBRID',
    'MEDIUM',
    'OPEN',
    2,
    3000000,
    'React,JavaScript',
    'TypeScript,Node.js',
    'React Nativeを使用したモバイルアプリケーション開発。'
  ),
  (
    'データ分析システム',
    'DEF工業',
    '2024-01-15',
    '2024-04-30',
    'ONSITE',
    'HIGH',
    'OPEN',
    4,
    4000000,
    'Python,PostgreSQL',
    'AWS,Docker',
    '製造業向けデータ分析システムの開発。Pythonを使用したデータ処理・分析機能の実装。'
  ),
  (
    'クラウド移行プロジェクト',
    'GHI銀行',
    '2024-02-15',
    '2024-07-31',
    'REMOTE',
    'HIGH',
    'OPEN',
    5,
    8000000,
    'AWS,Docker,Kubernetes',
    'Java,Spring Boot',
    '既存システムのAWS移行プロジェクト。インフラ設計・構築が主な業務。'
  ),
  (
    'Webアプリケーション開発',
    'JKL商社',
    '2024-03-15',
    '2024-09-30',
    'HYBRID',
    'MEDIUM',
    'OPEN',
    2,
    2500000,
    'Vue.js,JavaScript',
    'Node.js,MySQL',
    'Vue.jsを使用したWebアプリケーション開発。フロントエンド中心の開発業務。'
  ),
  (
    'API開発プロジェクト',
    'MNO製造',
    '2024-01-01',
    '2024-05-31',
    'REMOTE',
    'MEDIUM',
    'CLOSED',
    3,
    3500000,
    'Java,Spring Boot,PostgreSQL',
    'Docker,AWS',
    '製造業向けAPI開発プロジェクト。既に完了済み。'
  ),
  (
    'フロントエンド開発',
    'PQRサービス',
    '2024-02-01',
    '2024-06-30',
    'ONSITE',
    'LOW',
    'OPEN',
    1,
    2000000,
    'React,TypeScript',
    'Node.js,MySQL',
    'Reactを使用したフロントエンド開発。新卒レベルのスキルで対応可能。'
  );

-- マッチング結果テストデータ
INSERT INTO
  matching_results (
    job_requirement_id,
    employee_id,
    matching_score,
    status,
    matched_skills,
    missing_skills,
    comments
  )
VALUES
  -- ECサイト開発のマッチング結果
  (
    1,
    1,
    95.0,
    'APPROVED',
    'Java,Spring Boot,PostgreSQL',
    'React,TypeScript',
    'Java・Spring Bootの経験が豊富で最適な人材。'
  ),
  (
    1,
    3,
    90.0,
    'PENDING',
    'Java,Spring Boot',
    'PostgreSQL,React,TypeScript',
    'フルスタックエンジニアとして優秀だが、PostgreSQLの経験がやや不足。'
  ),
  (
    1,
    6,
    85.0,
    'REJECTED',
    'Java,Spring Boot,PostgreSQL',
    'React,TypeScript',
    'スキルは適合しているが、現在非アクティブ状態。'
  ),
  -- モバイルアプリ開発のマッチング結果
  (
    2,
    2,
    80.0,
    'PENDING',
    'React,JavaScript',
    'TypeScript,Node.js',
    'Reactの経験はあるが、モバイルアプリ開発経験は要確認。'
  ),
  (
    2,
    7,
    75.0,
    'PENDING',
    'React,TypeScript,JavaScript',
    'Node.js',
    'TypeScriptの経験があり、モバイルアプリ開発に適している。'
  ),
  -- データ分析システムのマッチング結果
  (
    3,
    3,
    70.0,
    'PENDING',
    'Python',
    'PostgreSQL,AWS,Docker',
    'Pythonの経験はあるが、データ分析の専門性は要確認。'
  ),
  -- クラウド移行プロジェクトのマッチング結果
  (
    4,
    5,
    95.0,
    'APPROVED',
    'AWS,Docker,Kubernetes',
    'Java,Spring Boot',
    'インフラエンジニアとして最適な人材。'
  ),
  (
    4,
    3,
    60.0,
    'REJECTED',
    'Docker',
    'AWS,Kubernetes,Java,Spring Boot',
    'Dockerの経験はあるが、AWS・Kubernetesの経験が不足。'
  ),
  -- Webアプリケーション開発のマッチング結果
  (
    5,
    2,
    85.0,
    'PENDING',
    'Vue.js,JavaScript',
    'Node.js,MySQL',
    'Vue.jsの経験があり、適している。'
  ),
  (
    5,
    7,
    70.0,
    'PENDING',
    'JavaScript',
    'Vue.js,Node.js,MySQL',
    'JavaScriptの経験はあるが、Vue.jsの経験が不足。'
  ),
  -- フロントエンド開発のマッチング結果
  (
    7,
    8,
    60.0,
    'PENDING',
    'React',
    'TypeScript,Node.js,MySQL',
    '新卒レベルだが、基本的なReactの知識はある。'
  ),
  (
    7,
    2,
    90.0,
    'APPROVED',
    'React,JavaScript',
    'TypeScript,Node.js,MySQL',
    'Reactの経験が豊富で最適な人材。'
  );

-- システムログテストデータ
INSERT INTO
  system_logs (log_level, message, details, user_id)
VALUES
  ('INFO', 'システム起動', 'アプリケーションが正常に起動しました。', 1),
  ('INFO', 'ユーザーログイン', 'ユーザーがログインしました。', 2),
  (
    'WARN',
    'データベース接続遅延',
    'データベース接続に時間がかかっています。',
    NULL
  ),
  (
    'ERROR',
    'マッチング処理エラー',
    'スキルマッチング処理中にエラーが発生しました。',
    1
  ),
  (
    'INFO',
    'データバックアップ完了',
    '日次データバックアップが完了しました。',
    NULL
  );
