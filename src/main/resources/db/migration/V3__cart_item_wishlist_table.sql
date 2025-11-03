CREATE TABLE wishlist (
    wishlist_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    CONSTRAINT unique_wishlist UNIQUE (user_id, book_id)
);


CREATE TABLE cart_items (
    cart_id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id INT NOT NULL REFERENCES books(book_id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_cart UNIQUE (user_id, book_id)
);