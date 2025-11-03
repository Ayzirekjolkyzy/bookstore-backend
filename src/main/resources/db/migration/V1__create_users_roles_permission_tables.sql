-- Создание таблицы Roles
CREATE TABLE Roles (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);


-- Создание таблицы Permissions
CREATE TABLE Permissions (
    permission_id SERIAL PRIMARY KEY,
    permission_name VARCHAR(255) NOT NULL
);


-- Создание таблицы RolePermissionsAssociation
CREATE TABLE Role_permissions_association (
    role_permission_id SERIAL PRIMARY KEY,
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id),
    FOREIGN KEY (permission_id) REFERENCES Permissions(permission_id)
);

--CREATE TYPE gender_enum AS ENUM ('male', 'female', 'none');

-- Создание таблицы users
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gender BOOLEAN,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_email_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- Таблица для хранения токенов подтверждения email
CREATE TABLE email_verification_tokens (
    token_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);




-- Создаем функцию для обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создаем триггер для таблицы users
CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Вставка начальных ролей
INSERT INTO Roles (role_id, role_name) VALUES
(1, 'ADMIN'),
(2, 'MANAGER'),
(3, 'USER'),
(4, 'GUEST');


-- Вставка начальных разрешений
INSERT INTO Permissions (permission_id, permission_name) VALUES
(1, 'add_remove_books'),
(2, 'manage_discounts'),
(3, 'manage_users'),
(4, 'manage_managers'),
(5, 'view_analytics'),
(6, 'block_users'),
(7, 'process_orders'),
(8, 'manage_reviews'),
(9, 'search_books'),
(10, 'use_chatbot'),
(11, 'view_catalog'),
(12, 'view_book_details'),
(13, 'register'),
(14, 'place_order'),
(15, 'leave_review'),
(16, 'personalized_recommendations'),
(17, 'smart_search'),
(18, 'track_order_status'),
(19, 'add_to_cart'),
(20, 'add_to_wishlist'),
(21, 'edit_profile');

-- Разрешения для гостей (role_id = 4)
INSERT INTO role_permissions_association (role_permission_id, role_id, permission_id) VALUES
(1, 4, 13), -- Гость может регистрироваться
(2, 4, 10), -- Гость может использовать чат-бот
(3, 4, 11); -- Гость может просматривать главную страницу (каталог)

-- Разрешения для зарегистрированных пользователей (role_id = 3)
INSERT INTO role_permissions_association (role_permission_id, role_id, permission_id) VALUES
(4, 3, 13), -- Пользователь может регистрироваться (если нужно)
(5, 3, 10), -- Пользователь может использовать чат-бот
(6, 3, 11), -- Пользователь может просматривать главную страницу
(7, 3, 21); -- Пользователь может редактировать профиль


