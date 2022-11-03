package com.sys.test.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.sys.test.R
import com.sys.test.databinding.SecondBinding
import com.sys.test.network.Item
import com.sys.test.network.KakaoMapApi
import com.sys.test.network.Monttak
import com.sys.test.profiledata.HorizontalItemDecorator
import com.sys.test.profiledata.ProfileAdapter
import com.sys.test.profiledata.ProfileData
import com.sys.test.profiledata.VerticalItemDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SecondActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var isNext = false // 다음 페이지 유무
    private var resultDec = 45
    private var split = ""
    private var label = ""
    lateinit var profileAdapter: ProfileAdapter
    private var datas = ArrayList<ProfileData>()
    private lateinit var data: ArrayList<Item>
    private var resultAmount = 0
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
    private lateinit var secondBinding: SecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        secondBinding = SecondBinding.inflate(layoutInflater)
        setContentView(secondBinding.root)
        setToolbar()
        val intent = intent
        data = ArrayList<Item>()
        split = intent.getStringExtra("split")!!
        label = intent.getStringExtra("label")!!
        Log.d("split:", " : $split")
        Log.d("label:", " : $label")
        CoroutineScope(Dispatchers.Default).launch {
            launch {
                val intent = Intent(this@SecondActivity, LoadingActivity::class.java)
                startActivity(intent)
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                val retrofit =
                    Retrofit.Builder().baseUrl("https://api.visitjeju.net/vsjApi/contents/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                val api = retrofit.create(KakaoMapApi::class.java)
                Log.d("apicall:", " : ")
                apiCall(api, label, split)
            }
        }
    }

    private fun initRecycler(items: ArrayList<Item>, label: String, split: String) {
        var count = 0
        val token = label.split(",")
        for (i in 0 until items.size) {
            if (items[i].repPhoto != null && items[i].repPhoto.photoid != null) {
                if (items[i].repPhoto.photoid.thumbnailpath != null && token.contains(
                        items[i].contentscd.label
                    )
                ) {
                    if (!items[i].roadaddress.isNullOrEmpty()) {
                        datas.add(
                            ProfileData(
                                roadaddress = "주소 : " + items[i].roadaddress,
                                thumbnailpath = items[i].repPhoto.photoid.thumbnailpath,
                                title = "제목 : " + items[i].title,
                                item = items[i]
                            )
                        )
                        count++
                    } else {
                        datas.add(
                            ProfileData(
                                roadaddress = "주소 : ",
                                thumbnailpath = items[i].repPhoto.photoid.thumbnailpath,
                                title = "제목 : " + items[i].title,
                                item = items[i]
                            )
                        )
                        count++
                    }
                }
            }
        }

        Log.d("실제1 : ", count.toString())
        profileAdapter = ProfileAdapter(datas, this)
        profileAdapter.notifyDataSetChanged()

        when (split) {
            "nol" -> {
                secondBinding.nollist.adapter = profileAdapter
                secondBinding.nollist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "bol" -> {
                secondBinding.bollist.adapter = profileAdapter
                secondBinding.bollist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "shil" -> {
                secondBinding.shillist.adapter = profileAdapter
                secondBinding.shillist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "muk" -> {
                secondBinding.muklist.adapter = profileAdapter
                secondBinding.muklist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
        }
        Log.d("size", profileAdapter.itemCount.toString())
    }

    private fun apiCall(api: KakaoMapApi, label: String, split: String) {
        var count = 0;

        for (j in (resultDec - 4)..(resultDec)) {
            val kakaoMap = api.getDataPage(j)
            kakaoMap.enqueue(object : Callback<Monttak> {
                override fun onResponse(call: Call<Monttak>, response: Response<Monttak>) {
                    if (response.isSuccessful && response.code() == 200) {
                        if (data.isNullOrEmpty()) {
                            data = response.body()!!.items as ArrayList<Item>
                        } else {
                            data.addAll(response.body()!!.items)
                        }
                        Log.d("결과", "성공 : ${response.raw()}")
                        resultAmount++
                        if (resultAmount == 5) {
                            initRecycler(data, label, split)
                            initScrollListener(split)
                            Log.d("최종 결과", "성공")
                            resultAmount = 0
                        }
                    }
                }

                override fun onFailure(call: Call<Monttak>, t: Throwable) {
                    Log.d("결과:", "실패 : $t")
                }
            })
            count++
        }

        resultDec -= count
    }

    private fun chooseView(category: String) {
        when (category) {
            "nol" -> {
                secondBinding.nollist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.nollist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.nollist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE
            }
            "bol" -> {
                secondBinding.bollist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.bollist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.bollist.visibility = View.VISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE
            }
            "shil" -> {
                secondBinding.shillist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.shillist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.shillist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE
            }
            "muk" -> {
                secondBinding.muklist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.muklist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.muklist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(secondBinding.toolbar)

        // 툴바 왼쪽 버튼 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)  // 왼쪽 버튼 이미지 설정
        supportActionBar!!.setDisplayShowTitleEnabled(false)    // 타이틀 안보이게 하기
    }

    // 툴바 메뉴 버튼을 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return true
    }

    // 툴바 메뉴 버튼이 클릭 됐을 때 콜백
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> { // 메뉴 버튼
                secondBinding.drawerLayout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {  // 네비게이션 메뉴가 클릭되면 스낵바가 나타난다.
//            R.id.account-> Snackbar.make(secondBinding.toolbar,"Navigation Account pressed", Snackbar.LENGTH_SHORT).show()
//            R.id.setting-> Snackbar.make(secondBinding.toolbar,"Navigation Setting pressed", Snackbar.LENGTH_SHORT).show()
        }
        secondBinding.drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }

    override fun onBackPressed() {
        if (secondBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            secondBinding.drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadMorePosts() {
        val retrofit = Retrofit.Builder().baseUrl("https://api.visitjeju.net/vsjApi/contents/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = retrofit.create(KakaoMapApi::class.java)
        val handler = android.os.Handler()
        Log.d("loadMore", "dd")
        handler.postDelayed({ apiCall(api, label, split) }, 1000)
    }

    private fun initScrollListener(split: String) {

        secondBinding.muklist.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = secondBinding.muklist.layoutManager

                // hasNextPage() -> 다음 페이지가 있는 경우
                if (hasNextPage()) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 5개 모자란 경우, 데이터를 loadMore 한다
                    if (!secondBinding.muklist.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                        loadMorePosts()
                    }
                }
            }
        })
    }


    private fun hasNextPage(): Boolean {
        return isNext
    }

    private fun setHasNextPage(b: Boolean) {
        isNext = b
    }
}