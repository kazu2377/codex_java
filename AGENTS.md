Repository Guidelines

プロジェクト構成とモジュール
- `src/main/java/com/example/attendance`: アプリケーションコード
  - `controller/`: Webコントローラ（例: `PersonController`）
  - `service/`: ビジネスロジック（例: `AttendanceService`）
  - `repository/`: Spring Data JPA リポジトリ
  - `domain/`・`model/`: JPAエンティティ／列挙型
  - `AttendanceApplication.java`: Spring Boot エントリポイント
- `src/main/resources`: 設定とビュー
  - `application.yml`: データソース、JPA、サーバーポート
  - `templates/`: Thymeleafビュー（例: `people.html`）
  - `static/`: CSS/JS アセット
- `src/test/java/...`: テスト配置場所
- `pom.xml`: Maven ビルドと依存関係
- ルートの `index.html`・`assets/`: UIプロトタイプ（任意）

ビルド・テスト・開発コマンド
- ビルド: `mvn clean package` — `target/` にJARを生成。
- 開発起動: `mvn spring-boot:run` — `http://localhost:8080` で起動。
- JAR起動: `java -jar target/attendance-0.0.1-SNAPSHOT.jar`。
- テスト: `mvn test`／単一クラスは `mvn -Dtest=ClassNameTest test`。

コーディングスタイルと命名
- Java 17・UTF-8・インデント4スペース。コントローラは薄く、ロジックは `service`、データアクセスは `repository` に配置。
- パッケージ: 小文字／クラス・Enum: PascalCase（例: `AttendanceStatus`）／メソッド・フィールド: camelCase。
- Thymeleaf: セマンティックHTMLを心がけ、小さく焦点の定まったテンプレート。

テストガイドライン
- フレームワーク: JUnit 5（`spring-boot-starter-test`）。
- 配置: `src/test/java/com/example/attendance/...`。
- 命名: `*Test.java`（1クラス=1テストクラス）。
- 優先カバレッジ: ドメインルール、リポジトリ、コントローラのエンドポイント。

コミットとプルリクエスト
- Conventional Commits（例: `feat: add Person list view`, `fix: correct repository query`）。
- PRは小さく焦点を絞り、説明、関連Issueリンク、UI変更（`templates/*`, `static/*`）はスクリーンショットを添付。
- レビュー前にビルドし、ローカル起動で動作確認。

セキュリティと設定のヒント
- 環境変数でDB設定: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`。
- シークレットはコミット禁止。ローカルは `.env`、CIはシークレットを使用。
- `spring.jpa.hibernate.ddl-auto=update` は開発向け。本番前に見直し、マイグレーションの利用を検討。
