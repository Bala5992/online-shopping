-- create table categories (id number, name varchar2(50),description varchar2(500), CONSTRAINT PK_CATEGORY primary key(id));
-- create sequence category_id_seq start with 1 maxvalue 9999999999 increment by 1 nocache;

-- create table products (id number, name varchar2(50),brand varchar2(50),description varchar2(500), category_id number, CONSTRAINT PK_PRODUCT primary key(id), CONSTRAINT FK_CategoryProduct FOREIGN KEY (category_id) REFERENCES categories(id));
-- create sequence product_id_seq start with 1 maxvalue 9999999999 increment by 1 nocache;

-- create table inventories (id number, product_id number, total number, available number, reserved number, CONSTRAINT PK_INVENTORY primary key(id), CONSTRAINT FK_ProductInventory FOREIGN KEY (product_id) REFERENCES products(id));
-- create sequence inventory_id_seq start with 1 maxvalue 9999999999 increment by 1 nocache;

-- create table attributes (id number, product_id number, a_name varchar2(50), a_value varchar2(50), CONSTRAINT PK_ATTRIBUTE primary key(id), CONSTRAINT FK_ProductAttribute FOREIGN KEY (product_id) REFERENCES products(id));
-- create sequence attribute_id_seq start with 1 maxvalue 9999999999 increment by 1 nocache;

-- create table prices (id number, product_id number, currency varchar2(3), amount DECIMAL, CONSTRAINT PK_PRICE primary key(id), CONSTRAINT FK_ProductPrice FOREIGN KEY (product_id) REFERENCES products(id));
-- create sequence price_id_seq start with 1 maxvalue 9999999999 increment by 1 nocache;
select 1 from dual;