package com.example.shop

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.buy.BuyBody
import com.example.shop.buy.BuyData
import com.example.shop.fragment.BuyFragment
import com.example.shop.fragment.ChatFragment
import com.example.shop.fragment.GameFragment
import com.example.shop.fragment.HistoryFragment
import com.example.shop.getMsg.MsgAdapter
import com.example.shop.getMsg.MsgData
import com.example.shop.getMsg.Sheepallmsg
import com.example.shop.getSortItem.Item
import com.example.shop.getSortItem.SortItemAdapter
import com.example.shop.getSortItem.SortItemData
import com.example.shop.renew.RenewData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.alert_layout_logout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IndexOutOfBoundsException
import java.sql.Time
import java.util.*
import java.util.zip.Inflater

class BuyActivity : AppCompatActivity() {

//    lateinit var musicplayer: MediaPlayer
    private lateinit var apiInterface: APIInterface
    lateinit var timer: Timer
    lateinit var timerMsg:Timer
    lateinit var timerEnd:Timer
    lateinit var adapter: SortItemAdapter
    lateinit var shared :SharedPreferences
    lateinit var userName:String
    lateinit var userBalance:String
    lateinit var userAccount:String
    lateinit var userLevel:String
    lateinit var userScore:String
    lateinit var userScore2:String
    lateinit var type :String
    var userScoreTotal:Int = 0
    lateinit var sortname:String
    lateinit var token:String
    lateinit var buyBody: BuyBody
    lateinit var userid:String
    var sortid:Int = 1
    val manager = this.supportFragmentManager
    var fragmentBuy = BuyFragment()
    var fragmentChat = ChatFragment()
    var fragmentGame = GameFragment()
    var fragmentHistory = HistoryFragment()
    var msgKeepList = mutableListOf<Sheepallmsg>()
    lateinit var context: Context
    var timercount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        shared = SharedPreferences(this)
        userid = shared.preference.getString("id", "")?:""
        userName = shared.preference.getString("loginName", "")?:""
        userBalance = shared.preference.getString("balancce", "")?:""
        userAccount = shared.preference.getString("account", "")?:""
        userLevel = shared.preference.getString("level", "")?:""
        type = shared.preference.getString("type", "")?:""
        userScore = shared.preference.getString("score", "")?:""
        userScore2 = shared.preference.getString("score2", "")?:""
        userScoreTotal = userScore.toInt() + userScore2.toInt()

        if (!shared.preference.getString("token", "").isNullOrEmpty()){
            token = "Bearer "+ shared.preference.getString("token", "")
        }

        textUserName.text = userName
        textUserBalance.text = userBalance
        textUserLv.text = "Lv. " +userLevel
        textUserScore.text = userScore
        if (userLevel=="5"){
            textUserScore2.text = "(" + "0" + ")"
        } else { textUserScore2.text = "("+ userScore2 + ")" }

        setPhoto()


        if (userLevel == "5"){
            proBar.max = userScore.toInt()
            proBar.setProgress(userScore.toInt(), true)
        }
        else{
            proBar.max = userScoreTotal
            proBar.setProgress(userScore.toInt(), true)
        }

//        musicplayer = MediaPlayer.create(this, R.raw.miemie)
//        musicplayer.start()

