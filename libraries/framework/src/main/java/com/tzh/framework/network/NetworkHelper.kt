package com.tzh.framework.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tzh.framework.network.interceptor.HttpRequestInterceptor
import okhttp3.Cache
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

private const val CONTENT_LENGTH = 250_000L
private const val CLIENT_TIME_OUT = 60L
private const val CLIENT_CACHE_SIZE = 10 * 1024 * 1024L
private const val CLIENT_CACHE_DIRECTORY = "http"

fun createMoshi(): Moshi {
    return Moshi.Builder()
        .add(SubscriptionStatusAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()
}

fun createCache(context: Context): Cache = Cache(
    directory = File(context.cacheDir, CLIENT_CACHE_DIRECTORY),
    maxSize = CLIENT_CACHE_SIZE
)

fun createHttpLoggingInterceptor(isDev: Boolean = true): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = if (isDev) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

fun createHttpRequestInterceptor(): HttpRequestInterceptor {
    return HttpRequestInterceptor()
}

fun createChuckInterceptor(context: Context): ChuckerInterceptor {
    // Create the Collector
    val chuckerCollector = ChuckerCollector(
        context = context,
        // Toggles visibility of the push notification
        showNotification = true,
        // Allows to customize the retention period of collected data
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    return ChuckerInterceptor.Builder(context)
        // The previously created Collector
        .collector(chuckerCollector)
        // The max body content length in bytes, after this responses will be truncated.
        .maxContentLength(CONTENT_LENGTH)
        // List of headers to replace with ** in the Chucker UI
        .redactHeaders("Auth-Token", "Bearer")
        // Read the whole response body even when the client does not consume the response completely.
        // This is useful in case of parsing errors or when the response body
        // is closed before being read like in Retrofit with Void and Unit types.
        .alwaysReadResponseBody(true)
        .build()
}

fun createOkHttpClient(
    isDev: Boolean = true,
    context: Context
): OkHttpClient {
    return OkHttpClient.Builder().apply {
        addInterceptor(createHttpLoggingInterceptor(isDev))
        if (isDev) {
            addInterceptor(createChuckInterceptor(context))
            addInterceptor(createHttpRequestInterceptor())
        }
        connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        followSslRedirects(true)
        followRedirects(true)
        retryOnConnectionFailure(true)
    }.build()
}

fun createOkHttpClient(
    isDev: Boolean = true,
    isCache: Boolean = false,
    context: Context
): OkHttpClient {
    return OkHttpClient.Builder().apply {
        if (isCache) cache(createCache(context))
        addInterceptor(createHttpLoggingInterceptor(isDev))
        if (isDev) {
            addInterceptor(createChuckInterceptor(context))
            addInterceptor(createHttpRequestInterceptor())
        }
        connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        followSslRedirects(true)
        followRedirects(true)
        retryOnConnectionFailure(true)
    }.build()
}

fun createOkHttpClient(
    isCache: Boolean = false,
    interceptors: MutableList<Interceptor> = mutableListOf(),
    context: Context
): OkHttpClient {
    return OkHttpClient.Builder().apply {
        if (isCache) cache(createCache(context))
        interceptors.forEach { addInterceptor(it) }
        connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        followSslRedirects(true)
        followRedirects(true)
        retryOnConnectionFailure(true)
    }.build()
}

fun createOkHttpClient(
    isCache: Boolean = false,
    vararg interceptors: Interceptor,
    context: Context,
    needCert: Boolean = false,
): OkHttpClient {
    return OkHttpClient.Builder().apply {

        if (needCert) {
            val sslInfo = addCert(context)

            val sslContext = sslInfo.first
            val trustManager = sslInfo.second
            val secureSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2) // enforce TLS 1.2 & 1.3
                .cipherSuites(
                    // Recommended strong cipher suites
                    CipherSuite.TLS_AES_128_GCM_SHA256,
                    CipherSuite.TLS_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
                )
                .build()
            if (sslContext != null && trustManager != null) {
                sslSocketFactory(
                    sslContext.socketFactory,
                    trustManager
                )
            }
            connectionSpecs(
                listOf(
                    secureSpec,
                )
            ) // include CLEARTEXT only if needed for debug
            protocols(listOf(Protocol.HTTP_1_1))
        }
        if (isCache) cache(createCache(context))
        interceptors.forEach { addInterceptor(it) }
        connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        followSslRedirects(true)
        followRedirects(true)
        retryOnConnectionFailure(true)
    }.build()
}

private fun addCert(context: Context): Pair<SSLContext?, X509TrustManager?> {


    try {
        val externalFilesDir = context.getExternalFilesDir(null)
        val caFile = File(externalFilesDir, "singCA.pem")

        val caInput = FileInputStream(caFile)
        val cf = CertificateFactory.getInstance("X.509")
        val ca = cf.generateCertificate(caInput) as java.security.cert.X509Certificate
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)
        var trustManager: X509TrustManager? = null
        for (tm in tmf.trustManagers) {
            if (tm is X509TrustManager) {
                trustManager = tm
                break
            }
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)
        return sslContext to trustManager
    } catch (e: Exception) {
        return (null to null)
    }
}


/**
 * Create Retrofit Client with Moshi
 *
 * <reified T> private func let us using reflection.
 * We can use generics and reflection so ->
 *  we don't have to define required NewsApi Interface here
 */
inline fun <reified T> createRetrofitWithMoshi(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
    baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    return retrofit.create(T::class.java)
}


class SubscriptionStatusAdapter {
    @FromJson
    fun fromJson(value: String): SubscriptionStatus = when (value) {
        "0" -> SubscriptionStatus.NoSubscription
        "1" -> SubscriptionStatus.Expired
        "2" -> SubscriptionStatus.ACTIVE
        else -> SubscriptionStatus.Unknown
    }

    @ToJson
    fun toJson(status: SubscriptionStatus): String = when (status) {
        SubscriptionStatus.NoSubscription -> "0"
        SubscriptionStatus.Expired -> "1"
        SubscriptionStatus.ACTIVE -> "2"
        SubscriptionStatus.Unknown -> "-1"
    }
}


enum class SubscriptionStatus {
    NoSubscription,
    Expired,
    ACTIVE,
    Unknown
}