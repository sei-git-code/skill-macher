# SES 営業支援ツール - 開発開始プロンプト

## 🎯 プロジェクト概要

**SES 営業支援ツール**は、SES 営業担当者が人員募集要項と SES 人員のスキルマッチ度を可視化する Web アプリケーションです。技術領域の関連性評価に基づいて、適切な人員配置を支援します。

## 📋 技術スタック

### フロントエンド

- **Spring Boot 3.x** + **Thymeleaf**
- **Bootstrap 5** (UI フレームワーク)
- **JavaScript** (クライアントサイド処理)

### バックエンド

- **Spring Boot 3.x** (Java 17)
- **Spring Security** (認証・認可)
- **Spring Data JPA** (データアクセス)
- **Maven** (依存関係管理)

### データベース

- **PostgreSQL** (Supabase)
- **SSL 接続** (セキュリティ)

### 認証システム

- **Supabase Auth** (フロントエンド認証)
- **Spring Security** (バックエンド認証)
- **JWT Token** (トークンベース認証)

### デプロイメント

- **OCI (Oracle Cloud Infrastructure)**
- **Docker** (コンテナ化)
- **GitHub Actions** (CI/CD)

## 🚀 開発開始手順

### 1. プロジェクト初期化

```bash
# Spring Boot プロジェクト作成
curl https://start.spring.io/starter.zip \
  -d dependencies=web,thymeleaf,security,data-jpa,postgresql,validation,actuator \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=3.2.0 \
  -d baseDir=ses-sales-support-tool \
  -d groupId=com.ses \
  -d artifactId=ses-sales-support-tool \
  -d name=ses-sales-support-tool \
  -d description="SES営業支援ツール" \
  -d packageName=com.ses.salessupport \
  -d packaging=jar \
  -d javaVersion=17 \
  -o ses-sales-support-tool.zip

unzip ses-sales-support-tool.zip
cd ses-sales-support-tool
```

### 2. 依存関係の追加

`pom.xml` に以下の依存関係を追加：

```xml
<!-- JWT処理 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- Supabase PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- テスト用 -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
```

### 3. 設定ファイルの作成

#### `src/main/resources/application.yml`

```yaml
spring:
  application:
    name: ses-sales-support-tool
    version: 1.0.0

  profiles:
    active: dev

  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/ses_sales_support_dev}?sslmode=require
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:dev_password}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      idle-timeout: 300000
      max-lifetime: 1200000
      connection-timeout: 30000
      leak-detection-threshold: 60000
      ssl: true
      ssl-mode: require

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        show_sql: false
    show-sql: false

  thymeleaf:
    cache: false
    prefix: classpath:/templates/
    suffix: .html
    encoding: UTF-8
    mode: HTML

# Supabase JWT設定
supabase:
  jwt:
    secret: ${SUPABASE_JWT_SECRET:your-jwt-secret}
    issuer: ${SUPABASE_URL:https://your-project.supabase.co}
    audience: authenticated
  url: ${SUPABASE_URL:https://your-project.supabase.co}
  anon-key: ${SUPABASE_ANON_KEY:your-anon-key}

server:
  port: ${PORT:8080}

logging:
  level:
    com.ses.salessupport: DEBUG
    org.springframework.security: DEBUG
```

### 4. パッケージ構造の作成

