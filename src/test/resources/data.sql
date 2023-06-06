insert into categories(id, name, description) values (1000, 'Electronics', 'Products under electronics');

insert into products(id, name, brand, description, category_id) values (2000, 'Laptop', 'Dell', 'Powerful and lightweight laptop for work and entertainment.', 1000);

insert into attributes(id, product_id, a_name, a_value) values (3000, 2000, 'Color', 'Silver');
insert into attributes(id, product_id, a_name, a_value) values (3001, 2000, 'Processor', 'Intel Core i7');
insert into attributes(id, product_id, a_name, a_value) values (3002, 2000, 'Memory', '16GB');

insert into inventories(id, product_id, available, total, reserved) values (4000, 2000, 25, 20, 5);

insert into prices(id, product_id, currency, amount) values (5000, 2000, 'USD', 1299.99);