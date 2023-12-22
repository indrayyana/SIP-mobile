package com.example.sipmobile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @Multipart
    // Memanggil upload_image.php dengan metode post
    @POST(URLs.URL_UPLOADIMAGE)

    // setting untuk nilai parameter dengan nama "file"
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}
