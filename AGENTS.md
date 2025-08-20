了解しました 👍
先ほどのガイドラインを **Markdown ベースのドキュメントテンプレート** として整理しました。
このまま `README.md` や `CONTRIBUTING.md` に貼り付けて利用できます。

---

```markdown
# リポジトリ ガイドライン

## 📂 プロジェクト構成 & モジュール整理
```

src/main/java/com/example/attendance
├── controller/ # Web コントローラ (例: PersonController)
├── repository/ # Spring Data JPA リポジトリ (例: PersonRepository)
├── model/ # JPA エンティティ (例: Person, Student, Attendance)
└── domain/ # JPA エンティティ

src/main/resources
├── application.yml # データソース & JPA 設定
├── templates/ # Thymeleaf ビュー (例: people.html)
└── static/ # CSS / JS アセット

pom.xml # Maven ビルド & 依存関係管理

```

---

## 🛠️ ビルド・テスト・開発コマンド
- **ビルド**: `mvn clean package`
  → `target/attendance-0.0.1-SNAPSHOT.jar` を生成
- **実行（開発）**: `mvn spring-boot:run`
  → `http://localhost:8080` で起動
- **実行（jar）**: `java -jar target/attendance-0.0.1-SNAPSHOT.jar`
- **テスト**: `mvn test`
  → ユニットテストを実行

---

## ✍️ コーディング規約 & 名前付け
- **Javaバージョン**: 17
- **インデント**: 4スペース
- **文字コード**: UTF-8
- **命名規則**:
  - パッケージ: 小文字 (`com.example.attendance.controller`)
  - クラス / Enum: `PascalCase` (例: `AttendanceStatus`)
  - メソッド / フィールド: `camelCase`
- **責務分割**:
  - エンティティ: `model` / `domain`
  - リポジトリ: `repository`
  - コントローラ: `controller`
- **テンプレート**: Thymeleafを使用。セマンティックHTMLを優先し、小規模かつ焦点の定まったビューを推奨

---

## 🧪 テストガイドライン
- **利用フレームワーク**: JUnit 5
  （統合テストが必要な場合は `spring-boot-starter-test` を追加）
- **配置場所**: `src/test/java/com/example/attendance/...`
- **命名規則**: `*Test.java` （1クラス = 1テスト対象）
- **実行例**:
  - 全体: `mvn test`
  - 特定クラス: `mvn -Dtest=ClassNameTest test`
- **カバレッジ目標**: ドメイン、リポジトリ、コントローラを優先

---

## 🔀 コミット & プルリクエスト規約
- **コミットメッセージ**: Conventional Commits 準拠
  - 例:
    - `feat: add Person list view`
    - `fix: correct repository query`
- **PR**: 小規模で焦点の定まった変更を推奨
  - 説明文必須
  - 関連Issueをリンク
  - UI変更（`templates/*`）はスクリーンショット添付
- **事前チェック**: ローカルでビルド & 起動確認後にレビュー依頼

---

## 🔒 セキュリティ & 設定の注意点
- DB設定は環境変数で管理:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
- デフォルトJPA DDL: `update`
  → 本番環境前に必ず見直し
- シークレットはコミット禁止
  → `.env` や CIシークレットを利用
- ローカル開発時は **読み取り専用ユーザ** の利用も検討

---

✅ このテンプレートをベースに、`README.md` に入れてもいいですし、
**開発フロー系は `CONTRIBUTING.md`** に分けるのもおすすめです。

---

こちら、`README.md` 向けと `CONTRIBUTING.md` 向け、両方の形に分けた方がよいですか？
```
