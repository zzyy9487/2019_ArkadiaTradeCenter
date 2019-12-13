package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.record.RecordAdapter
import com.example.shop.record.RecordCell
import com.example.shop.record.RecordData
import kotlinx.android.synthetic.main.activity_record.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecordActivity : AppCompatActivity() {

    private lateinit var apiInterface: APIInterface
    lateinit var adapter: RecordAdapter
    lateinit var shared : SharedPreferences
    lateinit var token:String
    var list = mutableListOf<RecordCell>()

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
        apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.record(token, userid!!.toInt())
        call.enqueue(object :retrofit2.Callback<RecordData>{
            override fun onFailure(call: Call<RecordData>, t: Throwable) {
                Toast.makeText(this@RecordActivity, "QQ", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<RecordData>, response: Response<RecordData>) {
                if (response.code() == 200){
                    val sortTypeMap = mapOf(1 to "糧食", 2 to "軍事", 3 to "特殊", 4 to "隱藏組合")
                    list.clear()
                    val list = response.body()!!.SheepAllBuy
                        .map {
                            RecordCell(
                                it.id,
                                it.sort_id,
                                it.sort_id.let { sortTypeMap.getOrDefault(it, "UnKnown") },
                                it.item_name,
                                it.price,
                                it.stock,
                                it.pic
                            )
                        }
                        .sortedBy { it.sort_id }
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
