INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Odyssey', 'Homer', '98765432', 250.50);

INSERT INTO categories (id, name)
VALUES (1, 'Fantasy');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'admin@gmail.com', 'password', 'Ivan', 'Ivanov', 'Third Shipping Address', false);

INSERT INTO shopping_carts (id, user_id, is_deleted)
VALUES (1, 1, false);

INSERT INTO cart_items (id, shopping_carts_id, books_id, quantity, is_deleted)
VALUES (1, 1, 1, 2, false);