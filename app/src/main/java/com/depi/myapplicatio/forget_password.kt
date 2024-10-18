package com.depi.myapplicatio.forget_password

import MailgunApi
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.depi.myapplicatio.R
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import kotlin.random.Random

class ForgetPasswordFragment : Fragment() {

    private lateinit var inputEmail: EditText
    private lateinit var btnSendCode: Button
    private lateinit var btnBack: ImageButton

    // استخدم API Key الخاص بك هنا
    private val mailgunApi: MailgunApi =
        MailgunApi.create("e05467477a40b20ec6457fab5ceaa320-d010bdaf-d0596b60")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // هنا نقوم فقط بإنشاء الـ View وإرجاعه
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // الآن نقوم بتعريف العناصر بعد إنشاء الـ View
        inputEmail = view.findViewById(R.id.inputEmail)
        btnSendCode = view.findViewById(R.id.btnSendCode)
        btnBack = view.findViewById(R.id.btnBack)

        // عند الضغط على زر العودة
        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_forgetPassword_to_login)
        }

        // عند الضغط على زر "إرسال الكود"
        btnSendCode.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                sendVerificationEmail(email) // استدعاء دالة الإرسال
            } else {
                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // دالة لإرسال الكود إلى البريد الإلكتروني
    private fun sendVerificationEmail(email: String) {
        // إنشاء كود التحقق عشوائي مكون من 6 أرقام
//        val verificationCode = Random.nextInt(100000, 999999).toString()
        val verificationCode = Random.nextInt(1000, 10000).toString()
        sendCodeToEmail(email, verificationCode)
        /*mailgunApi.sendMessage(
            "noreply@sandbox1e783de425564380b7688e84bd3d5ae2.mailgun.org", // من البريد الإلكتروني الخاص بك في Mailgun
            email,
            "Your Verification Code", // عنوان الرسالة
            "Your verification code is: $verificationCode" // محتوى الرسالة
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Verification email sent successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_forgetPassword_to_digitcode)
                } else {
                    Toast.makeText(context, "Failed to send email: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Failed to send email: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    private fun sendCodeToEmail(receiverEmail: String, verificationCode: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.brevo.com/v3/smtp/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: OtpCodeSendCodeApi = retrofit.create(OtpCodeSendCodeApi::class.java)

        val otpRequest = OptRequest(
            sender = UserContact(
                name = "E Commerce App",
                email = "ahmed.m.hassaan1998@gmail.com"
            ),
            receivers = mutableListOf(
                UserContact(
                    name = "E-Commerce User",
                    email = receiverEmail
                ),
            ),
            subject ="Opt Code",
            htmlContent ="""
                <html>
                    <head>
                    </head>
                                   
                    <body>
                        <p> Hello,</p> Your Verification Code is <h1>$verificationCode</h1></p>
                    </body>
                    
                </html>
            """.trimIndent()
        )
        api.sendMessage(request = otpRequest).enqueue(
            object : Callback<OtpResponse>{
                override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Verification email sent successfully", Toast.LENGTH_SHORT).show()
//                        findNavController().navigate(R.id.action_forgetPassword_to_digitcode)
                        findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordToDigitcode(verificationCode))
                    } else {
                        Log.d("APP_TAG", "onResponse: code is ${response.code()}")
                        Toast.makeText(context, "Failed to send email: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to send email: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

interface OtpCodeSendCodeApi {
    @POST("email")
    @Headers("api-key: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")
    fun sendMessage(
        @Body request: OptRequest
    ):Call<OtpResponse>
}

/*
{
    "sender": {
    "name": "E Commerce App",
    "email": "ahmed.m.hassaan1998@gmail.com"
},
    "to": [
    {
        "email": "norhannageh47@gmail.com",
        "name": "Temp Usename"
    }
    ],
    "subject": "Hello world",
    "htmlContent": "<html><head></head><body><p>Hello,</p>This is my first transactional email sent from Brevo.</p></body></html>"
}
*/

data class OptRequest(
    @SerializedName("sender")
    val sender: UserContact,

    @SerializedName("to")
    val receivers: List<UserContact>,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("htmlContent")
    val htmlContent: String

)

data class UserContact(
    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String
)


/*{
    "messageId": "<202410180103.19937663250@smtp-relay.mailin.fr>"
}`*/
data class OtpResponse(
    @SerializedName("messageId")
    val messageId:String?
)
