package com.example.shop.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.*
import com.example.shop.APIInterface
import com.example.shop.record.RecordAdapter

import com.example.shop.record.RecordCell
import com.example.shop.record.RecordData
import com.example.shop.renew.RenewData
import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {

    private lateinit var apiInterface: APIInterface
    lateinit var rootview: View
    lateinit var adapter: RecordAdapter
    lateinit var timer:Timer
    var list = mutableListOf<RecordCell>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val act = activity as BuyActivity

        rootview = inflater.inflate(R.layout.fragment_history, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.234.60.173")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)
        val call = apiInterface.record(act.token, act.userid.toInt())
        call.enqueue(object :retrofit2.Callback<RecordData>{
            override fun onFailure(call: Call<RecordData>, t: Throwable) {
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



        adapter = RecordAdapter()
        rootview.recyclerRecord.layoutManager = LinearLayoutManager(activity)
        rootview.recyclerRecord.adapter = adapter

        adapter.setclickedListener(object :RecordAdapter.clickedListener{
            override fun worldEnd() {
                act.imageGIF.visibility = View.VISIBLE
                act.bottomNavigation.visibility = View.GONE
                timer = Timer(true)
                val timerTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        act.finish()
                    }
                }
                timer.schedule(timerTask, 7000, 100000000)
            }
        })

        return rootview
    }
}
