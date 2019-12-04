package com.example.shop

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.buy.BuyBody
import com.example.shop.buy.BuyData
import com.example.shop.getSort.GetSortData
import com.example.shop.getSortItem.Item
import com.example.shop.getSortItem.SortItemData
import com.example.shop.renew.RenewData
import kotlinx.android.synthetic.main.activity_buy.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class BuyActivity : AppCompatActivity() {

    private lateinit var musicplayer: MediaPlayer
    private lateinit var apiInterface: APIInterface
    lateinit var timer: Timer
    lateinit var adapter:SortItemAdapter
    lateinit var shared :SharedPreferences
    lateinit var userName:String
    lateinit var userBalance:String
    lateinit var userAccount:String
    lateinit var userLevel:String
    lateinit var userScore:String
    lateinit var userScore2:String
    var userScoreTotal:Int = 0
    lateinit var sortname:String
    lateinit var token:String
    lateinit var buyBody: BuyBody
    var sortid:Int = 1
    var water :String = "0"
    var sortTypeList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        shared = SharedPreferences(this)
        userName = shared.preference.getString("loginName", "")?:""
        userBalance = shared.preference.getString("balancce", "")?:""
        userAccount = shared.preference.getString("account", "")?:""
        userLevel = shared.preference.getString("level", "")?:""
        userScore = shared.preference.getString("score", "")?:""
        userScore2 = shared.preference.getString("score2", "")?:""
        userScoreTotal = userScore.toInt() + userScore2.toInt()

        if (!shared.preference.getString("token", "").isNullOrEmpty()){
            token = "Bearer "+ shared.preference.getString("token", "")
        }

        textUserName.text = userName
        textUserBalance.text = userBalance
        textUserLv.text = userLevel
        textUserScore.text = userScore
        if (userLevel=="5"){
            textUserScore2.text = "(" + "0" + ")"
        } else { textUserScore2.text = "("+ userScore2 + ")" }

        when (userLevel){
            "1" -> imageViewHome.setImageResource(R.drawable.m0)
            "2" -> imageViewHome.setImageResource(R.drawable.m1)
            "3" -> imageViewHome.setImageResource(R.drawable.m2)
            "4" -> imageViewHome.setImageResource(R.drawable.m3)
            "5" -> imageViewHome.setImageResource(R.drawable.m4)
        }

        if (userLevel == "5"){
            proBar.max = userScore.toInt()
            proBar.setProgress(userScore.toInt(), true)
        }
        else{
            proBar.max = userScoreTotal
            proBar.setProgress(userScore.toInt(), true)
        }

        musicplayer = MediaPlayer.create(this, R.raw.miemie)
        musicplayer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.234.60.173")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)

//        apiInterface.getSort().enqueue(object :retrofit2.Callback<GetSortData>{
//            override fun onFailure(call: Call<GetSortData>, t: Throwable) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onResponse(call: Call<GetSortData>, response: Response<GetSortData>) {
//                if (response.isSuccessful){
//                    val sortTypeData = response.body()
//                    sortTypeList.clear()
//                    for (i in 0 until sortTypeData!!.AllSort.size){
//                        sortTypeList.add(sortTypeData.AllSort[i].name)
//                    }
//                    val spinnerAdapter = ArrayAdapter(this@BuyActivity, android.R.layout.simple_spinner_dropdown_item, sortTypeList)
//                    spinner.adapter = spinnerAdapter
//                }
//            }
//        })

        val list = listOf("糧食", "軍事", "特殊")
        val spinnerAdapter = ArrayAdapter(this@BuyActivity, android.R.layout.simple_spinner_dropdown_item, list)
        spinner.adapter = spinnerAdapter

        adapter = SortItemAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        spinner.setOnItemSelectedListener(object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                sortname = spinner.selectedItem.toString()
                if (sortname == list[0]){
                    sortid = 1
                }
                else if (sortname == list[1]){
                    sortid = 2
                }
                else if (sortname == list[2]){
                    sortid = 3
                }

                apiInterface.getSortItem(
                    sortid
                ).enqueue(object :retrofit2.Callback<MutableList<SortItemData>>{
                    override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<MutableList<SortItemData>>, response: Response<MutableList<SortItemData>> ) {
                        if (response.code() == 200){
//                            var sortTypeMap = mapOf<>(1 to "糧食", 2 to "軍事", 3 to "特殊", 4 to "隱藏")
                            val itemList = response.body()!!
                                .map {
                                    Item(
                                        it.id,
                                        sortname,
                                        it.item_name,
                                        it.price,
                                        it.stock,
                                        it.pic
                                    )
                                }
                                .sortedBy { it.id }
                            adapter.update(itemList)
                        }
                    }
                })

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        adapter.setclickedListener(object :SortItemAdapter.clickedListener{
            override fun modifyItemData(id: Int, item_name: String, sort_id: Int, sort_name: String, price: Int, stock: Int, pic: String) {
                if (userLevel.toInt() < 3 && sortid == 3  ){
                    Toast.makeText(this@BuyActivity, "想買齁！快買東西升到等級3吧！~", Toast.LENGTH_SHORT).show()
                }
                else{
                    val inflater = this@BuyActivity.layoutInflater
                    val view = inflater.inflate(R.layout.alert_layout, null)
                    val builder = AlertDialog.Builder(this@BuyActivity)
                        .setView(view)
                        .show()
                    val itemName = view.findViewById<TextView>(R.id.textViewItem)
                    val editCount = view.findViewById<EditText>(R.id.editNumber)
                    val btnBuy = view.findViewById<TextView>(R.id.textBuy)
                    val btnCancel = view.findViewById<TextView>(R.id.textCancel)
                    val count = "庫存只剩"+ "$stock"
                    editCount.hint = count
                    itemName.text = item_name
                    btnBuy.setOnClickListener {
                        if (editCount.text.isEmpty()){
                            Toast.makeText(this@BuyActivity, "請輸入購買數量齁！", Toast.LENGTH_SHORT).show()
                        }
                        else if (editCount.text.toString() == "0"){
                            Toast.makeText(this@BuyActivity, "正常人會按取消！", Toast.LENGTH_SHORT).show()
                        }
                        else if (editCount.text.toString().toInt() > stock ){
                            Toast.makeText(this@BuyActivity, "超過購買數量，請重新輸入！", Toast.LENGTH_SHORT).show()
                            editCount.setText("")
                        }
                        else if (editCount.text.toString().toInt() * price > userBalance.toInt() ){
                            Toast.makeText(this@BuyActivity, "餘額不足哦！請重新輸入！", Toast.LENGTH_SHORT).show()
                            editCount.setText("")
                        }
                        else{
                            buyBody = BuyBody(userAccount, id, editCount.text.toString().toInt())
                            apiInterface.buy(
                                token,
                                buyBody
                            ).enqueue(object :retrofit2.Callback<BuyData>{
                                override fun onFailure(call: Call<BuyData>, t: Throwable) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onResponse(call: Call<BuyData>, response: Response<BuyData>) {
                                    if (response.code() == 201){
                                        val data = response.body()
                                        Toast.makeText(this@BuyActivity, data!!.data.item.item_name+"已購買!", Toast.LENGTH_SHORT).show()
                                        userBalance = data.data.balance.toString()
                                        shared.setBalance(userBalance)
                                        userScore = data.data.score.toString()
                                        shared.setScore(userScore)
                                        textUserBalance.text = userBalance
                                        textUserScore.text = userScore
                                        musicplayer.start()

                                        if(data.data.achevement.isNullOrEmpty()){
                                        }
                                        else{
                                            if (water == "0"){
                                                Toast.makeText(this@BuyActivity, data.data.achevement, Toast.LENGTH_LONG).show()
                                                water = "1"
                                                buyBody = BuyBody(userAccount, 87, 1)
                                                apiInterface.buy(
                                                    token,
                                                    buyBody
                                                ).enqueue(object :retrofit2.Callback<BuyData>{
                                                    override fun onFailure(call: Call<BuyData>, t: Throwable) {
                                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                                    }

                                                    override fun onResponse(call: Call<BuyData>, response: Response<BuyData>) {
                                                        if (response.isSuccessful){
//                                                            Toast.makeText(this@BuyActivity, "恭喜", Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                                })
                                            }
                                        }
                                            apiInterface.getSortItem(
                                                sortid
                                            ).enqueue(object :retrofit2.Callback<MutableList<SortItemData>>{
                                            override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {
                                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                            }
                                            override fun onResponse(call: Call<MutableList<SortItemData>>, response: Response<MutableList<SortItemData>> ) {
                                                if (response.code() == 200){
                                                    val itemList = response.body()!!
                                                        .map {
                                                            Item(
                                                                it.id,
                                                                sortname,
                                                                it.item_name,
                                                                it.price,
                                                                it.stock,
                                                                it.pic
                                                            )
                                                        }
                                                        .sortedBy { it.id }
                                                    adapter.update(itemList)
                                                }
                                            }
                                        })

                                        builder.dismiss()
                                    }
                                }
                            })
                        }
                    }

                    btnCancel.setOnClickListener {
                        builder.dismiss()
                    }
                }
            }
        })

        timer = Timer(true)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                    apiInterface.renew(
                        token
                    ).enqueue(object :retrofit2.Callback<RenewData>{
                    override fun onFailure(call: Call<RenewData>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                    override fun onResponse(call: Call<RenewData>, response: Response<RenewData>) {
                        if (response.code() == 200){
                            val data = response.body()
                            userLevel = data!!.lv.toString()
                            userScore = data.now_sheep.score.toString()
                            userScore2 = data.last_lv.toString()
                            userScoreTotal = userScore.toInt() + userScore2.toInt()

                            this@BuyActivity.runOnUiThread{
                                textUserLv.text = userLevel
                                textUserScore.text = userScore
                                if (userLevel=="5"){
                                    textUserScore2.text = "(" + "0" + ")"
                                } else { textUserScore2.text = "("+ userScore2 + ")" }
                                when (userLevel){
                                    "1" -> imageViewHome.setImageResource(R.drawable.m0)
                                    "2" -> imageViewHome.setImageResource(R.drawable.m1)
                                    "3" -> imageViewHome.setImageResource(R.drawable.m2)
                                    "4" -> imageViewHome.setImageResource(R.drawable.m3)
                                    "5" -> imageViewHome.setImageResource(R.drawable.m4)
                                }

                                if (userLevel == "5"){
                                    proBar.max = userScore.toInt()
                                    proBar.setProgress(userScore.toInt(), true)
                                }
                                else{
                                    var usertotall = userScore.toInt() + userScore2.toInt()
                                    proBar.max = usertotall
                                    proBar.setProgress(userScore.toInt(), true)
                                }
                            }
                        }
                    }
                })
            }
        }
        timer.schedule(timerTask, 3000, 3000)









        btnLogout.setOnClickListener {
            this.finish()
        }

        btnRecord.setOnClickListener {
            val intent = Intent(this@BuyActivity, RecordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        musicplayer.stop()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
