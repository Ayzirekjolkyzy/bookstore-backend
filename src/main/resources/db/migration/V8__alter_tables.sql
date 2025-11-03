ALTER TABLE email_verification_tokens DROP CONSTRAINT email_verification_tokens_user_id_fkey;

ALTER TABLE email_verification_tokens
ADD CONSTRAINT email_verification_tokens_user_id_fkey
FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE;


-- Удаляение старых ограничений
ALTER TABLE books DROP CONSTRAINT fk_books_author;
ALTER TABLE books DROP CONSTRAINT fk_books_publisher;
ALTER TABLE books DROP CONSTRAINT fk_books_discount;

-- Добавление новых с ON DELETE SET NULL
ALTER TABLE books
ADD CONSTRAINT fk_books_author
FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE SET NULL;

ALTER TABLE books
ADD CONSTRAINT fk_books_publisher
FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id) ON DELETE SET NULL;

ALTER TABLE books
ADD CONSTRAINT fk_books_discount
FOREIGN KEY (discount_id) REFERENCES discounts(discount_id) ON DELETE SET NULL;