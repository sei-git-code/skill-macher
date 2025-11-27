# データベース接続設定手順書

## 概要

この手順書では、Supabase PostgreSQL データベースとの接続設定とテストデータ投入について説明します。

## 前提条件

- Supabase アカウント
- Java 17 以上
- Maven 3.6 以上

## 1. Supabase プロジェクトの設定

### 1.1 プロジェクト作成

1. [Supabase](https://supabase.com)にアクセス
2. 「New Project」をクリック
3. プロジェクト名とデータベースパスワードを設定
4. リージョンを選択（推奨: Asia Northeast (Tokyo)）

### 1.2 接続情報の取得

プロジェクトダッシュボードの「Settings」→「Database」から以下を取得：

- Database URL
- Database Password
- JWT Secret
- Anon Key

## 2. 環境変数の設定

### 2.1 環境変数ファイルの作成

プロジェクトルートに`.env`ファイルを作成：

```bash
# Supabase Database Configuration
DATABASE_URL=jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres?sslmode=require
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-database-password

# Supabase JWT Configuration
SUPABASE_URL=https://your-project-ref.supabase.co
SUPABASE_JWT_SECRET=your-jwt-secret
SUPABASE_ANON_KEY=your-anon-key

# Application Configuration
PORT=8080
```

### 2.2 環境変数の設定方法

#### macOS/Linux:

```bash
export DATABASE_URL="jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres?sslmode=require"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="your-database-password"
export SUPABASE_URL="https://your-project-ref.supabase.co"
export SUPABASE_JWT_SECRET="your-jwt-secret"
export SUPABASE_ANON_KEY="your-anon-key"
```

#### Windows:

```cmd
set DATABASE_URL=jdbc:postgresql://db.your-project-ref.supabase.co:5432/postgres?sslmode=require
set DATABASE_USERNAME=postgres
set DATABASE_PASSWORD=your-database-password
set SUPABASE_URL=https://your-project-ref.supabase.co
set SUPABASE_JWT_SECRET=your-jwt-secret
set SUPABASE_ANON_KEY=your-anon-key
```

## 3. データベース初期化

### 3.1 テーブル作成

Supabase の SQL Editor で以下のスクリプトを実行：

```sql
-- src/main/resources/db/init.sql の内容を実行
```

### 3.2 テストデータ投入

テーブル作成後、以下のスクリプトを実行：

```sql
-- src/main/resources/db/test-data.sql の内容を実行
```

## 4. アプリケーションの起動とテスト

### 4.1 アプリケーション起動

```bash
cd ses-sales-support-tool
./mvnw spring-boot:run
```

### 4.2 接続確認

起動ログで以下のメッセージを確認：

```
データベース接続テスト: Database connection successful
データベース内のテーブル数: 8
```

### 4.3 ブラウザでの確認

1. http://localhost:8080 にアクセス
2. ログイン画面が表示されることを確認
3. テストユーザーでログイン：
   - Email: admin@ses.com
   - Password: password

## 5. トラブルシューティング

### 5.1 接続エラーの場合

- 環境変数が正しく設定されているか確認
- Supabase のデータベースが起動しているか確認
- ファイアウォール設定を確認

### 5.2 SSL 証明書エラーの場合

- `sslmode=require`が設定されているか確認
- Supabase の SSL 証明書が有効か確認

### 5.3 認証エラーの場合

- JWT Secret と Anon Key が正しいか確認
- Supabase の認証設定を確認

## 6. 本番環境での注意事項

### 6.1 セキュリティ

- 本番環境では強力なパスワードを使用
- 環境変数は安全に管理
- データベースアクセスを制限

### 6.2 パフォーマンス

- 接続プール設定を調整
- インデックスを適切に設定
- 定期的なバックアップを実施

## 7. 参考情報

- [Supabase Documentation](https://supabase.com/docs)
- [Spring Boot Database Configuration](https://spring.io/guides/gs/accessing-data-jpa/)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)

