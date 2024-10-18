package com.depi.myapplicatio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.depi.myapplicatio.databinding.FragmentDigitCodeBinding


class digit_code : Fragment() {


    private lateinit var otpCode:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digit_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otpCode = arguments?.getString("otpCode","")?:""
        if (otpCode.isEmpty()){
            Toast.makeText(context, "No Opt Founded", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        Toast.makeText(context, "Otp For Verification is $otpCode", Toast.LENGTH_SHORT).show()
    }


}