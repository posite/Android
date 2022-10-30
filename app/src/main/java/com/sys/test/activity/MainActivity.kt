package com.sys.test.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.test.R
import com.sys.test.databinding.ActivityMainBinding
import com.sys.test.network.KakaoMapApi
import com.sys.test.network.Monttak
import com.sys.test.profiledata.HorizontalItemDecorator
import com.sys.test.profiledata.ProfileAdapter
import com.sys.test.profiledata.ProfileData
import com.sys.test.profiledata.VerticalItemDecorator
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var data : Monttak

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        var longitude : Double = 0.0
        var latitude : Double  = 0.0

        val retrofit = Retrofit.Builder().baseUrl("https://api.visitjeju.net/vsjApi/contents/").addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(KakaoMapApi::class.java)
        val kakaoMap = api.getData()
        kakaoMap.enqueue(object : Callback<Monttak>{
            override fun onResponse(call: Call<Monttak>, response: Response<Monttak>)
            {
                if(response.isSuccessful && response.code() == 200){
                    data = response.body()!!
                    binding.button.text=data.items[1].title
                    longitude = data.items[1].longitude ?: 127.005515
                    latitude = data.items[1].latitude ?: 37.537229

                    Log.d("결과", "성공 : ${response.raw()}")
                }

            }
            override fun onFailure(call: Call<Monttak>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
            }
        })
        binding.button.setOnClickListener {
            if(latitude != 0.0 && longitude != 0.0){
                val url ="kakaomap://look?p=${latitude},${longitude}"
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
        binding.bol.setOnClickListener {
            var intent = Intent(applicationContext, SecondActivity::class.java)
            intent.putExtra("datas",data)
            intent.putExtra("label","정보,축제/행사")
            intent.putExtra("split","bol")
            startActivity(intent)
        }
        binding.nol.setOnClickListener {
            var intent = Intent(applicationContext, SecondActivity::class.java)
            intent.putExtra("datas",data)
            intent.putExtra("label","테마여행,쇼핑")
            intent.putExtra("split","bol")
            startActivity(intent)
        }
        binding.shil.setOnClickListener {
            var intent = Intent(applicationContext, SecondActivity::class.java)
            intent.putExtra("datas",data)
            intent.putExtra("label","정보,숙박")
            intent.putExtra("split","shil")
            startActivity(intent)
        }
        binding.muk.setOnClickListener {
            var intent = Intent(applicationContext, SecondActivity::class.java)
            intent.putExtra("datas",data)
            intent.putExtra("label","음식점")
            intent.putExtra("split","muk")
            startActivity(intent)
        }
    }

}