        bottomNavigation.selectedItemId = R.id.buy
        bottomNavigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener)

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

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
               when(tab.position){
                   0 ->{
                       sortname = list[0]
                       sortid = 1
                       apiInterface.getSortItem(
                           sortid
                       ).enqueue(object :Callback<MutableList<SortItemData>>{
                           override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {

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
                   }

                   1 ->{
                       sortname = list[1]
                       sortid = 2
                       apiInterface.getSortItem(
                           sortid
                       ).enqueue(object :Callback<MutableList<SortItemData>>{
                           override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {

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
                   }

                   2 ->{
                       sortname = list[2]
                       sortid = 3
                       apiInterface.getSortItem(
                           sortid
                       ).enqueue(object :Callback<MutableList<SortItemData>>{
                           override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {

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
                   }
               }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        sortname = list[0]
        sortid = 1
        apiInterface.getSortItem(
            sortid
        ).enqueue(object :Callback<MutableList<SortItemData>>{
            override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {

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

        adapter = SortItemAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setclickedListener(object :
            SortItemAdapter.clickedListener{
            override fun modifyItemData(id: Int, item_name: String, sort_id: Int, sort_name: String, price: Int, stock: Int, pic: String) {
                if (userLevel.toInt() < 3 && sortid == 3  ){
                    Toast.makeText(this@BuyActivity, "想買齁！請洽管理者 ~ ~ ~", Toast.LENGTH_SHORT).show()
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
//                                        musicplayer.start()
                                        if (!data.data.achevement.isNullOrEmpty()){
                                            val toast = Toast(this@BuyActivity)
                                            toast.setGravity(Gravity.TOP, 0, 50)
                                            toast.duration = Toast.LENGTH_SHORT
                                            toast.view = View.inflate(this@BuyActivity, R.layout.toast_layout,null)
                                            toast.show()
                                        }

                                            apiInterface.getSortItem(
                                                sortid
                                            ).enqueue(object :retrofit2.Callback<MutableList<SortItemData>>{
                                            override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {

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
                                textUserLv.text = "Lv. " + userLevel
                                textUserScore.text = userScore
                                if (userLevel=="5"){
                                    textUserScore2.text = "(" + "0" + ")"
                                } else { textUserScore2.text = "("+ userScore2 + ")" }

                                if (userLevel == "5"){
                                    proBar.max = userScore.toInt()
                                    proBar.setProgress(userScore.toInt(), true)
                                }
                                else{
                                    val usertotall = userScore.toInt() + userScore2.toInt()
                                    proBar.max = usertotall
                                    proBar.setProgress(userScore.toInt(), true)
                                }
                                setPhoto()
                            }
                        }
                    }
                })
            }
        }
        timer.schedule(timerTask, 2500, 2500)

    }

    private fun setPhoto() {
        when (type) {
            "fire" -> {
                when (userLevel) {
                    "0" -> imageViewHome.setImageResource(R.drawable.fire)
                    "1" -> imageViewHome.setImageResource(R.drawable.fire01)
                    "2" -> imageViewHome.setImageResource(R.drawable.fire02)
                    "3" -> imageViewHome.setImageResource(R.drawable.fire03)
                    "4" -> imageViewHome.setImageResource(R.drawable.fire04)
                    "5" -> imageViewHome.setImageResource(R.drawable.fire04)
                }
            }
            "water" -> {
                when (userLevel) {
                    "0" -> imageViewHome.setImageResource(R.drawable.water)
                    "1" -> imageViewHome.setImageResource(R.drawable.water01)
                    "2" -> imageViewHome.setImageResource(R.drawable.water02)
                    "3" -> imageViewHome.setImageResource(R.drawable.water03)
                    "4" -> imageViewHome.setImageResource(R.drawable.water04)
                    "5" -> imageViewHome.setImageResource(R.drawable.water04)
                }
            }
            "grass" -> {
                when (userLevel) {
                    "0" -> imageViewHome.setImageResource(R.drawable.grass)
                    "1" -> imageViewHome.setImageResource(R.drawable.grass01)
                    "2" -> imageViewHome.setImageResource(R.drawable.grass02)
                    "3" -> imageViewHome.setImageResource(R.drawable.grass03)
                    "4" -> imageViewHome.setImageResource(R.drawable.grass04)
                    "5" -> imageViewHome.setImageResource(R.drawable.grass04)
                }
            }
        }

        if (userLevel=="0"){
            imageViewHome.scaleType = ImageView.ScaleType.CENTER
        }
        else imageViewHome.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun onDestroy() {
//        musicplayer.stop()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    val OnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.buy -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.framelayout, fragmentBuy).commit()
                    pageBuy.visibility = View.VISIBLE
                    return true
                }

                R.id.chat -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.framelayout, fragmentChat).commit()
                    pageBuy.visibility = View.GONE
                    bottomNavigation.removeBadge(R.id.chat)

                    if (timercount==0){
                        timerMsg = Timer(true)
                        val msgTimerTask :TimerTask = object :TimerTask(){
                            override fun run() {
                                apiInterface.getMsg(token).enqueue(object : Callback<MsgData> {
                                    override fun onFailure(call: Call<MsgData>, t: Throwable) {
                                        Toast.makeText(this@BuyActivity, "getMsg_OnFailure", Toast.LENGTH_LONG).show()
                                    }
                                    override fun onResponse(call: Call<MsgData>, response: Response<MsgData>) {
                                        if (response.isSuccessful) {
                                            var type2 = 0
                                            val msgRepeatList = response.body()!!.sheepallmsg
                                                .map {
                                                    Sheepallmsg(
                                                        it.id,
                                                        it.sheep_id,
                                                        it.sheep_msg,
                                                        it.wolf_id,
                                                        it.wolf_msg,
                                                        it.created_at,
                                                        it.updated_at
                                                    )
                                                }.sortedBy { it.id }
                                                .reversed()
                                            if (type == "fire"){
                                                type2 = R.drawable.fireicon
                                            } else if (type == "water"){
                                                type2 = R.drawable.watericon
                                            } else if (type == "grass"){
                                                type2 = R.drawable.grassicon
                                            }
                                            if (msgKeepList.isEmpty()){
                                                msgKeepList.addAll(msgRepeatList)
                                                fragmentChat.adapter.update(msgKeepList, type2, userName)
                                            } else if (msgKeepList != msgRepeatList){
                                                msgKeepList.clear()
                                                msgKeepList.addAll(msgRepeatList)
                                                fragmentChat.adapter.update(msgKeepList, type2, userName)
                                                if (bottomNavigation.selectedItemId !== R.id.chat){
                                                    val bottomnavigation:BottomNavigationView=findViewById(R.id.bottomNavigation)
                                                    val badgeDrawable = bottomnavigation.getOrCreateBadge(R.id.chat)
                                                    badgeDrawable.setContentDescriptionNumberless("!")
                                                }

                                            }

                                        }
                                    }
                                })
                            }
                        }
                        timerMsg.schedule(msgTimerTask, 3000, 3000)
                        timercount = 1
                    }

                    return true
                }

                R.id.game -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.framelayout, fragmentGame).commit()
                    pageBuy.visibility = View.GONE
                    val intent = Intent(this@BuyActivity, WaitActivity::class.java)
                    startActivity(intent)
                    return false
                }

                R.id.history -> {
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.framelayout, fragmentHistory).commit()
                    pageBuy.visibility = View.GONE
                    return true
                }

                R.id.logout -> {
                    val dm = DisplayMetrics()
                    this@BuyActivity.windowManager.defaultDisplay.getMetrics(dm)
                    val pixelX = dm.widthPixels
                    val pixelY = dm.heightPixels
                    val logoutInflater = this@BuyActivity.layoutInflater
                    val view = logoutInflater.inflate(R.layout.alert_layout_logout, null)
                    val builder = AlertDialog.Builder(this@BuyActivity, R.style.AlearTheme).setView(view).show()
                    val endTitle = view.findViewById<TextView>(R.id.textViewEndTitle)
                    val endLayout = view.findViewById<ConstraintLayout>(R.id.endLayout)
                    val hitme = view.findViewById<TextView>(R.id.logout)
                    endLayout.layoutParams.width = pixelX
                    endLayout.layoutParams.height = pixelY
                    timerEnd = Timer(true)
                    val endTimerTask :TimerTask = object :TimerTask(){
                        val time = System.currentTimeMillis()
                        val time10 = time + 5000
                        val time15 = time + 10000
                        val time20 = time + 15000
                        override fun run() {
                            hitme.setX((0..pixelX).random().toFloat())
                            hitme.setY((0..pixelY).random().toFloat())
                            runOnUiThread{
                                if (System.currentTimeMillis() > time20){
                                    hitme.text = "m_(._.)_m"
                                    timerEnd.cancel()
                                    hitme.setX(pixelX/2.toFloat())
                                    hitme.setY(pixelY/2.toFloat())
                                    endTitle.visibility = View.GONE
                                }else if(System.currentTimeMillis() > time15){
                                    hitme.text = "過10秒了..."
                                }else if(System.currentTimeMillis() > time10){
                                    hitme.text = "加油好嘛！！！"
                                }
                            }
                        }
                    }
                    timerEnd.schedule(endTimerTask, 500, 500)

                    hitme.setOnClickListener {
                        this@BuyActivity.finish()
                        builder.dismiss()
                    }
                }
            }
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        userBalance = shared.preference.getString("balancce", "")?:""
        textUserBalance.text = userBalance
        bottomNavigation.selectedItemId = R.id.buy
    }

}
