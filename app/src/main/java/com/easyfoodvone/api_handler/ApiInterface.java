package com.easyfoodvone.api_handler;

import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.app_common.ws.NewDetailBean;
import com.easyfoodvone.app_common.ws.OrdersListResponse;
import com.easyfoodvone.charity.webservice.responsebean.CharityInfoBean;
import com.easyfoodvone.charity.webservice.responsebean.CommonResponseBean;
import com.google.gson.JsonObject;
import com.easyfoodvone.contact_support.models.SupportRequest;
import com.easyfoodvone.contact_support.models.SupportResponse;
import com.easyfoodvone.models.LoginRequest;
import com.easyfoodvone.models.LoginResponse;
import com.easyfoodvone.models.CommonRequest;
import com.easyfoodvone.app_common.ws.MenuCategoryItemsResponse;
import com.easyfoodvone.models.MenuProductDetails;
import com.easyfoodvone.models.AddNewTimingRequest;
import com.easyfoodvone.app_common.ws.AllDaysRestaurantTiming;
import com.easyfoodvone.models.ChangePasswordRequest;
import com.easyfoodvone.models.CreateComboOfferRequest;
import com.easyfoodvone.models.DeletePostCodeDeliveryTimeRequest;
import com.easyfoodvone.models.DeleverySetting;
import com.easyfoodvone.models.DeliverySettingRequest;
import com.easyfoodvone.models.DiscountWithPercentageRequest;
import com.easyfoodvone.models.MenuProducts;
import com.easyfoodvone.app_common.ws.OffersResponse;
import com.easyfoodvone.models.OrderReportRequest;
import com.easyfoodvone.app_common.ws.OrderReportResponse;
import com.easyfoodvone.models.RatingRequest;
import com.easyfoodvone.models.RatingResponse;
import com.easyfoodvone.models.RestaurantClosingTimeByDataModel;
import com.easyfoodvone.models.RevenueReportRequest;
import com.easyfoodvone.models.RevenueReportResponse;
import com.easyfoodvone.models.ServeStyleResponse;
import com.easyfoodvone.models.SetServeStyleRequest;
import com.easyfoodvone.models.UpdateMenuCategoryRequest;
import com.easyfoodvone.models.UpdateMenuProductRequest;
import com.easyfoodvone.models.UpdatePostCodeDeliveryTimeRequest;
import com.easyfoodvone.models.menu_response.OrdersDetailsResponseNew;
import com.easyfoodvone.new_order.models.AcceptRejectOrderRequest;
import com.easyfoodvone.new_order.models.DeletePostCodeDeliveryTimeResponse;
import com.easyfoodvone.new_order.models.DeleverySettingResponse;
import com.easyfoodvone.app_common.ws.DeliveryPostCodeBean;
import com.easyfoodvone.new_order.models.DeliverySettingResponse;
import com.easyfoodvone.new_order.models.OrderDetailsRequest;
import com.easyfoodvone.new_order.models.OrderDetailsResponse;
import com.easyfoodvone.new_order.models.TimeSlotRequest;
import com.easyfoodvone.new_order.models.TimeSlotResponse;
import com.easyfoodvone.new_order.models.UpdatePostCodeDeliveryTimeResponse;
import com.easyfoodvone.models.OrdersRequest;
import com.easyfoodvone.models.RestaurantOpenCloseRequest;
import com.easyfoodvone.models.RestaurantOpenCloseResponse;
import com.easyfoodvone.models.SpendXgetXdiscountRequest;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST(ApiConstants.LOGIN)
    Single<LoginResponse> login(@Body LoginRequest request);

    @POST(ApiConstants.LOGOUT)
    Single<CommonResponse> logout(@Header("Authorization") String Authorization, @Body CommonRequest request);

    @POST(ApiConstants.RESTAURANT_OPEN_CLOSE)
    Single<RestaurantOpenCloseResponse> openCloseRestaurant(@Header("Authorization") String Authorization,@Body RestaurantOpenCloseRequest request);

    @POST(ApiConstants.HOME_DELIVERY_OPTION)
    Single<RestaurantOpenCloseResponse> openCloseHomeDelivery(@Header("Authorization") String Authorization,@Body RestaurantOpenCloseRequest request);

    @POST(ApiConstants.SET_SERVE_STYLE)
    Single<ServeStyleResponse> setServeStyle(@Header("Authorization") String Authorization,@Body SetServeStyleRequest request);

    @POST(ApiConstants.GET_SERVE_STYLE)
    Single<ServeStyleResponse> getServerStyle(@Header("Authorization") String Authorization,@Body SetServeStyleRequest request);

    @POST(ApiConstants.CONTACT_SUPPORT)
    Single<SupportResponse> contactSupport(@Header("Authorization") String Authorization,@Body SupportRequest request);

    @POST(ApiConstants.ALL_ORDERS)
    Single<OrdersListResponse> getOrders(@Header("Authorization") String Authorization, @Body OrdersRequest request);

    @POST(ApiConstants.ALL_ORDERS_NEW)
    Single<OrdersDetailsResponseNew> orderDetailsNew(@Header("Authorization") String Authorization,@Body OrderDetailsRequest request);

   /* @POST(ApiConstants.ALL_ORDERS_NEW)
    Single<NewOrdersDetailsResponse> orderDetailsNeww(@Body OrderDetailsRequest request);
*/

    @POST(ApiConstants.GET_ORDER_DETAILS)
    Single<OrderDetailsResponse> getOrderDetails(@Header("Authorization") String Authorization,@Body OrderDetailsRequest request);

    @POST(ApiConstants.FORGOT_PASSWORD)
    Single<LoginResponse> forgotPassword(@Header("Authorization") String Authorization,@Body LoginRequest request);

    @POST(ApiConstants.ORDER_ACCEPT_REJECT)
    Single<CommonResponse> acceptRejectOrders(@Header("Authorization") String Authorization,@Body AcceptRejectOrderRequest request);


    @POST(ApiConstants.ORDER_STATUS)
    Single<CommonResponse> orderStatus(@Header("Authorization") String Authorization,@Body AcceptRejectOrderRequest request);


    @POST(ApiConstants.SEND_EMAIL)
    Single<CommonResponse> sendEmail(@Header("Authorization") String Authorization,@Body AcceptRejectOrderRequest request);

    @POST(ApiConstants.GET_ALL_MENU_CATEGORIES)
    Single<MenuCategoryList> getMenuCategories(@Header("Authorization") String Authorization, @Body CommonRequest request);

    @POST(ApiConstants.GET_MENU_CATEGORY_ITEM)
    Single<MenuCategoryItemsResponse> getMenuCategoryItems(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.SET_MENU_CATEGORY_ITEM_POSITION)
    Single<MenuCategoryItemsResponse> getMenuCategoryItemsPositin(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.ACTIVE_DEACTIVE_MENU_PRODUCT)
    Single<MenuCategoryItemsResponse> activeDeactiveMenuProduct(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.ACTIVE_DEACTIVE_MENU)
    Single<CommonResponse> activeDeactiveMenu(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.UPDAET_MENU_CATEGORY)
    Single<CommonResponse> updateMenuCategory(@Header("Authorization") String Authorization,@Body UpdateMenuCategoryRequest request);

    @POST(ApiConstants.UPDATE_MENU_PRODUCT)
    Single<CommonResponse> updateMenuProduct(@Header("Authorization") String Authorization,@Body UpdateMenuProductRequest request);

    @POST(ApiConstants.GET_DELEVERY_SETTING)
    Single<DeleverySettingResponse> getDeleverySetting(@Header("Authorization") String Authorization,@Body DeleverySetting deleverySetting);


    @POST(ApiConstants.GET_DELEVERY_POST_CODE)
    Call<DeliveryPostCodeBean> getDeliveryPostCode(@Header("Authorization") String Authorization,@Body JsonObject jsonObject);

    @POST(ApiConstants.UPDATE_POSTCODE_DELIVERY_TIME)
    Single<UpdatePostCodeDeliveryTimeResponse> updatePostCodeDeliveryTime(@Header("Authorization") String Authorization,@Body UpdatePostCodeDeliveryTimeRequest updateMenuCategoryRequest);

    @POST(ApiConstants.UPDATE_POSTCODE_DELIVERY)
    Call<CommonResponse> updatePostCodeDelivery(@Header("Authorization") String Authorization,@Body JsonObject jsonObject);

    @POST(ApiConstants.DELETE_POSTCODE_DELIVERY)
    Call<CommonResponse> deletDeliveryPostCode(@Header("Authorization") String Authorization,@Body JsonObject jsonObject);

    @POST(ApiConstants.DELETE_POSTCODE_DELIVERY_TIME)
    Single<DeletePostCodeDeliveryTimeResponse> deletePostCodeDeliveryTime(@Header("Authorization") String Authorization,@Body DeletePostCodeDeliveryTimeRequest deletePostCodeDeliveryTimeRequest);

    @POST(ApiConstants.OFFER_X_SPEND_X_DISCOUNT)
    Single<CommonResponse> createSpendXGetXDiscount(@Header("Authorization") String Authorization,@Body SpendXgetXdiscountRequest request);

    @POST(ApiConstants.GET_RESTAURANT_TIMING)
    Single<AllDaysRestaurantTiming> getRestaurantTiming(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.ADD_NEW_TIMING)
    Single<CommonResponse> addNewTiming(@Header("Authorization") String Authorization,@Body AddNewTimingRequest request);

    @POST(ApiConstants.UPDATE_ALL_POSTCODE_DELIVERY_TIME)
    Single<UpdatePostCodeDeliveryTimeResponse> updatAllPostCodeDelivery(@Header("Authorization") String Authorization,@Body UpdatePostCodeDeliveryTimeRequest updatallCategoryRequest);

    @POST(ApiConstants.DELIVERY_SETTING)
    Single<DeliverySettingResponse> deliverySettingResponse(@Header("Authorization") String Authorization,@Body DeliverySettingRequest deliverySettingRequest);

    @POST(ApiConstants.DELETE_TIMING)
    Single<CommonResponse> deleteTiming(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.UPDATE_TIMING)
    Single<CommonResponse> updateTiming(@Header("Authorization") String Authorization,@Body AddNewTimingRequest request);

    @POST(ApiConstants.GET_REVENUE_REPORT)
    Single<RevenueReportResponse> getRevenueReport(@Header("Authorization") String Authorization,@Body RevenueReportRequest request);

    @POST(ApiConstants.GET_REVENUE_REPORT_BY_DATE)
    Single<RevenueReportResponse> getRevenueReportByDate(@Header("Authorization") String Authorization,@Body RevenueReportRequest request);

    @POST(ApiConstants.GET_REVENUE_REPORT_BETWEEN_DATE)
    Single<RevenueReportResponse> getRevenueReportBetweenDate(@Header("Authorization") String Authorization,@Body RevenueReportRequest request);

    @POST(ApiConstants.GET_ORDER_REPORT)
    Single<OrderReportResponse> getOrderReport(@Header("Authorization") String Authorization,@Body OrderReportRequest request);

    @POST(ApiConstants.GET_ORDER_REPORT_BY_DATE)
    Single<OrderReportResponse> getOrderReportByDate(@Header("Authorization") String Authorization,@Body OrderReportRequest request);

    @POST(ApiConstants.GET_ORDER_REPORT_BETWEEN_DATE)
    Single<OrderReportResponse> getOrderReportBetweenDate(@Header("Authorization") String Authorization,@Body OrderReportRequest request);

    @POST(ApiConstants.CHANGE_PASSWORD)
    Single<CommonResponse> changePassword(@Header("Authorization") String Authorization,@Body ChangePasswordRequest request);

    @POST(ApiConstants.THANK_YOU)
    Single<CommonResponse> sendThankyou(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.REPLY)
    Single<CommonResponse> reply(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.GET_RATINGS)
    Single<RatingResponse> getRatings(@Header("Authorization") String Authorization,@Body RatingRequest request);

    @POST(ApiConstants.GET_PRODUCTS_LIST)
    Single<MenuProducts> getProducts(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.CHARITY_DETAIL_URL)
    Call<CharityInfoBean> getCharityDetail(@Header("Authorization") String Authorization, @Body JsonObject jsonObject);

    @POST(ApiConstants.DONATE_MEAL_URL)
    Call<CommonResponseBean> donateMeal(@Header("Authorization") String Authorization, @Body JsonObject jsonObject);

    @POST(ApiConstants.UPDATE_MEAL_REQUEST_URL)
    Call<CommonResponseBean> updateMealStatus(@Header("Authorization") String Authorization,@Body JsonObject jsonObject);

    @POST(ApiConstants.ORDER_DETAIL_URL)
    Call<NewDetailBean> getOrderDetail(@Header("Authorization") String Authorization, @Body JsonObject jsonObject);

    @POST(ApiConstants.CREATE_OFFER_WITH_PERCENTAGE)
    Single<CommonResponse> createOfferWithPercentage(@Header("Authorization") String Authorization,@Body DiscountWithPercentageRequest request);

    @POST(ApiConstants.UPDATE_OFFER_WITH_PERCENTAGE)
    Single<CommonResponse> updateOfferWithPercentage(@Header("Authorization") String Authorization,@Body DiscountWithPercentageRequest request);

    @POST(ApiConstants.CREATE_COMBO_OFFER)
    Single<CommonResponse> createComboOffer(@Header("Authorization") String Authorization,@Body CreateComboOfferRequest request);

    @POST(ApiConstants.CREATE_OFFER_WITH_AMT)
    Single<CommonResponse> createOfferWithAmt(@Header("Authorization") String Authorization,@Body DiscountWithPercentageRequest request);

    @POST(ApiConstants.UPDATE_OFFER_WITH_AMT)
    Single<CommonResponse> updateOfferWithAmt(@Header("Authorization") String Authorization,@Body DiscountWithPercentageRequest request);

    @POST(ApiConstants.GET_OFFERS)
    Single<OffersResponse> getOffers(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.DELETE_OFFER)
    Single<CommonResponse> deleteOffer(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.DEACTIVATE_OFFER)
    Single<CommonResponse> deactivateOffer(@Header("Authorization") String Authorization,@Body JsonObject jsonObject);

    @POST(ApiConstants.GET_MENU_ITEM_DETAILS)
    Single<MenuProductDetails> getMenuDetails(@Header("Authorization") String Authorization,@Body CommonRequest request);

    @POST(ApiConstants.UPDATE_MENU_ITEM_DETAILS)
    Single<CommonResponse> saveMenuDetails(@Header("Authorization") String Authorization,@Body MenuProductDetails request);

    @POST(ApiConstants.GET_RESTAURANT_DELIVERY_SLOTS)
    Single<TimeSlotResponse> getDeliveryTimeSlot(@Header("Authorization") String Authorization,@Body TimeSlotRequest request);

    @POST(ApiConstants.get_restaurant_closing_time_by_date)
    Single<RestaurantClosingTimeByDataModel> getRestaurantClosingTimeByDate(@Header("Authorization") String Authorization,@Body TimeSlotRequest request);


}