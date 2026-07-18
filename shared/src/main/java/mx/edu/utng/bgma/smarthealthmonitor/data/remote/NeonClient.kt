package mx.edu.utng.bgma.smarthealthmonitor.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeonClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${NeonConfig.NEON_API_KEY}")
                .build()
            chain.proceed(request)
        }
        .build()

    val service: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NeonConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NeonApiService::class.java)
    }
}
