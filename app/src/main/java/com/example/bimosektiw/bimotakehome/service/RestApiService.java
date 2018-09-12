package com.example.bimosektiw.bimotakehome.service;

import com.example.bimosektiw.bimotakehome.model.UserList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("/search/users")
    Call<UserList> getUserList(@Query("q") String filter, @Query("page") int page, @Query("per_page") int perPage);
}
