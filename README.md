#  Инструкция по запуску backend проекта

##  Описание

Этот проект — backend онлайн-магазина книг **Okuylu (Smart Bookstore)**, написанный на **Spring Boot** с использованием **PostgreSQL**, **Stripe API**, **Cloudinary**, **Flyway** и **Spring Mail**.
Он поддерживает каталог книг, корзину, заказы, оплату, админ-панель и уведомления по email.

---

##  Настройка проекта

###  Шаг 1. Клонируйте репозиторий

```bash
git clone https://github.com/<your-username>/<your-repo-name>.git
cd <your-repo-name>
```

---

###  Шаг 2. Создайте файл `.env` в корне проекта

Файл `.env` **не хранится в репозитории** (он добавлен в `.gitignore`).
В него нужно добавить реальные данные для запуска локально.

Пример `.env`:

```
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=okuylu_project
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION_MS=86400000

# Mail (для отправки email)
MAIL_USERNAME=your_gmail_address@gmail.com
MAIL_PASSWORD=your_gmail_app_password

# Cloudinary (для хранения изображений книг)
CLOUDINARY_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Stripe (для оплаты)
STRIPE_SECRET_KEY=your_stripe_secret_key
STRIPE_WEBHOOK_SECRET=your_stripe_webhook_secret

# Клиент (Frontend)
CLIENT_URL=https://oku-kg.netlify.app

# Прочие настройки
EXCHANGE_RATE_SOM_TO_USD=90.93
STORE_ADDRESS="city Bishkek, street Jal, 123"
STORE_PHONE=+996555123456
TIMEZONE=Asia/Bishkek
```

---


##  Запуск приложения

### через Maven:

```bash
mvn clean package
java -jar target/okuylu_back.jar
```

или просто:

```bash
mvn spring-boot:run
```

---

##  Структура проекта

```
src/
 ├── main/java/com/okuylu
 │     ├── controller     # REST контроллеры
 │     ├── service        # бизнес-логика
 │     ├── repository     # работа с БД через JPA
 │     ├── model          # сущности
 |     ├── dto            # dto requests, responses
 │     ├── security       # конфигурации безопасности, почты и Stripe
 │     ├── utils          # exceptions, error responses
 │     └── config         # Swagger, Cloudinary
 └── resources/
       ├── db/migration   # SQL миграции Flyway
       ├── templates/     # HTML-шаблоны писем
       └── application.properties
```

---

##  Основные API-эндпоинты
### **Admin Author Controller** (`/api/admin/authors`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/authors` | Создать автора |
| `GET` | `/api/admin/authors/{id}` | Получить автора по ID |
| `GET` | `/api/admin/authors` | Получить список всех авторов |
| `PUT` | `/api/admin/authors/{id}` | Обновить данные автора |
| `DELETE` | `/api/admin/authors/{id}` | Удалить автора |

---

### **Admin Book Controller** (`/api/admin/books`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/books` | Добавить новую книгу |
| `PUT` | `/api/admin/books/{id}` | Редактировать информацию о книге |
| `DELETE` | `/api/admin/books/{id}` | Удалить книгу |
| `GET` | `/api/admin/books` | Получить список всех книг с пагинацией |
| `GET` | `/api/admin/books/{id}` | Получить книгу по ID |
| `POST` | `/api/admin/books/{bookId}/genre/{genreId}` | Связать книгу с жанром |
| `POST` | `/api/admin/books/{bookId}/tag/{tagId}` | Связать книгу с тегом |
| `DELETE` | `/api/admin/books/{bookId}/genre/{genreId}` | Отвязать жанр от книги |
| `DELETE` | `/api/admin/books/{bookId}/tag/{tagId}` | Отвязать тег от книги |

---

### **Admin Discount Controller** (`/api/admin/discounts`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/admin/discounts` | Получить список всех скидок |
| `GET` | `/api/admin/discounts/{id}` | Получить скидку по ID |
| `POST` | `/api/admin/discounts` | Создать скидку |
| `PUT` | `/api/admin/discounts/{id}` | Обновить скидку |
| `DELETE` | `/api/admin/discounts/{id}` | Удалить скидку |

