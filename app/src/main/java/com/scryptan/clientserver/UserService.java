package com.scryptan.clientserver;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface UserService {
    @GET("/{firstName}/{lastName}")
    Call<MainActivity.User> fetchUser(@Path("firstName") String firstName,
                                      @Path("lastName") String lastName);
}