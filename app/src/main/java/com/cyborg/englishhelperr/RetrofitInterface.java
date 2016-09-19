package com.cyborg.englishhelperr;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<Object> translate(@FieldMap Map<String, String> map);
}
