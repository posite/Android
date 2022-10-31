package com.sys.test.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.sys.test.R
import com.sys.test.databinding.SecondBinding
import com.sys.test.network.Monttak
import com.sys.test.profiledata.HorizontalItemDecorator
import com.sys.test.profiledata.ProfileAdapter
import com.sys.test.profiledata.ProfileData
import com.sys.test.profiledata.VerticalItemDecorator

class SecondActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var profileAdapter: ProfileAdapter
    private var datas = ArrayList<ProfileData>()
    private lateinit var data: Monttak
    private lateinit var secondBinding: SecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        secondBinding = SecondBinding.inflate(layoutInflater)
        setContentView(secondBinding.root)
        setToolbar()
        val intent = intent
        data = intent.getSerializableExtra("datas") as Monttak
        val split = intent.getStringExtra("split")!!
        val label = intent.getStringExtra("label")!!
        initRecycler(data, label, split)

    }

    private fun initRecycler(monttak: Monttak, label: String, split: String) {
        var count = 0
        val token = label.split(",")
        for (i in 0 until monttak.items.size) {
            if (monttak.items[i].repPhoto != null && monttak.items[i].repPhoto.photoid != null) {
                if (monttak.items[i].repPhoto.photoid.thumbnailpath != null && token.contains(
                        monttak.items[i].contentscd.label
                    )
                ) {
                    if (!monttak.items[i].roadaddress.isNullOrEmpty()) {
                        datas.add(
                            ProfileData(
                                roadaddress = "주소 : " + monttak.items[i].roadaddress,
                                thumbnailpath = monttak.items[i].repPhoto.photoid.thumbnailpath,
                                title = "제목 : " + monttak.items[i].title,
                                item = monttak.items[i]
                            )
                        )
                        count++
                    } else {
                        datas.add(
                            ProfileData(
                                roadaddress = "주소 : ",
                                thumbnailpath = monttak.items[i].repPhoto.photoid.thumbnailpath,
                                title = "제목 : " + monttak.items[i].title,
                                item = monttak.items[i]
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
    private fun setToolbar(){
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
        when(item!!.itemId){
            android.R.id.home->{ // 메뉴 버튼
                secondBinding.drawerLayout.openDrawer(GravityCompat.START)    // 네비게이션 드로어 열기
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){  // 네비게이션 메뉴가 클릭되면 스낵바가 나타난다.
            R.id.account-> Snackbar.make(secondBinding.toolbar,"Navigation Account pressed", Snackbar.LENGTH_SHORT).show()
            R.id.setting-> Snackbar.make(secondBinding.toolbar,"Navigation Setting pressed", Snackbar.LENGTH_SHORT).show()
        }
        secondBinding.drawerLayout.closeDrawers() // 기능을 수행하고 네비게이션을 닫아준다.
        return false
    }
    override fun onBackPressed() {
        if(secondBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            secondBinding.drawerLayout.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }
}