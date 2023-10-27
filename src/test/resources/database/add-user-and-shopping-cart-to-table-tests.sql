INSERT INTO users (id, email, password, firstName, lastName, shippingAddress, is_deleted)
VALUES (1, 'admin@gmail.com', 'password', 'Ivan', 'Ivanov', 'Third Shipping Address', false);

INSERT INTO shopping_carts (id, user_id, is_deleted)
VALUES (1, 1, false);