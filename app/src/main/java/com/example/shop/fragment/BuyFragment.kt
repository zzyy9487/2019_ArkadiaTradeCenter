package com.example.shop.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shop.APIInterface
import com.example.shop.BuyActivity
import com.example.shop.R
import com.example.shop.getSortItem.SortItemAdapter

/**
 * A simple [Fragment] subclass.
 */
class BuyFragment : Fragment() {

    private lateinit var apiInterface: APIInterface
    lateinit var rootview: View
    lateinit var adapter : SortItemAdapter
    lateinit var sortname:String
    var sortid:Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val act = activity as BuyActivity

        rootview = inflater.inflate(R.layout.fragment_buy, container, false)


//        val list = listOf("糧食", "軍事", "特殊")
//        val spinnerAdapter = ArrayAdapter.createFromResource(act, R.array.options_array, android.R.layout.simple_spinner_item)
//
//        val spinner = rootview.findViewById<Spinner>(R.id.spinner)
//        spinner.adapter = spinnerAdapter
//        adapter = SortItemAdapter()
//        val recyclerView = rootview.findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this.context)
//
//        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
//                sortname = spinner.selectedItem.toString()
//                if (sortname == list[0]){
//                    sortid = 1
//                }
//                else if (sortname == list[1]){
//                    sortid = 2
//                }
//                else if (sortname == list[2]){
//                    sortid = 3
//                }
//
//                val retrofit = Retrofit.Builder()
//                    .baseUrl("http://35.234.60.173")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//                apiInterface = retrofit.create(APIInterface::class.java)
//                apiInterface.getSortItem(
//                    sortid
//                ).enqueue(object :retrofit2.Callback<MutableList<SortItemData>>{
//                    override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                    }
//
//                    override fun onResponse(call: Call<MutableList<SortItemData>>, response: Response<MutableList<SortItemData>>) {
//                        if (response.code() == 200){
//                            val itemList = response.body()!!
//                                .map {
//                                    Item(
//                                        it.id,
//                                        sortname,
//                                        it.item_name,
//                                        it.price,
//                                        it.stock,
//                                        it.pic
//                                    )
//                                }
//                                .sortedBy { it.id }
//                            adapter.update(itemList)
//                        }
//                    }
//                })
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//
//        adapter.setclickedListener(object :SortItemAdapter.clickedListener{
//            override fun modifyItemData(id: Int, item_name: String, sort_id: Int, sort_name: String, price: Int, stock: Int, pic: String) {
//                if (act.userLevel.toInt() < 3 && sortid == 3  ){
//                    Toast.makeText(act, "想買齁！快買東西升到等級3吧！~", Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    val inflater = act.layoutInflater
//                    val view = inflater.inflate(R.layout.alert_layout, null)
//                    val builder = AlertDialog.Builder(act)
//                        .setView(view)
//                        .show()
//                    val itemName = view.findViewById<TextView>(R.id.textViewItem)
//                    val editCount = view.findViewById<EditText>(R.id.editNumber)
//                    val btnBuy = view.findViewById<TextView>(R.id.textBuy)
//                    val btnCancel = view.findViewById<TextView>(R.id.textCancel)
//                    val count = "庫存只剩"+ "$stock"
//                    editCount.hint = count
//                    itemName.text = item_name
//                    btnBuy.setOnClickListener {
//                        if (editCount.text.isEmpty()){
//                            Toast.makeText(act, "請輸入購買數量齁！", Toast.LENGTH_SHORT).show()
//                        }
//                        else if (editCount.text.toString() == "0"){
//                            Toast.makeText(act, "正常人會按取消！", Toast.LENGTH_SHORT).show()
//                        }
//                        else if (editCount.text.toString().toInt() > stock ){
//                            Toast.makeText(act, "超過購買數量，請重新輸入！", Toast.LENGTH_SHORT).show()
//                            editCount.setText("")
//                        }
//                        else if (editCount.text.toString().toInt() * price > act.userBalance.toInt() ){
//                            Toast.makeText(act, "餘額不足哦！請重新輸入！", Toast.LENGTH_SHORT).show()
//                            editCount.setText("")
//                        }
//                        else{
//                            act.buyBody = BuyBody(act.userAccount, id, editCount.text.toString().toInt())
//                            apiInterface.buy(
//                                act.token,
//                                act.buyBody
//                            ).enqueue(object :retrofit2.Callback<BuyData>{
//                                override fun onFailure(call: Call<BuyData>, t: Throwable) {
//                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                                }
//
//                                override fun onResponse(call: Call<BuyData>, response: Response<BuyData>) {
//                                    if (response.code() == 201){
//                                        val data = response.body()
//                                        Toast.makeText(act, data!!.data.item.item_name+"已購買!", Toast.LENGTH_SHORT).show()
//                                        act.userBalance = data.data.balance.toString()
//                                        act.shared.setBalance(act.userBalance)
//                                        act.userScore = data.data.score.toString()
//                                        act.shared.setScore(act.userScore)
//                                        textUserBalance.text = act.userBalance
//                                        textUserScore.text = act.userScore
//                                        act.musicplayer.start()
//
//                                        if(data.data.achevement.isNullOrEmpty()){
//                                        }
//                                        else{
//                                            if (act.water == "0"){
//                                                Toast.makeText(act, data.data.achevement, Toast.LENGTH_LONG).show()
//                                                act.water = "1"
//                                                act.buyBody = BuyBody(act.userAccount, 87, 1)
//                                                apiInterface.buy(
//                                                    act.token,
//                                                    act.buyBody
//                                                ).enqueue(object :retrofit2.Callback<BuyData>{
//                                                    override fun onFailure(call: Call<BuyData>, t: Throwable) {
//
//                                                    }
//
//                                                    override fun onResponse(call: Call<BuyData>, response: Response<BuyData>) {
//
//                                                    }
//                                                })
//                                            }
//                                        }
//                                        apiInterface.getSortItem(
//                                            sortid
//                                        ).enqueue(object :retrofit2.Callback<MutableList<SortItemData>>{
//                                            override fun onFailure(call: Call<MutableList<SortItemData>>, t: Throwable) {
//
//                                            }
//                                            override fun onResponse(call: Call<MutableList<SortItemData>>, response: Response<MutableList<SortItemData>> ) {
//                                                if (response.code() == 200){
//                                                    val itemList = response.body()!!
//                                                        .map {
//                                                            Item(
//                                                                it.id,
//                                                                sortname,
//                                                                it.item_name,
//                                                                it.price,
//                                                                it.stock,
//                                                                it.pic
//                                                            )
//                                                        }
//                                                        .sortedBy { it.id }
//                                                    adapter.update(itemList)
//                                                }
//                                            }
//                                        })
//
//                                        builder.dismiss()
//                                    }
//                                }
//                            })
//                        }
//                    }
//
//                    btnCancel.setOnClickListener {
//                        builder.dismiss()
//                    }
//                }
//            }
//        })

        return rootview
    }


}
