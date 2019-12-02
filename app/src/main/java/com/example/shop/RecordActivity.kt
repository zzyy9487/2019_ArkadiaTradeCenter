package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.record.RecordCell
import com.example.shop.record.RecordData
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecordActivity : AppCompatActivity() {

    lateinit var adapter:RecordAdapter
    lateinit var shared :SharedPreferences
    lateinit var token:String
    var list = mutableListOf<RecordCell>()
    val array = listOf<String>("糧食", "軍事", "特殊", "Bonus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        shared = SharedPreferences(this)

        val userid = shared.preference.getString("id", "")
        if (!shared.preference.getString("token", "").isNullOrEmpty()){
            token = "Bearer "+ shared.preference.getString("token", "")
        }

        adapter = RecordAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.234.60.173")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.record(token, userid!!.toInt())
        call.enqueue(object :retrofit2.Callback<RecordData>{
            override fun onFailure(call: Call<RecordData>, t: Throwable) {
                Toast.makeText(this@RecordActivity, "QQ", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<RecordData>, response: Response<RecordData>) {
                if (response.code() == 200){
                    val data = response.body()
                    list.clear()
                    for (i in 0 until data!!.SheepAllBuy.size){
                        list.add(i, RecordCell(data!!.SheepAllBuy[i].id, array[data!!.SheepAllBuy[i].sort_id-1], data!!.SheepAllBuy[i].item_name, data!!.SheepAllBuy[i].price, data!!.SheepAllBuy[i].stock, data!!.SheepAllBuy[i].pic) )
                    }
                    adapter.update(list)

                }

            }

        })






    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
