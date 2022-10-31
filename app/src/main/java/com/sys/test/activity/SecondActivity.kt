package com.sys.test.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sys.test.databinding.SecondBinding
import com.sys.test.network.Monttak
import com.sys.test.profiledata.HorizontalItemDecorator
import com.sys.test.profiledata.ProfileAdapter
import com.sys.test.profiledata.ProfileData
import com.sys.test.profiledata.VerticalItemDecorator

class SecondActivity : AppCompatActivity() {
    lateinit var profileAdapter: ProfileAdapter
    private var datas = ArrayList<ProfileData>()
    private lateinit var data : Monttak
    private lateinit var secondBinding: SecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        secondBinding = SecondBinding.inflate(layoutInflater)
        setContentView(secondBinding.root)
        val intent = intent
        data = intent.getSerializableExtra("datas") as Monttak
        val split = intent.getStringExtra("split")!!
        val label = intent.getStringExtra("label")!!
        initRecycler(data, label, split)
    }
    private fun initRecycler(monttak: Monttak, label: String, split: String) {
        var count = 0
        val token = label.split(",")
        for(i in 0 until monttak.items.size){
            if(monttak.items[i].repPhoto!=null && monttak.items[i].repPhoto.photoid!=null){
                if(monttak.items[i].repPhoto.photoid.thumbnailpath!=null && token.contains(monttak.items[i].contentscd.label)){
                    if(!monttak.items[i].roadaddress.isNullOrEmpty()){
                        datas.add(ProfileData(roadaddress ="주소 : "+monttak.items[i].roadaddress, thumbnailpath = monttak.items[i].repPhoto.photoid.thumbnailpath, title ="제목 : "+ monttak.items[i].title))
                        count++
                    }else{
                        datas.add(ProfileData(roadaddress ="주소 : ", thumbnailpath = monttak.items[i].repPhoto.photoid.thumbnailpath, title ="제목 : "+ monttak.items[i].title))
                        count++
                    }

                }
            }
        }

        Log.d("실제1 : ", count.toString())
        count = 0
        Log.d("실제2 : ", count.toString())
        profileAdapter = ProfileAdapter(datas)
        profileAdapter.notifyDataSetChanged()
        when(split){
            "nol"->{
                secondBinding.nollist.adapter = profileAdapter
                secondBinding.nollist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "bol"->{
                secondBinding.bollist.adapter = profileAdapter
                secondBinding.bollist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "shil"->{
                secondBinding.shillist.adapter = profileAdapter
                secondBinding.shillist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
            "muk"->{
                secondBinding.muklist.adapter = profileAdapter
                secondBinding.muklist.layoutManager = LinearLayoutManager(this)

                chooseView(split)
            }
        }

        Log.d("size", profileAdapter.itemCount.toString())
    }
    private fun chooseView(category: String){

        when(category){
            "nol"->{
                secondBinding.nollist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.nollist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.nollist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE}
            "bol"->{
                secondBinding.bollist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.bollist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.bollist.visibility = View.VISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE}
            "shil"->{
                secondBinding.shillist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.shillist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.shillist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.muklist.visibility = View.INVISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE}
            "muk"->{
                secondBinding.muklist.addItemDecoration(VerticalItemDecorator(20))
                secondBinding.muklist.addItemDecoration(HorizontalItemDecorator(10))
                secondBinding.muklist.visibility = View.VISIBLE
                secondBinding.bollist.visibility = View.INVISIBLE
                secondBinding.nollist.visibility = View.INVISIBLE
                secondBinding.shillist.visibility = View.INVISIBLE}
        }
    }
}