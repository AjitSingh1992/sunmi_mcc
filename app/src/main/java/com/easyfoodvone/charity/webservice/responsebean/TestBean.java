package com.easyfoodvone.charity.webservice.responsebean;

import java.util.List;

public class TestBean {

    /**
     * success : true
     * message : Order details!
     * orders_details : {"average_delivery_time":"30","average_preparation_time":"1","order_id":"3e372984-5154-11ea-b98d-0657952ed75a","restaurant_id":"254a2092-4e44-11ea-a5a0-0657952ed75a","customer_id":"bb20307c-5018-11ea-9200-0657952ed75a","cart_id":"3e2dd7b2-5154-11ea-b690-0657952ed75a","order_num":"158192328599","order_total":"236.95","order_date_time":"17 Feb 2020 07:08:05","is_paid":0,"payment_mode":"cash","payment_status":"pending","is_delivered":0,"delivery_time":"30","delivery_option":"collection","delivery_date_time":"17 Feb 2020 07:07:00","delivery_charge":"0.00","discount_amount":"0.00","sub_total":"236.95","voucher_id":"","offer_id":"","order_notes":"","order_reject_note":"","cart":{"menu":[{"rowId":"c6e5957366161c2c2a6f06e8f918bf6e","id":"c3390f60-4e63-11ea-8b49-0657952ed75a","name":"Chilli Chip Butty","qty":1,"price":6.3,"options":{"size":{"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]},"productModifiers":[]},"tax":0,"subtotal":6.3},{"rowId":"941657132eb0c98cdc630caeebed1a08","id":"5c852388-4e47-11ea-851d-0657952ed75a","name":"Chicken Tikka","qty":1,"price":5.95,"options":{},"tax":0,"subtotal":5.95},{"rowId":"ddc67c221e7d682b603d6d6f127a7ece","id":"735238d6-4e4b-11ea-8a7e-0657952ed75a","name":"Sheesh Kebab","qty":1,"price":5.95,"options":{},"tax":0,"subtotal":5.95},{"rowId":"4be8251282a74cec0b73675714a90672","id":"9b19889c-4e4b-11ea-8812-0657952ed75a","name":"Lamb Chops","qty":1,"price":7.5,"options":{},"tax":0,"subtotal":7.5},{"rowId":"71231dd358464f3a00f667cd08296c10","id":"b9b727be-4e4b-11ea-9c07-0657952ed75a","name":"Fish Pakora","qty":1,"price":7.25,"options":{},"tax":0,"subtotal":7.25},{"rowId":"5be80858e5ced4a14a89b7066c8624bd","id":"d1ea88f8-4e4b-11ea-9431-0657952ed75a","name":"Chilli Chicken","qty":1,"price":7.25,"options":{},"tax":0,"subtotal":7.25},{"rowId":"b3c755b00baadd01ab85f05f937951ca","id":"0e86e01c-4e4d-11ea-84c3-0657952ed75a","name":"Chilli Paneer","qty":1,"price":5.5,"options":{},"tax":0,"subtotal":5.5},{"rowId":"e674481e20f24b9757fafc3d12eb5484","id":"2b1576b2-4e4d-11ea-b03d-0657952ed75a","name":"Samosa","qty":1,"price":4,"options":{},"tax":0,"subtotal":4},{"rowId":"b0e9300d9cf532ddcee238a447088339","id":"44dc5d68-4e4d-11ea-96ef-0657952ed75a","name":"Samosa Chaat","qty":1,"price":5.25,"options":{},"tax":0,"subtotal":5.25},{"rowId":"694e52a15cd79bef896d9ffcabab9010","id":"613d13d0-4e4d-11ea-9574-0657952ed75a","name":"Paneer Tikka","qty":1,"price":5.5,"options":{},"tax":0,"subtotal":5.5},{"rowId":"adfac12386b1524df60289feba9fac9e","id":"efad6c6e-4e4d-11ea-bb6e-0657952ed75a","name":"Regular Sizzler","qty":1,"price":9.95,"options":{},"tax":0,"subtotal":9.95},{"rowId":"f9e783bae8ddbb4d73bac802cc12ac30","id":"32a837c4-4e4e-11ea-b67e-0657952ed75a","name":"Large Sizzler","qty":1,"price":13.95,"options":{},"tax":0,"subtotal":13.95},{"rowId":"b5395f7bb5e6691b94f8837f59ef2b56","id":"4931a39a-4e4e-11ea-a839-0657952ed75a","name":"Wagon Grill Special Sizzler","qty":1,"price":15.95,"options":{},"tax":0,"subtotal":15.95},{"rowId":"71f1e912afb42992894769e495347b36","id":"b1f45904-4e4e-11ea-9164-0657952ed75a","name":"Butter Chicken","qty":1,"price":7.5,"options":{},"tax":0,"subtotal":7.5},{"rowId":"2eeafa3f0d929071ac65afe3e69fed88","id":"cff06448-4e4e-11ea-9909-0657952ed75a","name":"Chicken Tikka Masala","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"2bef6840e0c233ee9fc651f6e502691f","id":"e94caf32-4e4e-11ea-9581-0657952ed75a","name":"Chicken Madras","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"93729882cd2fd414b19d8f107dcce23b","id":"0093c6a8-4e4f-11ea-8f6f-0657952ed75a","name":"Chicken Vindaloo","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"649f2d5a6da4545a7a6b89ffd6b1e654","id":"1a9e46f4-4e4f-11ea-80a6-0657952ed75a","name":"Chicken Jalfrazi","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"046a0206c9253412719e3071bea8f556","id":"3e8607a0-4e4f-11ea-ac59-0657952ed75a","name":"Chicken Korma","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"fe2dab20414ac6c4a0580c9374b47f54","id":"6ac38332-4e50-11ea-bb5b-0657952ed75a","name":"Lamb Korma","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"9a0ec6efa8218e4260a9dd6b111f0a17","id":"86b72f8a-4e50-11ea-9fd4-0657952ed75a","name":"Lamb Curry","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"95585381d2ef3b6c9496cfe35f9ab81f","id":"9c5d5062-4e50-11ea-8e57-0657952ed75a","name":"Lamb Balti","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"482576bd39cf441848e370070b1b1a31","id":"b374e990-4e50-11ea-9db7-0657952ed75a","name":"Lamb Saag","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"0386227cf3e53ae81912aeaf6fe1941d","id":"cc613436-4e50-11ea-a848-0657952ed75a","name":"Lamb Dhansak","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"a5793d4b86bb9aae4fdcb9a4032633bd","id":"e13b9d4c-4e50-11ea-bd40-0657952ed75a","name":"Lamb Rogan Josh","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"4d60dbd1d1b981c812da8ddc704ebe85","id":"79e9703c-4e51-11ea-84a8-0657952ed75a","name":"Fish Curry","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"ca4fbc61dd9d1442bfb3e76d9e4dff82","id":"a8a6a41c-4e51-11ea-8db5-0657952ed75a","name":"Prawn Curry","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"69e50ccec91a9e6ab05b79939bf2e7cd","id":"c1a233c8-4e51-11ea-a106-0657952ed75a","name":"Prawn Jalfrazi","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"704c2fd4a204215162ae0541f07ee8aa","id":"f1606be8-4e51-11ea-9ea3-0657952ed75a","name":"Channa Masala","qty":1,"price":6,"options":{},"tax":0,"subtotal":6},{"rowId":"d5d4602b55a2b694f2cce3c3b59f05d4","id":"d044ae08-4e54-11ea-bdc1-0657952ed75a","name":"Plain Rice","qty":1,"price":2.25,"options":{},"tax":0,"subtotal":2.25},{"rowId":"06c4925c3117358d927632168592d5f8","id":"ea32bd00-4e54-11ea-9713-0657952ed75a","name":"Egg Fried Rice","qty":1,"price":2.5,"options":{},"tax":0,"subtotal":2.5},{"rowId":"09ad4e0e143c1e57a1c1e5e5dad87b14","id":"0604609c-4e55-11ea-9164-0657952ed75a","name":"Pilau Rice","qty":1,"price":2.75,"options":{},"tax":0,"subtotal":2.75},{"rowId":"874d48f515b3adc6b903ffd33e20b293","id":"66b73ad0-4e65-11ea-86bb-0657952ed75a","name":"A trio of Kulfi (Indian ice cream)","qty":1,"price":3.95,"options":{},"tax":0,"subtotal":3.95},{"rowId":"132ac9559448f49cf396dfaae1d99c74","id":"4dfc10c4-4e65-11ea-a2ea-0657952ed75a","name":"A trio of ice cream","qty":1,"price":3.95,"options":{},"tax":0,"subtotal":3.95},{"rowId":"c6e5957366161c2c2a6f06e8f918bf6e","id":"c3390f60-4e63-11ea-8b49-0657952ed75a","name":"Chilli Chip Butty","qty":1,"price":6.3,"options":{"size":{"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]},"productModifiers":[]},"tax":0,"subtotal":6.3}]},"delivery_address":{"customer_name":"Dheeraj Pandey","phone_number":"8810615248","email":"dhiraj.omnisttechhub@gmail.com","customer_address_id":"","customer_location":"    ","address_1":"","address_2":"","city":"","post_code":"","country":"","address_type":""}}
     * errors : {}
     */