```
src/main/java/com/ses/salessupport/
├── SesSalesSupportToolApplication.java
├── config/                              # 設定クラス
│   ├── SupabaseSecurityConfig.java      # セキュリティ設定
│   ├── DatabaseConfig.java              # データベース設定
│   └── ThymeleafConfig.java             # Thymeleaf設定
├── controller/                          # コントローラー層
│   ├── LoginController.java             # ログインコントローラー
│   ├── DashboardController.java         # ダッシュボードコントローラー
│   ├── JobRequirementController.java     # 人員募集要項コントローラー
│   ├── EmployeeController.java          # SES人員コントローラー
│   ├── MatchingController.java          # マッチングコントローラー
│   └── ReportController.java            # レポートコントローラー
├── service/                             # サービス層
│   ├── AuthService.java                 # 認証サービス
│   ├── DashboardService.java            # ダッシュボードサービス
│   ├── JobRequirementService.java       # 人員募集要項サービス
│   ├── EmployeeService.java             # SES人員サービス
│   ├── SkillMatchingService.java        # スキルマッチングサービス
│   ├── ReportService.java               # レポートサービス
│   └── UserService.java                 # ユーザー管理サービス
├── repository/                          # リポジトリ層
│   ├── UserRepository.java              # ユーザーリポジトリ
│   ├── JobRequirementRepository.java    # 人員募集要項リポジトリ
│   ├── EmployeeRepository.java          # SES人員リポジトリ
│   ├── EmployeeSkillRepository.java     # 人員スキルリポジトリ
│   ├── MatchingResultRepository.java    # マッチング結果リポジトリ
│   ├── SkillMappingRepository.java      # スキルマッピングリポジトリ
│   └── SystemLogRepository.java         # システムログリポジトリ
├── entity/                              # エンティティ層
│   ├── User.java                        # ユーザーエンティティ
│   ├── UserProfile.java                 # ユーザープロフィールエンティティ
│   ├── JobRequirement.java              # 人員募集要項エンティティ
│   ├── Employee.java                    # SES人員エンティティ
│   ├── EmployeeSkill.java               # 人員スキルエンティティ
│   ├── MatchingResult.java              # マッチング結果エンティティ
│   ├── SkillMapping.java                # スキルマッピングエンティティ
│   └── SystemLog.java                   # システムログエンティティ
├── dto/                                 # データ転送オブジェクト
│   ├── LoginRequest.java                # ログインリクエスト
│   ├── JobRequirementDto.java           # 人員募集要項DTO
│   ├── EmployeeDto.java                 # SES人員DTO
│   ├── MatchingRequest.java             # マッチングリクエスト
│   ├── MatchingResultDto.java           # マッチング結果DTO
│   └── DashboardStats.java              # ダッシュボード統計DTO
├── exception/                           # 例外クラス
│   ├── GlobalExceptionHandler.java      # グローバル例外ハンドラー
│   ├── ResourceNotFoundException.java   # リソース未発見例外
│   ├── ValidationException.java         # バリデーション例外
│   └── AuthenticationException.java     # 認証例外
├── security/                            # セキュリティ関連
│   ├── SupabaseJwtUtil.java             # Supabase JWTユーティリティ
│   ├── SupabaseJwtAuthenticationFilter.java # JWT認証フィルター
│   └── CustomUserDetailsService.java    # カスタムユーザー詳細サービス
└── util/                                # ユーティリティ
    ├── DateUtil.java                    # 日付ユーティリティ
    ├── ValidationUtil.java              # バリデーションユーティリティ
    └── SecurityUtil.java                # セキュリティユーティリティ
```

### 5. コアクラスの実装

#### `config/SupabaseSecurityConfig.java`

```java
package com.ses.salessupport.config;

import com.ses.salessupport.security.SupabaseJwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SupabaseSecurityConfig {

    @Value("${supabase.jwt.secret}")
    private String supabaseJwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(supabaseJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SupabaseJwtAuthenticationFilter supabaseJwtAuthenticationFilter() {
        return new SupabaseJwtAuthenticationFilter(supabaseJwtSecret);
    }
}
```

#### `security/SupabaseJwtAuthenticationFilter.java`

```java
package com.ses.salessupport.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SupabaseJwtAuthenticationFilter extends OncePerRequestFilter {

    private final String jwtSecret;

    public SupabaseJwtAuthenticationFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (token != null && validateSupabaseToken(token)) {
            String userId = getUserIdFromToken(token);
            String role = getRoleFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateSupabaseToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    private String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();
        return claims.get("role", String.class);
    }
}
```

### 6. データベース初期化

#### Supabase PostgreSQL 接続設定

1. Supabase プロジェクトを作成
2. データベース接続情報を取得
3. 環境変数を設定：

```bash
export DATABASE_URL="jdbc:postgresql://your-project.supabase.co:5432/postgres?sslmode=require"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="your-password"
export SUPABASE_JWT_SECRET="your-jwt-secret"
export SUPABASE_URL="https://your-project.supabase.co"
export SUPABASE_ANON_KEY="your-anon-key"
```

### 7. 開発環境の起動

```bash
# アプリケーション起動
./mvnw spring-boot:run

# または
./mvnw clean package
java -jar target/ses-sales-support-tool-1.0.0.jar
```

### 8. テスト実行

```bash
# ユニットテスト実行
./mvnw test

# 統合テスト実行
./mvnw test -Dtest=*IntegrationTest
```

## 📚 参考ドキュメント

開発に必要な詳細な設計ドキュメントは以下の通りです：

