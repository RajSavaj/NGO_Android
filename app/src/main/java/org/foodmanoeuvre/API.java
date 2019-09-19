package org.foodmanoeuvre;

import org.foodmanoeuvre.modal.FoodHistory;
import org.foodmanoeuvre.modal.FoodItem;
import org.foodmanoeuvre.modal.Restorent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface API {
    String BASE_URL = "http://jaishreekrishna.me/NgoFood/public/api/";// http://192.160.11.8:8080/api/v1/
    String IMG_URL="http://jaishreekrishna.me/NgoFood/public/image/";

    @Multipart
    @POST("register")
    Call<String> Register(@Part MultipartBody.Part imgp, @PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("login")
    Call<String> Login(@FieldMap HashMap<String, String> maps);

    @Multipart
    @POST("addFood")
    Call<String> addFood(@Part MultipartBody.Part imgp, @PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("getTodayFood")
    Call<List<FoodItem>> getTodayFood(@Field("R_id") String search);

    @FormUrlEncoded
    @POST("deleteItem")
    Call<String> deleteItem(@Field("Id") String rid);

    @FormUrlEncoded
    @POST("updateQty")
    Call<String> updateqty(@Field("Id") String rid,@Field("Qty") String uqty);

    @FormUrlEncoded
        @POST("getRestaurants")
    Call<List<Restorent>> getRestaurants(@Field("Id") String lat,@Field("Qty") String lang);

    @FormUrlEncoded
    @POST("todayItemDetail")
    Call<List<FoodItem>> todayItemDetail(@Field("R_id") String rid);

    @FormUrlEncoded
    @POST("addOrderItem")
    Call<String> confirmorder(@Field("R_id") String rid,@Field("N_id") String nid,@Field("Qty[]") String[] qtys,@Field("Name[]") String[] Name,@Field("I_id[]") String[] I_id);

    @FormUrlEncoded
    @POST("restoHistory")
    Call<List<FoodHistory>> restoHistory(@Field("R_id") String rid);

    @FormUrlEncoded
    @POST("acceptItem")
    Call<String> acceptItem(@Field("Id") String rid,@Field("Status") String status);

    @FormUrlEncoded
    @POST("ngoRequestStatus")
    Call<List<FoodHistory>> ngoRequestStatus(@Field("N_id") String rid);
}
