# Quản lý Sinh viên – Môn học (Spring Web API + Spring MVC Client)

Gồm 2 project Spring Boot độc lập:

| Project  | Port | Vai trò |
|----------|------|---------|
| `api`    | 8080 | Web API (Spring Web + Spring Data JPA + MySQL) |
| `client` | 8081 | Giao diện Spring MVC + Thymeleaf, gọi Web API bằng `RestTemplate` |

## Cấu trúc

```
database.sql                         Script tạo DB APIDB + dữ liệu mẫu
api/src/main/java/com/example/api
├── entity/        Student, Course (JPA entity)
├── repository/    StudentRepository, CourseRepository (Spring Data JPA)
├── service/       StudentService, CourseService (nghiệp vụ, tính ĐTB + xếp loại)
├── controller/    StudentApiController, CourseApiController (@RestController)
└── exception/     NotFoundException, GlobalExceptionHandler
client/src/main/java/com/example/client
├── model/         Student, Course, StudentSummary
├── service/       StudentApiService, CourseApiService (gọi API bằng RestTemplate)
├── controller/    StudentController, CourseController (@Controller)
└── config/        RestTemplateConfig
client/src/main/resources/templates
├── students/      list.html, form.html, detail.html
└── courses/       form.html
```

## Danh sách Web API

| Method | URL | Chức năng |
|--------|-----|-----------|
| GET    | `/api/students?name=` | Liệt kê / tìm sinh viên theo tên |
| GET    | `/api/students/{id}` | Lấy 1 sinh viên |
| POST   | `/api/students` | Thêm sinh viên |
| PUT    | `/api/students/{id}` | Sửa sinh viên |
| DELETE | `/api/students/{id}` | Xóa sinh viên (xóa luôn môn học) |
| GET    | `/api/students/{id}/courses?name=&minScore=&maxScore=` | Môn học của SV, tìm theo tên / khoảng điểm |
| POST   | `/api/students/{id}/courses` | Thêm môn học cho SV |
| GET    | `/api/students/{id}/summary` | Điểm trung bình + xếp loại |
| GET    | `/api/courses/{id}` | Lấy 1 môn học |
| PUT    | `/api/courses/{id}` | Sửa môn học |
| DELETE | `/api/courses/{id}` | Xóa môn học |

Xếp loại theo điểm trung bình: `>= 9` Xuất sắc, `>= 8` Giỏi, `>= 6.5` Khá, `>= 5` Trung bình, còn lại Yếu.

## Chạy bằng Docker (khuyên dùng)

Chỉ cần Docker Desktop, không cần cài Java/Maven/MySQL:

```
docker compose up -d --build
```

- Client: http://localhost:8081
- API: http://localhost:8080/api/students
- MySQL: `localhost:3307`, user `root` / `123456` (dữ liệu mẫu nạp từ `database.sql` ở lần chạy đầu)

Dừng: `docker compose down` (thêm `-v` để xóa luôn dữ liệu DB).

## Chạy không dùng Docker

1. Cài MySQL, chạy `database.sql` (hoặc bỏ qua – API tự tạo DB/bảng nhờ `createDatabaseIfNotExist` + `ddl-auto=update`).
2. Sửa user/password MySQL trong `api/src/main/resources/application.properties`.
3. Chạy `ApiApplication` (project `api`) trước, sau đó chạy `ClientApplication` (project `client`).
4. Mở http://localhost:8081

Chạy bằng Maven: `mvn spring-boot:run` trong từng thư mục `api` và `client`.