- **要件定義書**: `営業支援ツール要件定義.md`
- **基本設計書**:
  - `システム概要設計書.md`
  - `データベース設計書.md`
  - `API設計書.md`
  - `アプリケーション設計書.md`
  - `クラス設計書.md`
  - `インフラ設計書.md`
- **詳細設計書**:
  - `画面設計書.md`
  - `スキルマッチングロジック設計書.md`
  - `セキュリティ設計書.md`
  - `設定設計書.md`
  - `テスト設計書.md`
- **開発・運用**:
  - `開発環境構築手順書.md`
  - `デプロイ手順書.md`
  - `運用マニュアル.md`

## ⚠️ 重要な注意事項

### Supabase + Spring Boot 統合について

1. **認証フロー**:

   - フロントエンド: Supabase Auth でログイン
   - フロントエンド: Supabase JWT を Spring Boot に送信
   - Spring Boot: Supabase JWT を検証して認証

2. **RLS (Row Level Security) の制限**:

   - Supabase RLS は Spring Boot から直接動作しません
   - アプリケーションレベルでの認可制御を実装してください

3. **SSL 接続**:
   - Supabase PostgreSQL への接続は SSL 必須です
   - `sslmode=require` を必ず設定してください

### スキル移行成功率について

- 設計ドキュメント内の数値は **推定値** です
- 実際の成功率はプロジェクトの複雑さ、チームの経験、学習期間等により変動します
- 根拠と課題を詳細に記載しているので、実装時に参考にしてください

## 🎯 開発の優先順位

1. **認証システムの実装** (Supabase Auth + Spring Security)
2. **データベース接続とエンティティ作成**
3. **基本 CRUD 操作の実装**
4. **スキルマッチングロジックの実装**
5. **フロントエンド画面の実装**
6. **テストの実装**
7. **デプロイメント設定**

## 🚀 次のステップ

1. このプロンプトに従ってプロジェクトを初期化
2. 設計ドキュメントを参照しながら実装
3. 段階的にテストを追加
4. CI/CD パイプラインの構築
5. OCI へのデプロイ

開発を開始する準備が整いました！設計ドキュメントを参照しながら、段階的に実装を進めてください。

## 📊 開発進行状況

### ✅ 完了済み項目

#### 1. プロジェクト基盤構築

- [x] Spring Boot プロジェクト初期化
- [x] Maven 依存関係設定（JWT、PostgreSQL、Spring Security 等）
- [x] アプリケーション設定ファイル（application.yml）
- [x] パッケージ構造の作成

#### 2. 設計ドキュメント

- [x] 要件定義書
- [x] 基本設計書（システム概要、データベース、API、アプリケーション、クラス、インフラ）
- [x] 詳細設計書（画面、スキルマッチングロジック、セキュリティ、設定、テスト）
- [x] 開発・運用ドキュメント（環境構築、デプロイ、運用マニュアル）

#### 3. バックエンド実装

- [x] **エンティティ層** (8 ファイル)
  - User.java, UserProfile.java
  - JobRequirement.java, Employee.java, EmployeeSkill.java
  - MatchingResult.java, SkillMapping.java, SystemLog.java
- [x] **リポジトリ層** (7 ファイル)
  - UserRepository.java, JobRequirementRepository.java, EmployeeRepository.java
  - EmployeeSkillRepository.java, MatchingResultRepository.java
  - SkillMappingRepository.java, SystemLogRepository.java
- [x] **サービス層** (5 ファイル)
  - AuthService.java, DashboardService.java, EmployeeService.java
  - JobRequirementService.java, SkillMatchingService.java
- [x] **コントローラー層** (5 ファイル)
  - LoginController.java, DashboardController.java, JobRequirementController.java
  - EmployeeController.java, MatchingController.java
- [x] **設定クラス** (3 ファイル)
  - SupabaseSecurityConfig.java, DatabaseConfig.java, ThymeleafConfig.java
- [x] **セキュリティ** (1 ファイル)
  - SupabaseJwtAuthenticationFilter.java

#### 4. フロントエンド実装

- [x] **Thymeleaf テンプレート** (3 ファイル)
  - layout.html (レイアウトテンプレート)
  - dashboard.html (ダッシュボード画面)
  - auth/login.html (ログイン画面)
- [x] **静的リソース**
  - CSS (style.css)
  - JavaScript (app.js)

#### 5. フロントエンド画面実装

- [x] **人員募集要項管理画面** (4 ファイル)
  - list.html (一覧画面)
  - detail.html (詳細画面)
  - create.html (作成画面)
  - edit.html (編集画面)
