package com.tuftstudios.kershoadmin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @FormUrlEncoded
    @POST("admin_login")
    Call<Login> userLogin(
            @Field("user") String user,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("get_current_orders")
    Call<KitchenOrders> getCurOrders(
            @Field("kitchen") int kitchen
    );

    /*@POST("get_all_current_orders")
    Call<KitchenOrders> getAllCurOrders();*/

    @FormUrlEncoded
    @POST("get_old_orders")
    Call<KitchenOrders> getOldOrders(
            @Field("kitchen") int kitchen
    );

    /*@POST("get_all_old_orders")
    Call<KitchenOrders> getAllOldOrders(
    );*/

    @FormUrlEncoded
    @POST("get_order_dishes")
    Call<DishesItem> getDishesItem(
            @Field("order_id") int orderId
    );



    @FormUrlEncoded
    @POST("update_order_status")
    Call<Result> updateOrderStatus(
            @Field("id") int id,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("admin_set_order_ready")
    Call<Result> setOrderReady(
            @Field("id") int id
    );

    @GET("get_kitchen_users")
    Call<Users> getKitchens();

    @FormUrlEncoded
    @POST("add_user_promocode")
    Call<Result> addUserPromoCode(
            @Field("user_id") int id,
            @Field("promocode") String promocode,
            @Field("discount") String discount,
            @Field("expire") String expire);

    @FormUrlEncoded
    @POST("add_promocode")
    Call<Result> addPromoCode(
            @Field("promocode") String promocode,
            @Field("discount") String discount,
            @Field("expire") String expire,
            @Field("type") int promocodeType
    );

    @PUT("update_kitchen_password/{id}/{password}")
    Call<Result> updateKitchenPassword(
            @Path("id") int id,
            @Path("password") String password
    );

    @PUT("update_kitchen_status/{user_id}/{available}")
    Call<Result> updateKitchenStatus(
            @Path("user_id") int user_id,
            @Path("available") int available
    );

    @GET("get_kitchen_status/{user_id}")
    Call<StatusResult> getKitchenStatus(
            @Path("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("save_admin_token")
    Call<Result> saveToken(
            @Field("token") String token,
            @Field("user_id") int userId
    );




    /*get kitchen status
    URL /get_kitchen_status/{user_id}
    Method  GET
    the following json response will be issued.

    {
        "error": false,
            "availability": 1

    }
*/



    /*28-Update kitchen status
    URL /update_kitchen_status/{user_id}/{avilable}
    Method  PUT
    the following json response will be issued.

    {
        "error": false,
            "message": "status updated successfully"

    }
    Or
    {
        "error": true,
            "message": "Some error occurred"
    }*/


/*    @FormUrlEncoded
    @POST("get_menu")
    Call<OrdersItem> getOrders(
            @Field("type") String type,
            @Field("location") String location
    );*/


 /*
    @GET("posts")
    Call <List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call <List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

/////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

/////////////////////////////////////////////////////////////////////////////////////////////////////


*/
}