---

### **Admin Discount Image Controller** (`/api/admin/discount-banners`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/discount-banners/upload` | Загрузить баннер скидки (1440x500px) |
| `DELETE` | `/api/admin/discount-banners/delete` | Удалить баннер скидки |
| `GET` | `/api/admin/discount-banners/info` | Получить информацию о баннере скидки |
| `GET` | `/api/admin/discount-banners/all` | Получить список всех баннеров |

---

### **Admin FAQ Controller** (`/api/admin/faq`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/faq` | Добавить вопрос FAQ |
| `DELETE` | `/api/admin/faq/{id}` | Удалить вопрос FAQ |
| `GET` | `/api/admin/faq` | Получить все вопросы FAQ |
| `PUT` | `/api/admin/faq/{id}` | Обновить вопрос FAQ |

---

### **Admin Genre Controller** (`/api/admin/genres`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/genres` | Создать жанр |
| `GET` | `/api/admin/genres` | Получить все жанры |
| `GET` | `/api/admin/genres/{id}` | Получить жанр по ID |
| `PUT` | `/api/admin/genres/{id}` | Обновить жанр |
| `DELETE` | `/api/admin/genres/{id}` | Удалить жанр |

---

### **Admin Image Controller** (`/api/admin/images`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/images/upload` | Загрузить изображение в Cloudinary |
| `DELETE` | `/api/admin/images/delete/{publicId}` | Удалить изображение из Cloudinary |
| `GET` | `/api/admin/images/info/{publicId}` | Получить информацию об изображении |
| `GET` | `/api/admin/images/all` | Получить список всех изображений |

---

### **Admin Manager Controller** (`/api/admin`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/admin/managers` | Получить список всех менеджеров с пагинацией |
| `PATCH` | `/api/admin/managers/{managerId}/block-status` | Изменить статус блокировки менеджера |
| `DELETE` | `/api/admin/managers/{managerId}` | Удалить менеджера |
| `POST` | `/api/admin/managers/register` | Зарегистрировать нового менеджера |
| `PATCH` | `/api/admin/managers/{managerId}/profile` | Обновить профиль менеджера |

---

### **Admin Order Controller** (`/api/admin/orders`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/admin/orders` | Получить все заказы с пагинацией |
| `POST` | `/api/admin/orders/{orderId}/status` | Обновить статус заказа |
| `GET` | `/api/admin/orders/status/{status}` | Получить заказы по статусу с пагинацией |

---

### **Admin Publisher Controller** (`/api/admin/publishers`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/publishers` | Создать издательство |
| `GET` | `/api/admin/publishers` | Получить все издательства |
| `GET` | `/api/admin/publishers/{id}` | Получить издательство по ID |
| `PUT` | `/api/admin/publishers/{id}` | Обновить издательство |
| `DELETE` | `/api/admin/publishers/{id}` | Удалить издательство |

---

### **Admin Statistics Controller** (`/api/admin/statistics`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/admin/statistics` | Получить статистику (с фильтрацией по датам) |

---

### **Admin Tag Controller** (`/api/admin/tags`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/admin/tags` | Создать тег |
| `GET` | `/api/admin/tags` | Получить все теги |
| `GET` | `/api/admin/tags/{id}` | Получить тег по ID |
| `PUT` | `/api/admin/tags/{id}` | Обновить тег |
| `DELETE` | `/api/admin/tags/{id}` | Удалить тег |

---

### **Admin User Controller** (`/api/admin`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/admin/users` | Получить список пользователей с пагинацией |
| `PATCH` | `/api/admin/users/{userId}/block-status` | Изменить статус блокировки пользователя |
| `DELETE` | `/api/admin/users/{userId}` | Удалить пользователя |

### **Cart Controller** (`/api/user/cart`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/user/cart/add` | Добавить книгу в корзину |
| `GET` | `/api/user/cart/items` | Получить список книг в корзине |
| `DELETE` | `/api/user/cart/clear` | Очистить корзину |
| `DELETE` | `/api/user/cart/remove/{cartItemId}` | Удалить книгу из корзины |
| `POST` | `/api/user/cart/increase/{cartItemId}` | Увеличить количество книги в корзине на 1 |
| `POST` | `/api/user/cart/decrease/{cartItemId}` | Уменьшить количество книги в корзине на 1 |

