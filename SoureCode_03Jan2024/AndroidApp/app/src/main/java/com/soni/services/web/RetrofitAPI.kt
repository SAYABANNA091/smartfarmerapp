package com.soni.services.web


import android.content.Context
import android.util.Log
import com.soni.SoniApp.Companion.getAndroidVersion
import com.soni.SoniApp.Companion.getDeviceId
import com.soni.SoniApp.Companion.getDeviceName
import com.google.gson.GsonBuilder
import com.soni.Preference.getCurrentLanguageID
import com.soni.Preference.getFcmToken
import com.soni.Preference.getLoginToken
import com.soni.Preference.getUserID
import com.soni.SoniApp
import com.soni.utils.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitAPI {

    companion object {

        fun getRetrofit(mContext: Context): Retrofit {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(Const.API.BaseUrl)
                .client(getUnsafeOkHttpClient(mContext))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit
        }


        fun getUnsafeOkHttpClient(applicationContext: Context): OkHttpClient? {
            return try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts =
                    arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                                return arrayOfNulls(0)
                            }
                        }
                    )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory

                val okHttpClient = OkHttpClient.Builder()

                okHttpClient.addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                        .header(Const.ApiHeaders.DEVICE_ID, applicationContext.getDeviceId())
                        .header(Const.ApiHeaders.DEVICE_TYPE, Const.ApiHeaders.DEVICE_TYPE_ANDROID)
                        .header(Const.ApiHeaders.OS_VERSION, getAndroidVersion()!!)
                        .header(Const.ApiHeaders.DEVICE_NAME, getDeviceName()!!)
                        .header(Const.ApiHeaders.USER_TOKEN, getLoginToken())
                        .header(Const.ApiHeaders.USER_ID, getUserID())
                        .header(Const.ApiHeaders.DeviceToken, getFcmToken())
                        .header(Const.ApiHeaders.Latitude,if(SoniApp.location!=null)SoniApp.location!!.latitude.toString() else "")
                        .header(Const.ApiHeaders.Longitude,if(SoniApp.location!=null)SoniApp.location!!.longitude.toString() else "")
                        .header(Const.ApiHeaders.LANGUAGE_ID, if(getCurrentLanguageID()=="1") "en" else "kn" )

                        .method(original.method, original.body)
                        .build()
                    chain.proceed(request)


                }


                okHttpClient.addInterceptor(object : Interceptor {
                    @Throws(Exception::class)
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val request: Request = chain.request()
                        val response: okhttp3.Response = chain.proceed(request)
                        Log.e("tagResponse", "response is " + response)

                        return response
                    }
                })

                okHttpClient.connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .addInterceptor(LoggingIntercepor())
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor(UnauthorizedInterceptor(applicationContext))
                    .retryOnConnectionFailure(true)
                    .build()

                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                okHttpClient.hostnameVerifier(HostnameVerifier { hostname, session -> true })

                okHttpClient.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

    }


}