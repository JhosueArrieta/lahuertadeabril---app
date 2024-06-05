package com.example.lahuertadeabril;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("v1/info_product1/{product_id}/")
    Call<Product1Response> getProduct1Info(@Path("product_id") int product1Id);
    @GET("v1/info_product2/{product_id}/")
    Call<Product2Response> getProduct2Info(@Path("product_id") int product2Id);
}
