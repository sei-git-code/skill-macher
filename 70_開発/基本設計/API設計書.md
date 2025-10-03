# API 設計書

## 1. API 概要

### 1.1 API 仕様

- **ベース URL**: `https://your-app.oci.com/api/v1/`
- **認証方式**: Bearer Token (JWT)
- **データ形式**: JSON
- **HTTP メソッド**: GET, POST, PUT, DELETE, PATCH
- **フレームワーク**: Spring Boot REST API

### 1.2 認証・認可

- **認証**: Spring Security JWT Token
- **認可**: Spring Security のロールベース認可
- **ヘッダー**: `Authorization: Bearer <jwt_token>`

## 2. 共通仕様

### 2.1 共通ヘッダー

```http
Authorization: Bearer <jwt_token>
Content-Type: application/json
Accept: application/json
```

### 2.2 共通レスポンス形式

```json
{
  "data": <response_data>,
  "error": null,
  "status": 200,
  "statusText": "OK"
}
```

### 2.3 エラーレスポンス

```json
{
  "error": {
    "message": "エラーメッセージ",
    "code": "ERROR_CODE",
    "details": "詳細情報"
  },
  "status": 400,
  "statusText": "Bad Request"
}
```

## 3. ユーザー管理 API

### 3.1 ユーザー情報取得

```http
GET /api/v1/users/{userId}
```

**レスポンス例:**

```json
{
  "data": [
    {
      "id": "uuid",
      "email": "user@example.com",
      "name": "田中太郎",
      "role": "user",
      "created_at": "2024-01-01T00:00:00Z",
      "updated_at": "2024-01-01T00:00:00Z",
      "is_active": true
    }
  ]
}
```

### 3.2 ユーザープロフィール更新

```http
PATCH /api/v1/users/{userId}/profile
Content-Type: application/json

{
  "companyName": "株式会社サンプル",
  "department": "営業部",
  "phone": "03-1234-5678"
}
```

## 4. 人員募集要項 API

### 4.1 人員募集要項一覧取得

```http
GET /api/v1/job-requirements?status=active&sort=createdAt,desc
```

**クエリパラメータ:**

- `status`: ステータス (active, closed, cancelled)
- `sort`: ソート順 (createdAt,desc / updatedAt,desc)
- `page`: ページ番号 (デフォルト: 0)
- `size`: 取得件数 (デフォルト: 20)

**レスポンス例:**

```json
{
  "data": [
    {
      "id": "uuid",
      "project_name": "ECサイト開発",
      "client_name": "株式会社クライアント",
      "description": "ECサイトの新規開発プロジェクト",
      "required_skills": ["React", "Node.js", "PostgreSQL"],
      "preferred_skills": ["TypeScript", "AWS"],
      "experience_years": 3,
      "budget_range": "500-800万円",
      "start_date": "2024-02-01",
      "end_date": "2024-08-31",
      "location": "東京都",
      "work_style": "hybrid",
      "status": "active",
      "created_by": "uuid",
      "created_at": "2024-01-01T00:00:00Z"
    }
  ]
}
```

### 4.2 人員募集要項作成

```http
POST /api/v1/job-requirements
Content-Type: application/json

{
  "projectName": "ECサイト開発",
  "clientName": "株式会社クライアント",
  "description": "ECサイトの新規開発プロジェクト",
  "requiredSkills": ["React", "Node.js", "PostgreSQL"],
  "preferredSkills": ["TypeScript", "AWS"],
  "experienceYears": 3,
  "budgetRange": "500-800万円",
  "startDate": "2024-02-01",
  "endDate": "2024-08-31",
  "location": "東京都",
  "workStyle": "hybrid",
  "additionalRequirements": "リモートワーク可"
}
```

### 4.3 人員募集要項更新

```http
PATCH /api/v1/job-requirements/{jobId}
Content-Type: application/json

{
  "status": "closed"
}
```

### 4.4 人員募集要項削除

```http
DELETE /api/v1/job-requirements/{jobId}
```

## 5. SES 人員管理 API

### 5.1 SES 人員一覧取得

```http
GET /api/v1/employees?availability=available&sort=createdAt,desc
```

**クエリパラメータ:**

- `availability`: 稼働状況 (available, busy, unavailable)
- `sort`: ソート順
- `page`: ページ番号 (デフォルト: 0)
- `size`: 取得件数 (デフォルト: 20)

**レスポンス例:**

```json
{
  "data": [
    {
      "id": "uuid",
      "employee_id": "EMP001",
      "name": "山田花子",
      "email": "yamada@example.com",
      "phone": "090-1234-5678",
      "availability": "available",
      "hourly_rate": 5000,
      "created_at": "2024-01-01T00:00:00Z"
    }
  ]
}
```

### 5.2 SES 人員詳細取得（スキル情報含む）

```http
GET /api/v1/employees/{employeeId}?includeSkills=true
```

**レスポンス例:**

```json
{
  "data": [
    {
      "id": "uuid",
      "employee_id": "EMP001",
      "name": "山田花子",
      "email": "yamada@example.com",
      "availability": "available",
      "employee_skills": [
        {
          "id": "uuid",
          "technology": "React",
          "proficiency": "advanced",
          "experience_years": 3.5,
          "last_used_date": "2024-01-15"
        },
        {
          "id": "uuid",
          "technology": "Node.js",
          "proficiency": "intermediate",
          "experience_years": 2.0,
          "last_used_date": "2024-01-10"
        }
      ]
    }
  ]
}
```

