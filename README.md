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
| GET    | `/api/students/{id}/courses/{courseId}` | Lấy 1 môn học của SV (404 nếu môn không thuộc SV) |
| PUT    | `/api/students/{id}/courses/{courseId}` | Sửa môn học của SV |
| DELETE | `/api/students/{id}/courses/{courseId}` | Xóa môn học của SV |
| GET    | `/api/students/{id}/summary` | Điểm trung bình + xếp loại |
| GET    | `/api/courses/{id}` | Lấy 1 môn học |
| PUT    | `/api/courses/{id}` | Sửa môn học |
| DELETE | `/api/courses/{id}` | Xóa môn học |

Xếp loại theo điểm trung bình: `>= 9` Xuất sắc, `>= 8` Giỏi, `>= 6.5` Khá, `>= 5` Trung bình, còn lại Yếu.

Validate dữ liệu (cả API và client): tên SV/môn học bắt buộc, tối đa 255 ký tự; địa chỉ tối đa 255 ký tự;
SĐT 9-11 chữ số; ngày sinh phải trong quá khứ; điểm 0-10; khi tìm môn học thì điểm từ <= điểm đến.
Dữ liệu sai trả về `400` kèm thông báo theo từng trường, ví dụ `{"phone": "Số điện thoại gồm 9-11 chữ số"}`.

## Chạy bằng Docker (khuyên dùng)

Cần Docker Desktop và MySQL cài sẵn trên máy (Laragon, `localhost:3306`, user `root`, không mật khẩu).
Container `api` kết nối tới MySQL của máy qua `host.docker.internal`.

1. Bật MySQL trong Laragon, chạy `database.sql` để tạo DB `APIDB` + dữ liệu mẫu (bỏ qua cũng được – API tự tạo DB/bảng).
2. Chạy:

```
docker compose up -d --build
```

- Client: http://localhost:8081
- API: http://localhost:8080/api/students

Dừng: `docker compose down`.

## Chạy không dùng Docker

1. Cài JDK 25 và MySQL, chạy `database.sql` (hoặc bỏ qua – API tự tạo DB/bảng nhờ `createDatabaseIfNotExist` + `ddl-auto=update`).
2. Sửa user/password MySQL trong `api/src/main/resources/application.properties`.
3. Chạy `ApiApplication` (project `api`) trước, sau đó chạy `ClientApplication` (project `client`).
4. Mở http://localhost:8081

Chạy bằng Maven: `mvn spring-boot:run` trong từng thư mục `api` và `client`.
