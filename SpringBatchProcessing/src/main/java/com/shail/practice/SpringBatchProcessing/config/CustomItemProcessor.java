package com.shail.practice.SpringBatchProcessing.config;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        //logic to perform you can write on item
        //logic for discount price

        int discountPer = Integer.parseInt(item.getDiscount());
        double orginalPrice = Double.parseDouble(item.getPrice());
        double discount=(discountPer/100)* orginalPrice;
        double finalPrice = orginalPrice-discount;
        item.setDiscountedPrice(String.valueOf(finalPrice));
        return item;
    }
}
