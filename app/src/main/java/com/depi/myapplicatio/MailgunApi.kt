import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MailgunApi {
    @FormUrlEncoded
    @POST("messages")
    fun sendMessage(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("subject") subject: String,
        @Field("text") text: String
    ): Call<Void>

    companion object {
        fun create(apiKey: String): MailgunApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.mailgun.net/v3/sandbox1e783de425564380b7688e84bd3d5ae2.mailgun.org/") // Base URL الخاص بـ Mailgun
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(MailgunApi::class.java)
        }
    }
}
