package com.easyfoodvone.api_handler;

public class ApiConstants {
    //public static final String BASE_URL = "https://admin.easyfood.co.uk/api/v1_app/v1/";
    //public static final String BASE_URL = "https://admin.easyfood.co.uk/api/v1_app/v1/";
    //public static final String BASE_URL = "http://35.177.163.219/mcc-easyfood-backend/public/api/v1_app/v1/";
    public static final String BASE_URL = "https://admin.easyfood.co.uk/api/v1_app/v1_1/";


    /*    public static final String BASE_URL = "http://35.177.163.219/mcc-easyfood-backend/public/api/v1_app/v1/";*/
    public static final String LOGIN = "login";
    public static final String LOGOUT = "user_logout";
    public static final String CONTACT_SUPPORT = "supports";
    public static final String ALL_ORDERS = "order_list";
    public static final String ALL_ORDERS_NEW = "order_details_new";
    public static final String FORGOT_PASSWORD = "forget_password_web";
    public static final String RESTAURANT_OPEN_CLOSE = "restaurant_open_close";
    public static final String HOME_DELIVERY_OPTION = "restaurant_delivery_option";
    public static final String GET_ORDER_DETAILS = "order_details";
    public static final String ORDER_ACCEPT_REJECT = "order_accept_reject";
    public static final String ORDER_STATUS = "updateOrderStatus";
    public static final String SEND_EMAIL = "send_email_report";
    public static final String GET_ALL_MENU_CATEGORIES = "menu_category";
    public static final String GET_MENU_CATEGORY_ITEM = "menu_category_items";
    public static final String SET_MENU_CATEGORY_ITEM_POSITION = "menu_product_position";
    public static final String ACTIVE_DEACTIVE_MENU_PRODUCT = "active_deactive_menu_product";
    public static final String ACTIVE_DEACTIVE_MENU = "active_deactive_menu_category";
    public static final String UPDAET_MENU_CATEGORY = "update_category";
    public static final String UPDATE_MENU_PRODUCT = "update_menu_product";
    public static final String GET_DELEVERY_SETTING = "get_delivery_setting";

    public static final String GET_DELEVERY_POST_CODE = "getDeliveryPostcodesAreasList";
    public static final String UPDATE_POSTCODE_DELIVERY_TIME = "update_postcode_delivery_time";

    public static final String UPDATE_POSTCODE_DELIVERY = "update_delivery_postcode";
    public static final String UPDATE_ALL_POSTCODE_DELIVERY_TIME = "update_all_postcode_delivery_time";
    public static final String DELETE_POSTCODE_DELIVERY_TIME = "delete_postcode_delivery_time";

    public static final String DELETE_POSTCODE_DELIVERY = "del_delivery_postcode";
    public static final String DELIVERY_SETTING = "delivery_settings";
    public static final String OFFER_X_SPEND_X_DISCOUNT = "add_spend_x_offer";
    public static final String GET_RESTAURANT_TIMING = "get_restaurant_timing";
    public static final String ADD_NEW_TIMING = "add_restaurant_timeing";
    public static final String DELETE_TIMING = "delete_restaurant_timeing";
    public static final String GET_REVENUE_REPORT_BY_DATE = "get_revenue_report_by_date";
    public static final String GET_REVENUE_REPORT_BETWEEN_DATE = "get_revenue_report_between_date";
    public static final String GET_REVENUE_REPORT = "get_revenue_report";
    public static final String UPDATE_TIMING = "update_restaurant_timeing";
    public static final String GET_ORDER_REPORT_BY_DATE = "order_report_by_date";
    public static final String GET_ORDER_REPORT_BETWEEN_DATE = "order_report_between_date";
    public static final String GET_ORDER_REPORT = "order_report";
    public static final String CHANGE_PASSWORD = "change-password";
    public static final String GET_RATINGS = "get_review_rating";
    public static final String THANK_YOU = "thank-you";
    public static final String REPLY = "send-message";
    public static final String GET_PRODUCTS_LIST = "get_menu_products";
    public static final String CHARITY_DETAIL_URL = "get_charitydetails";
    public static final String DONATE_MEAL_URL = "savecharitymeal";
    public static final String UPDATE_MEAL_REQUEST_URL = "updateMealReqeustStatus";
    public static final String ORDER_DETAIL_URL = "order_details_new";
    public static final String CREATE_OFFER_WITH_PERCENTAGE = "add_discount_percentage_offer";
    public static final String UPDATE_OFFER_WITH_PERCENTAGE = "edit_discount_percentage_offer";
    public static final String CREATE_OFFER_WITH_AMT = "add_flat_discount_offer";
    public static final String UPDATE_OFFER_WITH_AMT = "edit_flat_discount_offer";
    public static final String CREATE_COMBO_OFFER = "add_combo_offer";
    public static final String GET_OFFERS = "get_offers";
    public static final String DELETE_OFFER = "delete_offer";
    public static final String DEACTIVATE_OFFER = "update_offer_status";
    public static final String GET_SERVE_STYLE = "get_serve_style";
    public static final String SET_SERVE_STYLE = "set_serve_style";
    public static final String GET_MENU_ITEM_DETAILS = "get_menu_product";
    public static final String UPDATE_MENU_ITEM_DETAILS = "save_menu_product";
    public static final String GET_RESTAURANT_DELIVERY_SLOTS = "get_restaurant_delivery_slots";
    public static final String get_restaurant_closing_time_by_date = "get_restaurant_closing_time_by_date\n";


    //API's BY AJIT
    public static final String CHANGE_MENU_POSITION = "menu_category_position_update";
    public static final String CHANGE_ITEM_POSITION = "menu_product_position_updated";
    public static final String ONOFFDAY = "closeOpenDay";
    public static final String SAME_POSTCODE_FOR_ALL = "copy_delivery_setting";

}