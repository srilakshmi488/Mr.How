package com.volive.mrhow.util;

import android.database.Observable;

import com.google.gson.JsonElement;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.http.Field;
        import retrofit2.http.FormUrlEncoded;
        import retrofit2.http.GET;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiClass {

    @GET("welcome_video?")
    Call<JsonElement> welcomevideo(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("home_screens?")
    Call<JsonElement> homescreens(@Query("api_key") String apikey, @Query("lang") String lang);

    @FormUrlEncoded
    @POST("registration")
    Call<JsonElement> registration(@Field("name") String name, @Field("email") String email, @Field("password") String password,
                                   @Field("device_name") String devicename, @Field("device_token") String devicetoken,
                                   @Field("agree_tc") String checked, @Field("lang") String language, @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("login")
    Call<JsonElement> login(@Field("email") String email, @Field("password") String password,
                            @Field("device_name") String devicename, @Field("device_token") String devicetoken,
                            @Field("lang") String lang, @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("social_login")
    Call<JsonElement> sociallogin(@Field("name") String name, @Field("email") String email,
                                  @Field("device_name") String devicename, @Field("device_token") String devicetoken,
                                  @Field("login_type") String logintype, @Field("unique_id") String uniqueid,
                                  @Field("lang") String lang, @Field("api_key") String apikey);


    @GET("discover?")
    Call<JsonElement> discover(@Query("api_key") String apikey, @Query("lang") String lang);


    @GET("discover_courses?")
    Call<JsonElement> discovercourses(@Query("api_key") String apikey, @Query("lang") String lang);

    @FormUrlEncoded
    @POST("courses_list")
    Call<JsonElement> courseslistfilter(@Field("category_id") String catid, @Field("sub_category_id") String subcatid,
                                        @Field("price_from") String pricefrom, @Field("price_to") String priceto,
                                        @Field("ratings") String ratings, @Field("sort_by") String sortby,
                                        @Field("lang") String lang, @Field("api_key") String apikey);


    @GET("categories_list?")
    Call<JsonElement> categorieslist(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("sub_categories_list?")
    Call<JsonElement> subcatagirieslist(@Query("category_id") String catid, @Query("api_key") String apikey,
                                        @Query("lang") String lang);

    @GET("terms_conditions?")
    Call<JsonElement> termsandconditions(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("privacy_policy?")
    Call<JsonElement> privacypolicy(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("about_us?")
    Call<JsonElement> aboutus(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("contact_email?")
    Call<JsonElement> contactemail(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("faqs?")
    Call<JsonElement> faqs(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("price_range?")
    Call<JsonElement> pricerange(@Query("api_key") String apikey, @Query("lang") String lang);

    @GET("course_details?")
    Call<JsonElement> coursedetails(@Query("course_id") String courseid, @Query("user_id") String userid,
                                    @Query("lang") String lang, @Query("api_key") String apikey);

    @GET("notifications?")
    Call<JsonElement> notifications(@Query("user_id") String userid, @Query("lang") String lang,
                                    @Query("api_key") String apikey);

    @GET("articles_list?")
    Call<JsonElement> articleslist(@Query("user_id") String userid, @Query("lang") String lang,
                                   @Query("api_key") String apikey,@Query("hashtag") String hashtag);

    @GET("user_profile?")
    Call<JsonElement> userprofile(@Query("user_id") String userid, @Query("lang") String lang,
                                  @Query("api_key") String apikey);

    @Multipart
    @POST("update_profile_pic")
    Call<JsonElement> updateprofile(@Part("user_id") RequestBody userid, @Part("api_key") RequestBody apikey,
                                    @Part("lang") RequestBody lang, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<JsonElement> editprofile(@Field("user_id") String userid, @Field("email") String email, @Field("phone") String phone,
                                  @Field("gender") String gender, @Field("lang") String lang, @Field("api_key") String apikey);


    @GET("article_details?")
    Call<JsonElement> articledetails(@Query("user_id") String userid, @Query("article_id") String articleid,
                                     @Query("lang") String lang, @Query("api_key") String apikey);

    @FormUrlEncoded
    @POST("article_comment")
    Call<JsonElement> articlecomment(@Field("user_id") String userid, @Field("article_id") String articleid,
                                     @Field("main_comment") String comment, @Field("lang") String lang,
                                     @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("article_sub_comment")
    Call<JsonElement> articlesubcomment(@Field("user_id") String userid, @Field("article_comment_id") String articlecommentid,
                                        @Field("sub_comment") String subcomment, @Field("lang") String lang, @Field("api_key") String apikey);


    @GET("my_courses?")
    Call<JsonElement> mycourses(@Query("user_id") String userid, @Query("lang") String lang, @Query("api_key") String apikey);


    @FormUrlEncoded
    @POST("add_to_wishlist")
    Call<JsonElement> addtowishlist(@Field("user_id") String userid, @Field("course_id") String courseid,
                                    @Field("lang") String lang, @Field("api_key") String apikey);

    @GET("trainer_profile?")
    Call<JsonElement> trainerprofile(@Query("user_id") String userid, @Query("lang") String lang, @Query("api_key") String apikey);

    @FormUrlEncoded
    @POST("follow_trainer")
    Call<JsonElement> followtrainer(@Field("user_id") String userid, @Field("author_id") String authorid,
                                    @Field("lang") String lang, @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("article_comment_like")
    Call<JsonElement> articlecommentlike(@Field("user_id") String userid, @Field("article_comment_id") String articlecommentid,
                                         @Field("lang") String lang, @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<JsonElement> forgotpassword(@Field("email") String email, @Field("lang") String lang, @Field("api_key") String apikey);


    @Multipart
    @POST("upload_projects")
    Call<JsonElement> uploadprojects(@Part("user_id") RequestBody userid, @Part("course_id") RequestBody courseid,
                                     @Part("api_key") RequestBody apikey, @Part("lang") RequestBody lang, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("make_payment")
    Call<JsonElement> makepayment(@Field("user_id") String userid, @Field("course_id") String courseid,
                                  @Field("price") String price, @Field("amount_paid") String amount,
                                  @Field("payment_method") String paymentmethod, @Field("lang") String lang,
                                  @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("submit_course_review")
    Call<JsonElement> submitcoursereview(@Field("user_id") String userid, @Field("course_id") String courseid,
                                         @Field("comment") String comment, @Field("comment_rating") String commentrating,
                                         @Field("lang") String lang, @Field("api_key") String apikey);


    @GET("view_lesson_video?")
    Call<JsonElement> viewlessonvideo(@Query("user_id") String userid, @Query("lesson_id") String lessonid,
                                      @Query("course_id") String courseid, @Query("lang") String lang, @Query("api_key") String apikey);

    @GET("course_certificate?")
    Call<JsonElement> coursecertificate(@Query("user_id") String userid, @Query("course_id") String courseid,
                                        @Query("lang") String lang, @Query("api_key") String apikey);

    @FormUrlEncoded
    @POST("update_notification_settings")
    Call<JsonElement> updatenotificationsettings(@Field("user_id") String user_id, @Field("trainer_updates") String trainerupdates,
                                                 @Field("recommendations") String recommendations, @Field("lang") String lang,
                                                 @Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("logout")
    Call<JsonElement>logout(@Field("user_id") String userid,@Field("lang") String lang,@Field("api_key") String apikey);


    @FormUrlEncoded
    @POST("search")
    Call<JsonElement>search(@Field("search") String search,@Field("lang") String lang,@Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("article_like")
    Call<JsonElement>articlelike(@Field("user_id") String userid,@Field("article_id") String articleid,@Field("lang") String lang,
                                 @Field("api_key") String apikey);

    @GET("hashtags?")
    Call<JsonElement>hashtags(@Query("lang") String lang,@Query("api_key") String apikey);

    @GET("downloads?")
    Call<JsonElement>downloads(@Query("user_id") String userid,@Query("lang") String lang,@Query("api_key") String apikey);

    @FormUrlEncoded
    @POST("add_to_downloads")
    Call<JsonElement>addtodownloads(@Field("user_id") String userid,@Field("course_id") String courseid,
                                    @Field("lang") String lang,@Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("remove_downloads")
    Call<JsonElement>removedownloads(@Field("user_id") String userid,@Field("course_id") String courseid,
                                     @Field("lang") String lang,@Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("usersettings")
    Call<JsonElement>usersettings(@Field("user_id") String userid,@Field("background_video") String backgroundvideo,
                                  @Field("wifi_only") String wifionly,@Field("video_quality") String videoquality,@Field("api_key") String apikey,
                                  @Field("lang") String lang);

    @FormUrlEncoded
    @POST("validpromocode")
    Call<JsonElement>validpromocode(@Field("promocode") String promocode,@Field("api_key") String apikey,
                                    @Field("lang") String lang);

    @GET("promocodes")
    Call<JsonElement>promocodes(@Query("lang") String lang,@Query("api_key") String apikey);


}
