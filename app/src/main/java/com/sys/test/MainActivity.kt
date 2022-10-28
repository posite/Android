package com.sys.test

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sys.test.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val retrofit = Retrofit.Builder().baseUrl("https://api.visitjeju.net/vsjApi/contents/").addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(KakaoMapApi::class.java)
        val kakaoMap = api.getData()
        kakaoMap.enqueue(object : Callback<Item>{
            override fun onResponse(call: Call<Item>, response: Response<Item>)
            {
                if(response.isSuccessful && response.code() == 200){
                    api?.let{

                    }
                    Log.d("결과", "성공 : ${response.raw()}")
                }

            }
            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })


        binding.button.setOnClickListener {
            val url ="kakaomap://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=FOOT"
            var intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            var list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

            //카카오맵 어플리케이션이 사용자 핸드폰에 깔려있으면 바로 앱으로 연동
            //그렇지 않다면 다운로드 페이지로 연결

            if (list== null){
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")))
            }else{
                startActivity(intent)
            }
        }
    }

}