### 5.3 SES 人員作成

```http
POST /api/v1/employees
Content-Type: application/json

{
  "employeeId": "EMP002",
  "name": "佐藤次郎",
  "email": "sato@example.com",
  "phone": "090-9876-5432",
  "availability": "available",
  "hourlyRate": 4500
}
```

### 5.4 スキル情報追加

```http
POST /api/v1/employees/{employeeId}/skills
Content-Type: application/json

{
  "technology": "Vue.js",
  "proficiency": "intermediate",
  "experienceYears": 1.5,
  "lastUsedDate": "2024-01-20"
}
```

## 6. スキルマッチング API

### 6.1 スキルマッチング実行

```http
POST /api/v1/matching/execute
Content-Type: application/json

{
  "jobRequirementId": "uuid",
  "employeeIds": ["uuid1", "uuid2", "uuid3"]
}
```

**レスポンス例:**

```json
{
  "data": [
    {
      "employee_id": "uuid1",
      "employee_name": "山田花子",
      "overall_score": 85.5,
      "skill_match_score": 80.0,
      "experience_match_score": 90.0,
      "proficiency_match_score": 85.0,
      "matched_skills": ["React", "Node.js"],
      "missing_skills": ["TypeScript"],
      "recommendation_level": "high",
      "recommendation_comment": "React経験が豊富で、Node.jsの経験もあります。TypeScriptは短期学習で対応可能です。"
    }
  ]
}
```

### 6.2 マッチング結果取得

```http
GET /api/v1/matching/results?jobRequirementId={jobId}&sort=overallScore,desc
```

### 6.3 スキルマッピング取得

```http
GET /api/v1/skill-mappings?fromTechnology=jQuery
```

**レスポンス例:**

```json
{
  "data": [
    {
      "id": "uuid",
      "from_technology": "jQuery",
      "to_technology": "React",
      "compatibility_score": 75.0,
      "learning_cost": "medium",
      "learning_duration": "1-2 months",
      "description": "JavaScript基礎知識共通、パラダイム違いで学習コスト発生"
    }
  ]
}
```

## 7. レポート API

### 7.1 マッチング統計取得

```http
GET /api/v1/reports/matching-statistics
```

**レスポンス例:**

```json
{
  "data": {
    "total_job_requirements": 150,
    "total_employees": 500,
    "total_matching_results": 1250,
    "average_match_score": 72.5,
    "high_recommendation_count": 180,
    "medium_recommendation_count": 650,
    "low_recommendation_count": 420
  }
}
```

### 7.2 スキル需要分析

```http
GET /api/v1/reports/skill-demand-analysis
```

**レスポンス例:**

```json
{
  "data": [
    {
      "technology": "React",
      "demand_count": 45,
      "supply_count": 120,
      "demand_supply_ratio": 0.375
    },
    {
      "technology": "Node.js",
      "demand_count": 38,
      "supply_count": 85,
      "demand_supply_ratio": 0.447
    }
  ]
}
```

## 8. システム管理 API

### 8.1 システムログ取得

```http
GET /api/v1/system/logs?sort=createdAt,desc&size=100
```

### 8.2 スキルカテゴリ取得

```http
GET /api/v1/skill-categories?isActive=true&sort=sortOrder,asc
```

## 9. エラーハンドリング

### 9.1 共通エラーコード

- `400`: Bad Request - リクエスト形式エラー
- `401`: Unauthorized - 認証エラー
- `403`: Forbidden - 認可エラー
- `404`: Not Found - リソースが見つからない
- `409`: Conflict - データ競合エラー
- `500`: Internal Server Error - サーバー内部エラー

### 9.2 バリデーションエラー

```json
{
  "error": {
    "message": "バリデーションエラー",
    "code": "VALIDATION_ERROR",
    "details": [
      {
        "field": "experience_years",
        "message": "経験年数は0以上50以下である必要があります"
      }
    ]
  }
}
```

## 10. レート制限

### 10.1 制限値

- **認証済みユーザー**: 1000 リクエスト/時間
- **未認証ユーザー**: 100 リクエスト/時間

### 10.2 レート制限ヘッダー

```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1640995200
```

### 10.3 Spring Boot での実装

```java
@RestController
@RateLimiter(name = "api")
public class JobRequirementController {

    @GetMapping("/api/v1/job-requirements")
    @RateLimiter(name = "job-requirements")
    public ResponseEntity<List<JobRequirement>> getJobRequirements() {
        // 実装
    }
}
```

## 11. バージョニング

### 11.1 API バージョン

- **現在のバージョン**: v1
- **バージョン指定**: URL パスに含める
- **後方互換性**: 6 ヶ月間維持

### 11.2 非推奨 API

```http
GET /api/v1/deprecated-endpoint
X-Deprecated: true
X-Sunset-Date: 2024-12-31
```

### 11.3 Spring Boot での実装

```java
@RestController
@RequestMapping("/api/v1")
public class ApiV1Controller {

    @GetMapping("/deprecated-endpoint")
    @Deprecated(since = "2024-01-01", forRemoval = true)
    public ResponseEntity<String> deprecatedEndpoint() {
        return ResponseEntity.ok()
            .header("X-Deprecated", "true")
            .header("X-Sunset-Date", "2024-12-31")
            .body("This endpoint is deprecated");
    }
}
```
