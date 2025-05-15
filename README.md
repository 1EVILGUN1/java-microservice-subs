# User Subscription Management Service

Микросервис для управления пользователями и их подписками на Spring Boot.

## Обзор

Сервис предоставляет REST API для:
- Создания и управления профилями пользователей (имя, email)
- Добавления подписок для пользователей (например, YouTube Premium)
- Получения списка популярных подписок

Технологии:
- Spring Boot
- Spring Data JPA
- PostgreSQL (основная БД)
- H2 (для тестов)
- MapStruct (маппинг DTO)
- Lombok (уменьшение шаблонного кода)

## Требования

- Java 17 (OpenJDK 17)
- Maven 3.8.6+
- Docker и Docker Compose
- PostgreSQL клиент (опционально)
- Свободный порт 5434 (для PostgreSQL)

## Настройка проекта

1. Клонируйте репозиторий:
```bash
git clone <url-репозитория>
cd user-subscription-service
```

2. Проверьте зависимости:
```bash
mvn dependency:tree
```

3. Соберите проект (без тестов):
```bash
mvn clean package -DskipTests
```

## Настройка базы данных (Docker)

1. Запустите PostgreSQL контейнер:
```bash
docker-compose up -d
```

2. Проверьте работу БД:
```bash
docker-compose logs db
```
Ищите сообщение: `database system is ready to accept connections`

3. (Опционально) Подключитесь к БД:
```bash
psql -h localhost -p 5434 -U postgres -d subscription_db
```
Пароль: `password`

4. Остановка БД (при необходимости):
```bash
docker-compose down
```
Для полной очистки:
```bash
docker-compose down -v
```

## Конфигурация приложения

Основные настройки в `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5434/subscription_db
    username: postgres
    password: password
server:
  port: 8080
```

## Запуск приложения

1. Запустите приложение:
```bash
mvn spring-boot:run
```
Или через вашу IDE (класс `com.microservice.subs.SubsApplication`)

2. Проверьте работоспособность сервиса:
```bash
curl http://localhost:8080/actuator/health
```
Ожидаемый ответ: `{"status":"UP"}`

## API Endpoints

### Создать пользователя
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","name":"Test User"}'
```

### Добавить подписку
```bash
curl -X POST http://localhost:8080/users/1/subscriptions \
  -H "Content-Type: application/json" \
  -d '{"serviceName":"YouTube Premium"}'
```

### Получить топ подписок
```bash
curl http://localhost:8080/users/1/subscriptions/top
```

## Тестирование

1. Убедитесь, что H2 настроена в `src/test/resources/application-test.yml`

2. Запустите тесты:
```bash
mvn test
```

## Устранение неполадок

**Ошибка подключения к БД:**
- Проверьте, что контейнер запущен: `docker-compose ps`
- Проверьте порт: `lsof -i :5434`

**Логи для отладки:**
Добавьте в `application.yml`:
```yaml
logging:
  level:
    org.springframework: DEBUG
    org.hibernate: DEBUG
```

## Дополнительно

Сборка JAR:
```bash
mvn clean package -DskipTests
java -jar target/subs-0.0.1-SNAPSHOT.jar
```

Очистка Docker:
```bash
docker system prune -a
docker volume prune
```