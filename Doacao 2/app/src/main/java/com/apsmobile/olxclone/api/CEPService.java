package com.apsmobile.olxclone.api;

import com.apsmobile.olxclone.model.Local;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json/")
    Call<Local> recuperaCEP(@Path("cep") String cep);

}