---

### **Order Controller** (`/api/user`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/user/orders/create` | Создать новый заказ |
| `GET` | `/api/user/orders/my-orders` | Получить список заказов пользователя с пагинацией |
| `GET` | `/api/user/orders/{orderId}` | Получить детали заказа по ID |
| `PUT` | `/api/user/orders/{orderId}/confirm-delivery` | Подтвердить получение заказа пользователем |
| `GET` | `/api/user/payments/confirm` | Подтвердить оплату по sessionId (Stripe) |

---

### **Payment Controller** (`/api/user/payments`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/user/payments/create-order` | Создать заказ и получить данные для оплаты |

---

### **Webhook Controller** (`/api/stripe`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/stripe/webhook` | Обработчик вебхуков Stripe (подтверждение оплаты) |

---

### **Wishlist Controller** (`/api/user/wishlist`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/user/wishlist/{bookId}` | Добавить книгу в избранное |
| `DELETE` | `/api/user/wishlist/{bookId}` | Удалить книгу из избранного |
| `GET` | `/api/user/wishlist` | Получить список избранных книг пользователя |
| `DELETE` | `/api/user/wishlist/clear` | Очистить список избранного |

### **ChatBot Controller** (`/api/public/chatbot`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/public/chatbot/start` | Получить стартовый узел диалога |
| `GET` | `/api/public/chatbot/node/{nodeId}` | Получить узел диалога по ID |
| `POST` | `/api/public/chatbot/answer` | Обработать ответ пользователя и получить следующий узел |
| `GET` | `/api/public/chatbot/all` | Получить все узлы дерева диалогов |

---

### **Book Controller** (`/api/public/books`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/public/books/{id}` | Получить книгу по ID |
| `GET` | `/api/public/books` | Получить список всех книг |
| `GET` | `/api/public/books/search` | Найти книги по части названия или по автору |
| `GET` | `/api/public/books/smart-search` | Умный поиск книг по описанию (AI) |
| `GET` | `/api/public/books/genres/{genreId}` | Получить список книг по жанру |
| `GET` | `/api/public/books/tags/{tagId}` | Получить список книг по тегу |
| `GET` | `/api/public/books/genres` | Получить все жанры |
| `GET` | `/api/public/books/tags` | Получить все теги |
| `GET` | `/api/public/books/discounts` | Получить баннер скидок (активные) |

---

### **Manager Order Controller** (`/api/manager`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/manager/orders/unassigned` | Получить список нераспределенных заказов с пагинацией |
| `GET` | `/api/manager/orders/status/{status}` | Получить список заказов по статусу (только для текущего менеджера) с пагинацией |
| `GET` | `/api/manager/orders/my-orders` | Получить список заказов, назначенных менеджеру с пагинацией |
| `POST` | `/api/manager/orders/{orderId}/assign` | Назначить заказ себе |
| `POST` | `/api/manager/orders/{orderId}/status` | Обновить статус заказа |
| `POST` | `/api/manager/orders/{orderId}/delivery-cost` | Установить стоимость доставки для заказа |
| `GET` | `/api/manager/profile` | Получить профиль текущего менеджера |
| `GET` | `/api/manager/orders/statistics` | Получить статистику заказов менеджера (с фильтрацией по датам) |

---

##  Дополнительные функции

* Отправка email-уведомлений пользователям на кыргызском и русском языках
* Панель администратора для обработки заказов
* Система статистики менеджеров
* Flyway миграции для базы данных

---



##  Автор

**Ayzirek Akjolkyzy**
Junior Java Developer | Bishkek, Kyrgyzstan
📧 [aizirek.akjolkyzy.ch@gmail.com](mailto:aizirek.akjolkyzy.ch@gmail.com)
🔗 GitHub: [https://github.com/your-username](https://github.com/Ayzirekjolkyzy/)