    private boolean success;
    private String message;
    private OrdersDetailsBean orders_details;
    private ErrorsBean errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrdersDetailsBean getOrders_details() {
        return orders_details;
    }

    public void setOrders_details(OrdersDetailsBean orders_details) {
        this.orders_details = orders_details;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class OrdersDetailsBean {
        /**
         * average_delivery_time : 30
         * average_preparation_time : 1
         * order_id : 3e372984-5154-11ea-b98d-0657952ed75a
         * restaurant_id : 254a2092-4e44-11ea-a5a0-0657952ed75a
         * customer_id : bb20307c-5018-11ea-9200-0657952ed75a
         * cart_id : 3e2dd7b2-5154-11ea-b690-0657952ed75a
         * order_num : 158192328599
         * order_total : 236.95
         * order_date_time : 17 Feb 2020 07:08:05
         * is_paid : 0
         * payment_mode : cash
         * payment_status : pending
         * is_delivered : 0
         * delivery_time : 30
         * delivery_option : collection
         * delivery_date_time : 17 Feb 2020 07:07:00
         * delivery_charge : 0.00
         * discount_amount : 0.00
         * sub_total : 236.95
         * voucher_id :
         * offer_id :
         * order_notes :
         * order_reject_note :
         * cart : {"menu":[{"rowId":"c6e5957366161c2c2a6f06e8f918bf6e","id":"c3390f60-4e63-11ea-8b49-0657952ed75a","name":"Chilli Chip Butty","qty":1,"price":6.3,"options":{"size":{"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]},"productModifiers":[]},"tax":0,"subtotal":6.3},{"rowId":"941657132eb0c98cdc630caeebed1a08","id":"5c852388-4e47-11ea-851d-0657952ed75a","name":"Chicken Tikka","qty":1,"price":5.95,"options":{},"tax":0,"subtotal":5.95},{"rowId":"ddc67c221e7d682b603d6d6f127a7ece","id":"735238d6-4e4b-11ea-8a7e-0657952ed75a","name":"Sheesh Kebab","qty":1,"price":5.95,"options":{},"tax":0,"subtotal":5.95},{"rowId":"4be8251282a74cec0b73675714a90672","id":"9b19889c-4e4b-11ea-8812-0657952ed75a","name":"Lamb Chops","qty":1,"price":7.5,"options":{},"tax":0,"subtotal":7.5},{"rowId":"71231dd358464f3a00f667cd08296c10","id":"b9b727be-4e4b-11ea-9c07-0657952ed75a","name":"Fish Pakora","qty":1,"price":7.25,"options":{},"tax":0,"subtotal":7.25},{"rowId":"5be80858e5ced4a14a89b7066c8624bd","id":"d1ea88f8-4e4b-11ea-9431-0657952ed75a","name":"Chilli Chicken","qty":1,"price":7.25,"options":{},"tax":0,"subtotal":7.25},{"rowId":"b3c755b00baadd01ab85f05f937951ca","id":"0e86e01c-4e4d-11ea-84c3-0657952ed75a","name":"Chilli Paneer","qty":1,"price":5.5,"options":{},"tax":0,"subtotal":5.5},{"rowId":"e674481e20f24b9757fafc3d12eb5484","id":"2b1576b2-4e4d-11ea-b03d-0657952ed75a","name":"Samosa","qty":1,"price":4,"options":{},"tax":0,"subtotal":4},{"rowId":"b0e9300d9cf532ddcee238a447088339","id":"44dc5d68-4e4d-11ea-96ef-0657952ed75a","name":"Samosa Chaat","qty":1,"price":5.25,"options":{},"tax":0,"subtotal":5.25},{"rowId":"694e52a15cd79bef896d9ffcabab9010","id":"613d13d0-4e4d-11ea-9574-0657952ed75a","name":"Paneer Tikka","qty":1,"price":5.5,"options":{},"tax":0,"subtotal":5.5},{"rowId":"adfac12386b1524df60289feba9fac9e","id":"efad6c6e-4e4d-11ea-bb6e-0657952ed75a","name":"Regular Sizzler","qty":1,"price":9.95,"options":{},"tax":0,"subtotal":9.95},{"rowId":"f9e783bae8ddbb4d73bac802cc12ac30","id":"32a837c4-4e4e-11ea-b67e-0657952ed75a","name":"Large Sizzler","qty":1,"price":13.95,"options":{},"tax":0,"subtotal":13.95},{"rowId":"b5395f7bb5e6691b94f8837f59ef2b56","id":"4931a39a-4e4e-11ea-a839-0657952ed75a","name":"Wagon Grill Special Sizzler","qty":1,"price":15.95,"options":{},"tax":0,"subtotal":15.95},{"rowId":"71f1e912afb42992894769e495347b36","id":"b1f45904-4e4e-11ea-9164-0657952ed75a","name":"Butter Chicken","qty":1,"price":7.5,"options":{},"tax":0,"subtotal":7.5},{"rowId":"2eeafa3f0d929071ac65afe3e69fed88","id":"cff06448-4e4e-11ea-9909-0657952ed75a","name":"Chicken Tikka Masala","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"2bef6840e0c233ee9fc651f6e502691f","id":"e94caf32-4e4e-11ea-9581-0657952ed75a","name":"Chicken Madras","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"93729882cd2fd414b19d8f107dcce23b","id":"0093c6a8-4e4f-11ea-8f6f-0657952ed75a","name":"Chicken Vindaloo","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"649f2d5a6da4545a7a6b89ffd6b1e654","id":"1a9e46f4-4e4f-11ea-80a6-0657952ed75a","name":"Chicken Jalfrazi","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"046a0206c9253412719e3071bea8f556","id":"3e8607a0-4e4f-11ea-ac59-0657952ed75a","name":"Chicken Korma","qty":1,"price":7,"options":{},"tax":0,"subtotal":7},{"rowId":"fe2dab20414ac6c4a0580c9374b47f54","id":"6ac38332-4e50-11ea-bb5b-0657952ed75a","name":"Lamb Korma","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"9a0ec6efa8218e4260a9dd6b111f0a17","id":"86b72f8a-4e50-11ea-9fd4-0657952ed75a","name":"Lamb Curry","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"95585381d2ef3b6c9496cfe35f9ab81f","id":"9c5d5062-4e50-11ea-8e57-0657952ed75a","name":"Lamb Balti","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"482576bd39cf441848e370070b1b1a31","id":"b374e990-4e50-11ea-9db7-0657952ed75a","name":"Lamb Saag","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"0386227cf3e53ae81912aeaf6fe1941d","id":"cc613436-4e50-11ea-a848-0657952ed75a","name":"Lamb Dhansak","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"a5793d4b86bb9aae4fdcb9a4032633bd","id":"e13b9d4c-4e50-11ea-bd40-0657952ed75a","name":"Lamb Rogan Josh","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"4d60dbd1d1b981c812da8ddc704ebe85","id":"79e9703c-4e51-11ea-84a8-0657952ed75a","name":"Fish Curry","qty":1,"price":8,"options":{},"tax":0,"subtotal":8},{"rowId":"ca4fbc61dd9d1442bfb3e76d9e4dff82","id":"a8a6a41c-4e51-11ea-8db5-0657952ed75a","name":"Prawn Curry","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"69e50ccec91a9e6ab05b79939bf2e7cd","id":"c1a233c8-4e51-11ea-a106-0657952ed75a","name":"Prawn Jalfrazi","qty":1,"price":8.25,"options":{},"tax":0,"subtotal":8.25},{"rowId":"704c2fd4a204215162ae0541f07ee8aa","id":"f1606be8-4e51-11ea-9ea3-0657952ed75a","name":"Channa Masala","qty":1,"price":6,"options":{},"tax":0,"subtotal":6},{"rowId":"d5d4602b55a2b694f2cce3c3b59f05d4","id":"d044ae08-4e54-11ea-bdc1-0657952ed75a","name":"Plain Rice","qty":1,"price":2.25,"options":{},"tax":0,"subtotal":2.25},{"rowId":"06c4925c3117358d927632168592d5f8","id":"ea32bd00-4e54-11ea-9713-0657952ed75a","name":"Egg Fried Rice","qty":1,"price":2.5,"options":{},"tax":0,"subtotal":2.5},{"rowId":"09ad4e0e143c1e57a1c1e5e5dad87b14","id":"0604609c-4e55-11ea-9164-0657952ed75a","name":"Pilau Rice","qty":1,"price":2.75,"options":{},"tax":0,"subtotal":2.75},{"rowId":"874d48f515b3adc6b903ffd33e20b293","id":"66b73ad0-4e65-11ea-86bb-0657952ed75a","name":"A trio of Kulfi (Indian ice cream)","qty":1,"price":3.95,"options":{},"tax":0,"subtotal":3.95},{"rowId":"132ac9559448f49cf396dfaae1d99c74","id":"4dfc10c4-4e65-11ea-a2ea-0657952ed75a","name":"A trio of ice cream","qty":1,"price":3.95,"options":{},"tax":0,"subtotal":3.95},{"rowId":"c6e5957366161c2c2a6f06e8f918bf6e","id":"c3390f60-4e63-11ea-8b49-0657952ed75a","name":"Chilli Chip Butty","qty":1,"price":6.3,"options":{"size":{"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]},"productModifiers":[]},"tax":0,"subtotal":6.3}]}
         * delivery_address : {"customer_name":"Dheeraj Pandey","phone_number":"8810615248","email":"dhiraj.omnisttechhub@gmail.com","customer_address_id":"","customer_location":"    ","address_1":"","address_2":"","city":"","post_code":"","country":"","address_type":""}
         */

        private String average_delivery_time;
        private String average_preparation_time;
        private String order_id;
        private String restaurant_id;
        private String customer_id;
        private String cart_id;
        private String order_num;
        private String order_total;
        private String order_date_time;
        private int is_paid;
        private String payment_mode;
        private String payment_status;
        private int is_delivered;
        private String delivery_time;
        private String delivery_option;
        private String delivery_date_time;
        private String delivery_charge;
        private String discount_amount;
        private String sub_total;
        private String voucher_id;
        private String offer_id;
        private String order_notes;
        private String order_reject_note;
        private CartBean cart;
        private DeliveryAddressBean delivery_address;

        public String getAverage_delivery_time() {
            return average_delivery_time;
        }

        public void setAverage_delivery_time(String average_delivery_time) {
            this.average_delivery_time = average_delivery_time;
        }

        public String getAverage_preparation_time() {
            return average_preparation_time;
        }

        public void setAverage_preparation_time(String average_preparation_time) {
            this.average_preparation_time = average_preparation_time;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getOrder_total() {
            return order_total;
        }

        public void setOrder_total(String order_total) {
            this.order_total = order_total;
        }

        public String getOrder_date_time() {
            return order_date_time;
        }

        public void setOrder_date_time(String order_date_time) {
            this.order_date_time = order_date_time;
        }

        public int getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(int is_paid) {
            this.is_paid = is_paid;
        }

        public String getPayment_mode() {
            return payment_mode;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public int getIs_delivered() {
            return is_delivered;
        }

        public void setIs_delivered(int is_delivered) {
            this.is_delivered = is_delivered;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getDelivery_option() {
            return delivery_option;
        }

        public void setDelivery_option(String delivery_option) {
            this.delivery_option = delivery_option;
        }

        public String getDelivery_date_time() {
            return delivery_date_time;
        }

        public void setDelivery_date_time(String delivery_date_time) {
            this.delivery_date_time = delivery_date_time;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        public String getSub_total() {
            return sub_total;
        }

        public void setSub_total(String sub_total) {
            this.sub_total = sub_total;
        }

        public String getVoucher_id() {
            return voucher_id;
        }

        public void setVoucher_id(String voucher_id) {
            this.voucher_id = voucher_id;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public void setOffer_id(String offer_id) {
            this.offer_id = offer_id;
        }

        public String getOrder_notes() {
            return order_notes;
        }

        public void setOrder_notes(String order_notes) {
            this.order_notes = order_notes;
        }

        public String getOrder_reject_note() {
            return order_reject_note;
        }

        public void setOrder_reject_note(String order_reject_note) {
            this.order_reject_note = order_reject_note;
        }

        public CartBean getCart() {
            return cart;
        }

        public void setCart(CartBean cart) {
            this.cart = cart;
        }

        public DeliveryAddressBean getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(DeliveryAddressBean delivery_address) {
            this.delivery_address = delivery_address;
        }

        public static class CartBean {
            private List<MenuBean> menu;

            public List<MenuBean> getMenu() {
                return menu;
            }

            public void setMenu(List<MenuBean> menu) {
                this.menu = menu;
            }

            public static class MenuBean {
                /**
                 * rowId : c6e5957366161c2c2a6f06e8f918bf6e
                 * id : c3390f60-4e63-11ea-8b49-0657952ed75a
                 * name : Chilli Chip Butty
                 * qty : 1
                 * price : 6.3
                 * options : {"size":{"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]},"productModifiers":[]}
                 * tax : 0
                 * subtotal : 6.3
                 */

                private String rowId;
                private String id;
                private String name;
                private int qty;
                private double price;
                private OptionsBean options;
                private int tax;
                private double subtotal;

                public String getRowId() {
                    return rowId;
                }

                public void setRowId(String rowId) {
                    this.rowId = rowId;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getQty() {
                    return qty;
                }

                public void setQty(int qty) {
                    this.qty = qty;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public OptionsBean getOptions() {
                    return options;
                }

                public void setOptions(OptionsBean options) {
                    this.options = options;
                }

                public int getTax() {
                    return tax;
                }

                public void setTax(int tax) {
                    this.tax = tax;
                }

                public double getSubtotal() {
                    return subtotal;
                }

                public void setSubtotal(double subtotal) {
                    this.subtotal = subtotal;
                }

                public static class OptionsBean {
                    /**
                     * size : {"id":"cd308ae2-4e64-11ea-b059-0657952ed75a","productSizeName":"Regular","productSizePrice":"4.50","quantity":1,"sizemodifiers":[{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]}
                     * productModifiers : []
                     */

                    private SizeBean size;
                    private List<?> productModifiers;

                    public SizeBean getSize() {
                        return size;
                    }

                    public void setSize(SizeBean size) {
                        this.size = size;
                    }

                    public List<?> getProductModifiers() {
                        return productModifiers;
                    }

                    public void setProductModifiers(List<?> productModifiers) {
                        this.productModifiers = productModifiers;
                    }

                    public static class SizeBean {
                        /**
                         * id : cd308ae2-4e64-11ea-b059-0657952ed75a
                         * productSizeName : Regular
                         * productSizePrice : 4.50
                         * quantity : 1
                         * sizemodifiers : [{"maxAllowedQuantity":"0","minAllowedQuantity":"0","modifierId":"cd325d40-4e64-11ea-b89b-0657952ed75a","modifierName":"Choose Option:","modifierType":"paid","sizeModifierProducts":[{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]}]
                         */

                        private String id;
                        private String productSizeName;
                        private String productSizePrice;
                        private int quantity;
                        private List<SizemodifiersBean> sizemodifiers;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getProductSizeName() {
                            return productSizeName;
                        }

                        public void setProductSizeName(String productSizeName) {
                            this.productSizeName = productSizeName;
                        }

                        public String getProductSizePrice() {
                            return productSizePrice;
                        }

                        public void setProductSizePrice(String productSizePrice) {
                            this.productSizePrice = productSizePrice;
                        }

                        public int getQuantity() {
                            return quantity;
                        }

                        public void setQuantity(int quantity) {
                            this.quantity = quantity;
                        }

                        public List<SizemodifiersBean> getSizemodifiers() {
                            return sizemodifiers;
                        }

                        public void setSizemodifiers(List<SizemodifiersBean> sizemodifiers) {
                            this.sizemodifiers = sizemodifiers;
                        }

                        public static class SizemodifiersBean {
                            /**
                             * maxAllowedQuantity : 0
                             * minAllowedQuantity : 0
                             * modifierId : cd325d40-4e64-11ea-b89b-0657952ed75a
                             * modifierName : Choose Option:
                             * modifierType : paid
                             * sizeModifierProducts : [{"modifierProductPrice":"0.00","amount":0,"productId":"cd338f94-4e64-11ea-a62e-0657952ed75a","productName":"No Cheese","unit":"Item","quantity":"7","originalQuantity":"7","originalAmount":0,"originalAmount1":0},{"modifierProductPrice":"0.30","amount":1.7999999999999998,"productId":"cd342fd0-4e64-11ea-9a61-0657952ed75a","productName":"Add Cheese","unit":"Item","quantity":"6","originalQuantity":"6","originalAmount":1.7999999999999998,"originalAmount1":1.7999999999999998}]
                             */

                            private String maxAllowedQuantity;
                            private String minAllowedQuantity;
                            private String modifierId;
                            private String modifierName;
                            private String modifierType;
                            private List<SizeModifierProductsBean> sizeModifierProducts;

                            public String getMaxAllowedQuantity() {
                                return maxAllowedQuantity;
                            }

                            public void setMaxAllowedQuantity(String maxAllowedQuantity) {
                                this.maxAllowedQuantity = maxAllowedQuantity;
                            }

                            public String getMinAllowedQuantity() {
                                return minAllowedQuantity;
                            }

                            public void setMinAllowedQuantity(String minAllowedQuantity) {
                                this.minAllowedQuantity = minAllowedQuantity;
                            }

                            public String getModifierId() {
                                return modifierId;
                            }

                            public void setModifierId(String modifierId) {
                                this.modifierId = modifierId;
                            }

                            public String getModifierName() {
                                return modifierName;
                            }

                            public void setModifierName(String modifierName) {
                                this.modifierName = modifierName;
                            }

                            public String getModifierType() {
                                return modifierType;
                            }

                            public void setModifierType(String modifierType) {
                                this.modifierType = modifierType;
                            }

                            public List<SizeModifierProductsBean> getSizeModifierProducts() {
                                return sizeModifierProducts;
                            }

                            public void setSizeModifierProducts(List<SizeModifierProductsBean> sizeModifierProducts) {
                                this.sizeModifierProducts = sizeModifierProducts;
                            }

                            public static class SizeModifierProductsBean {
                                /**
                                 * modifierProductPrice : 0.00
                                 * amount : 0
                                 * productId : cd338f94-4e64-11ea-a62e-0657952ed75a
                                 * productName : No Cheese
                                 * unit : Item
                                 * quantity : 7
                                 * originalQuantity : 7
                                 * originalAmount : 0
                                 * originalAmount1 : 0
                                 */

                                private String modifierProductPrice;
                                private int amount;
                                private String productId;
                                private String productName;
                                private String unit;
                                private String quantity;
                                private String originalQuantity;
                                private int originalAmount;
                                private int originalAmount1;

                                public String getModifierProductPrice() {
                                    return modifierProductPrice;
                                }

                                public void setModifierProductPrice(String modifierProductPrice) {
                                    this.modifierProductPrice = modifierProductPrice;
                                }

                                public int getAmount() {
                                    return amount;
                                }

                                public void setAmount(int amount) {
                                    this.amount = amount;
                                }

                                public String getProductId() {
                                    return productId;
                                }

                                public void setProductId(String productId) {
                                    this.productId = productId;
                                }

                                public String getProductName() {
                                    return productName;
                                }

                                public void setProductName(String productName) {
                                    this.productName = productName;
                                }

                                public String getUnit() {
                                    return unit;
                                }

                                public void setUnit(String unit) {
                                    this.unit = unit;
                                }

                                public String getQuantity() {
                                    return quantity;
                                }

                                public void setQuantity(String quantity) {
                                    this.quantity = quantity;
                                }

                                public String getOriginalQuantity() {
                                    return originalQuantity;
                                }

                                public void setOriginalQuantity(String originalQuantity) {
                                    this.originalQuantity = originalQuantity;
                                }

                                public int getOriginalAmount() {
                                    return originalAmount;
                                }

                                public void setOriginalAmount(int originalAmount) {
                                    this.originalAmount = originalAmount;
                                }

                                public int getOriginalAmount1() {
                                    return originalAmount1;
                                }

                                public void setOriginalAmount1(int originalAmount1) {
                                    this.originalAmount1 = originalAmount1;
                                }
                            }
                        }
                    }
                }
            }
        }

        public static class DeliveryAddressBean {
            /**
             * customer_name : Dheeraj Pandey
             * phone_number : 8810615248
             * email : dhiraj.omnisttechhub@gmail.com
             * customer_address_id :
             * customer_location :
             * address_1 :
             * address_2 :
             * city :
             * post_code :
             * country :
             * address_type :
             */

            private String customer_name;
            private String phone_number;
            private String email;
            private String customer_address_id;
            private String customer_location;
            private String address_1;
            private String address_2;
            private String city;
            private String post_code;
            private String country;
            private String address_type;

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getPhone_number() {
                return phone_number;
            }

            public void setPhone_number(String phone_number) {
                this.phone_number = phone_number;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getCustomer_address_id() {
                return customer_address_id;
            }

            public void setCustomer_address_id(String customer_address_id) {
                this.customer_address_id = customer_address_id;
            }

            public String getCustomer_location() {
                return customer_location;
            }

            public void setCustomer_location(String customer_location) {
                this.customer_location = customer_location;
            }

            public String getAddress_1() {
                return address_1;
            }

            public void setAddress_1(String address_1) {
                this.address_1 = address_1;
            }

            public String getAddress_2() {
                return address_2;
            }

            public void setAddress_2(String address_2) {
                this.address_2 = address_2;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getPost_code() {
                return post_code;
            }

            public void setPost_code(String post_code) {
                this.post_code = post_code;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getAddress_type() {
                return address_type;
            }

            public void setAddress_type(String address_type) {
                this.address_type = address_type;
            }
        }
    }

    public static class ErrorsBean {
    }
}
