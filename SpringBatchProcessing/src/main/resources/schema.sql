CREATE TABLE IF NOT EXISTS public.batch_file_product
(
    product_id SERIAL PRIMARY KEY,
    title varchar(200),
    description varchar(200),
    price varchar(200),
    discount varchar(200),
    discounted_price varchar(200)
);