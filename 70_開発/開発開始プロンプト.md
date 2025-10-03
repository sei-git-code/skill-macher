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