- [x] **SES 人員管理画面** (4 ファイル)
  - list.html (一覧画面)
  - detail.html (詳細画面)
  - create.html (作成画面)
  - edit.html (編集画面)
- [x] **スキルマッチング画面** (1 ファイル)
  - list.html (マッチング実行・結果表示画面)
- [x] **エラーページ** (1 ファイル)
  - 404.html (404 エラーページ)

#### 6. データベース設定

- [x] **Supabase PostgreSQL 接続設定**
  - application.yml の更新
  - データベース設定クラスの更新
- [x] **データベース初期化スクリプト**
  - init.sql (テーブル作成スクリプト)
  - test-data.sql (テストデータ投入スクリプト)
- [x] **データベース接続テスト機能**
  - DatabaseConfig.java に接続テスト機能追加
- [x] **データベース設定手順書**
  - DATABASE_SETUP.md (詳細な設定手順)

#### 7. DTO・例外処理

- [x] **DTO クラス群** (8 ファイル)
  - LoginRequest.java, RegisterRequest.java
  - JobRequirementDto.java, EmployeeDto.java, EmployeeSkillDto.java
  - MatchingRequest.java, MatchingResultDto.java
  - DashboardStatsDto.java, ApiResponse.java
- [x] **例外クラス群** (6 ファイル)
  - ResourceNotFoundException.java, ValidationException.java
  - AuthenticationException.java, AuthorizationException.java
  - BusinessLogicException.java, GlobalExceptionHandler.java

#### 8. スキルマッチング機能

- [x] **スキルマッチングサービスの改善**
  - SkillMatchingService.java の修正・最適化
  - スキルマッチングアルゴリズムの実装
  - 経験年数・予算マッチング機能
- [x] **マッチング結果管理**
  - MatchingResultService.java の実装
  - マッチング結果の承認・却下機能
- [x] **マッチング画面の実装**
  - matching/results.html (マッチング結果一覧)
  - matching/detail.html (マッチング結果詳細)
- [x] **MatchingController の改善**
  - 実際のサービス連携
  - API エンドポイントの実装

#### 9. ビルド・コンパイル

- [x] Maven ビルド成功（46 個の Java ファイル）
- [x] コンパイルエラーなし（警告のみ：非推奨 API 使用）

### 🚧 進行中・未完了項目

#### 1. テスト実装

- [ ] ユニットテスト
- [ ] 統合テスト
- [ ] E2E テスト

#### 2. デプロイメント

- [ ] Docker 設定
- [ ] GitHub Actions CI/CD
- [ ] OCI デプロイ設定

### 📈 進捗率

- **全体進捗**: 約 90%
- **バックエンド**: 約 95% (コア機能実装完了)
- **フロントエンド**: 約 90% (主要画面実装完了)
- **データベース**: 約 90% (設定・スクリプト完了)
- **DTO・例外処理**: 約 100% (実装完了)
- **スキルマッチング**: 約 95% (動作確認済み)
- **インフラ・デプロイ**: 約 10% (設計のみ)

### 🎯 次の優先タスク

1. **ユニットテストと統合テストの実装**
2. **Docker 設定とデプロイメント準備**
3. **本番環境での動作確認**
4. **パフォーマンステストと最適化**

### 📝 実装済みファイル一覧

**Java ファイル (46 個)**:

- エンティティ: 8 個
- リポジトリ: 7 個
- サービス: 6 個
- コントローラー: 5 個
- 設定: 3 個
- セキュリティ: 1 個
- DTO: 8 個
- 例外: 6 個
- メインアプリケーション: 1 個
- スキルマッチング: 1 個

**HTML ファイル (14 個)**:

- レイアウト: layout.html
- ダッシュボード: dashboard.html
- ログイン: auth/login.html
- 人員募集要項: job-requirements/list.html, detail.html, create.html, edit.html
- SES 人員: employees/list.html, detail.html, create.html, edit.html
- マッチング: matching/list.html, results.html, detail.html
- エラー: error/404.html

**データベースファイル (3 個)**:

- 初期化スクリプト: db/init.sql
- テストデータ: db/test-data.sql
- 設定手順書: DATABASE_SETUP.md

**設定ファイル**:

- application.yml, pom.xml

### ⚠️ 注意事項

- JWT 認証フィルターで非推奨 API 使用の警告あり（動作には問題なし）
- Supabase 接続設定は環境変数での設定が必要
- データベース接続テスト機能が実装済み（起動時に自動実行）
- テストデータは 8 名の従業員と 7 件の案件要件を含む
