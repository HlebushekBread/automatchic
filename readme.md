# Автоматчик - Трекер учебных требований
## <a href="http://softloaf.net">softloaf.net</a>

Домашний Spring boot проект, построенный на микросервисной event-driven архитектуре <br>
с использованием Kafka, со сбором и отображением метрик через Prometheus и Grafana. <br>
Просмотр прогресса реализован через CQRS и Event Sourcing с помощью Axon. <br>

#### <a href="https://github.com/HlebushekBread/automatchic_web">Frontend часть</a>

---

## Основные возможности

- Управление учебными требованиями по дисциплинам
- Аутентификация и авторизация (JWT)
- Отслеживание истории успеваемости
- Возможность делиться созданными дисциплинами
- Возможность просматривать и искать публичные дисциплины
- Email-уведомления (подтверждение аккаунта, сброс пароля)
- Асинхронное взаимодействие через Kafka
- Event Sourcing + CQRS (Axon Framework)
- Мониторинг и метрики (Prometheus + Grafana)

---

## Архитектура

Система состоит из нескольких микросервисов:

### 1. `app-service`
- CRUD логика
- Spring Security + JWT
- Работа с PostgreSQL
- Кэширование через Redis
- Публикация событий в Kafka

### 2. `history-service`
- Хранение истории изменений
- Реализация **CQRS + Event Sourcing**
- Axon Framework + Axon Server
- Отдельная база данных

### 3. `notification-service`
- Подписка на события Kafka
- Отправка email уведомлений

### 4. `common-module`
- Общие DTO
- конфигурация Kafka
- Enum и shared логика

---

## Используемые технологии

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Apache Kafka
- Axon Framework
- PostgreSQL
- Redis
- Docker & Docker Compose
- Prometheus + Grafana + Alertmanager

---

## Запуск проекта

### 1. Подготовка `.env`

Создайте файл `.env` в корне проекта:

```env
APP_DB_URL=
APP_DB_USERNAME=
APP_DB_PASSWORD=

HISTORY_DB_URL=
HISTORY_DB_USERNAME=
HISTORY_DB_PASSWORD=

JWT_SECRET=
JWT_LIFETIME=

BROKER_URL=

REDIS_HOST=
REDIS_PORT=
REDIS_PASSWORD=
REDIS_TIMEOUT=

GRAFANA_ADMIN_PASSWORD=

MAIL_HOST= MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=

FRONTEND_URL=
```

Создайте файл /docker-compose/alertmanager.yml по шаблону docker-compose/alertmanager.template.yml <br>
и вручную заполните его значениями из .env (Alert manager не умеет в переменные окружения).

Запустите командой
```
docker-compose --env-file .env up --build -d
```