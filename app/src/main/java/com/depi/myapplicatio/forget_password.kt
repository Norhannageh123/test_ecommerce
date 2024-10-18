package com.depi.myapplicatio.forget_password

import MailgunApi
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.depi.myapplicatio.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class ForgetPasswordFragment : Fragment() {

    private lateinit var inputEmail: EditText
    private lateinit var btnSendCode: Button
    private lateinit var btnBack: ImageButton

    // استخدم API Key الخاص بك هنا
    private val mailgunApi: MailgunApi = MailgunApi.create("e05467477a40b20ec6457fab5ceaa320-d010bdaf-d0596b60")

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
        val verificationCode = Random.nextInt(100000, 999999).toString()

        mailgunApi.sendMessage(
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
        })
    }
}
