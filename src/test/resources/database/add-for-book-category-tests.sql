INSERT INTO categories (id, name, is_deleted)
VALUES (1, 'Fantasy', false);

INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Odyssey', 'Homer', '98765432', 250.50);


INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);