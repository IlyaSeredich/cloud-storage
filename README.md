# Cloud Storage Application

Веб-приложение для хранения и управления файлами в облаке с использованием Spring Boot, MinIO и PostgreSQL.

## 🚀 Технологии

- **Backend**: Spring Boot 3.4.4, Java 21
- **База данных**: PostgreSQL с Liquibase миграциями
- **Хранилище файлов**: MinIO (S3-совместимое)
- **Аутентификация**: Spring Security с сессиями в Redis
- **API документация**: SpringDoc OpenAPI (Swagger)
- **Тестирование**: JUnit 5, TestContainers

## 📁 Функциональность

### Пользователи
- Регистрация и авторизация пользователей
- Получение деталей аутентифицированным пользователем

### Файловое хранилище
- Загрузка файлов в облако
- Создание папок и подпапок
- Просмотр содержимого директорий
- Переименование файлов и папок
- Перемещение файлов и папок
- Удаление файлов и папок
- Скачивание файлов и папок
- Поиск

### Безопасность
- Изоляция файлов пользователей
- Валидация входных данных
- Защита от несанкционированного доступа

## 🏗️ Архитектура

```
src/
├── main/java/by/practice/git/cloudstorage/
│   ├── config/          # Конфигурации (Security, MinIO, Redis)
│   ├── controller/      # REST контролеры
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Кастомные исключения
│   ├── mapper/         # Мапперы для преобразования данных
│   ├── model/          # JPA сущности
│   ├── repository/     # Spring Data репозитории
│   ├── service/        # Бизнес-логика
│   └── validation/     # Кастомные валидаторы
├── main/resources/
│   ├── db/changelog/   # Liquibase миграции
│   └── application.properties
└── test/               # Тесты с TestContainers
```

## 🛠️ Установка и запуск

### Предварительные требования
- Java 21+
- Maven 3.6+
- Docker и Docker Compose

### Запуск с Docker Compose

1. Клонируйте репозиторий:
```bash
git clone https://github.com/IlyaSeredich/cloud-storage
cd cloud-storage
```

2. Запустите инфраструктуру:
```bash
docker-compose up -d
```

3. Запустите приложение:
# Запуск с Maven Wrapper
./mvnw spring-boot:run

# Запуск с глобальным Maven
mvn spring-boot:run


По умолчанию приложение слушает порт 8080.
При запуске локально доступно по адресу http://localhost:8080

### Ручная настройка

1. Настройте PostgreSQL базу данных
2. Настройте MinIO сервер
3. Настройте Redis (опционально)
4. Создайте `.env` файл с настройками:
```env
DB_URL=jdbc:postgresql://localhost:5432/cloud_storage
DB_USERNAME=your_username
DB_PASSWORD=your_password
MINIO_URL=http://localhost:9000
MINIO_ACCESS_KEY=your_access_key
MINIO_SECRET_KEY=your_secret_key
MINIO_BUCKET=cloud-storage-bucket
REDIS_URL=redis://localhost:6379
```

## 📚 API Документация

После запуска приложения Swagger UI доступен по адресу:
http://localhost:8080/swagger-ui.html

### Основные эндпоинты

#### Аутентификация
- `POST /api/auth/sign-up` - Регистрация пользователя
- `POST /api/auth/sign-in` - Авторизация
- `GET /api/user/me` - Информация о текущем пользователе

#### Управление ресурсами
- `GET /api/resources` - Список файлов в директории
- `POST /api/resources/directory` - Создание папки
- `POST /api/resources/upload` - Загрузка файлов
- `GET /api/resources/download` - Скачивание файла
- `PUT /api/resources/move` - Перемещение/переименование
- `DELETE /api/resources` - Удаление файла/папки
- `GET /api/resources/search` - Поиск файлов

## 🧪 Тестирование

### Запуск тестов
# Запуск с Maven Wrapper
./mvnw test

# Запуск с глобальным Maven
mvn test

### Интеграционные тесты
Проект использует TestContainers для изолированного тестирования:
- PostgreSQL контейнер для тестов БД
- MinIO контейнер для тестов файлового хранилища

### Структура тестов
```
src/test/java/by/practice/git/cloudstorage/
├── BaseIntegrationTest.java           # Родительский класс с настройкой контейнеров
├── CloudStorageApplicationTests.java  # Тест загрузки контекста
├── controller/                        # Тесты контролеров
└── service/                          # Тесты сервисов
```
