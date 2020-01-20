package com.example.shop.fragment


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.APIInterface
import com.example.shop.BuyActivity
import com.example.shop.getMsg.MsgAdapter

import com.example.shop.R
import com.example.shop.getMsg.MsgData
import com.example.shop.getMsg.Sheepallmsg
import com.example.shop.sendMsg.SendMsgBody
import com.example.shop.sendMsg.SendMsgData
import kotlinx.android.synthetic.main.activity_buy.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    private lateinit var apiInterface: APIInterface
    lateinit var adapter: MsgAdapter
    lateinit var rootview: View
    lateinit var body :SendMsgBody
    var type :Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val act = activity as BuyActivity

        rootview = inflater.inflate(R.layout.fragment_chat, container, false)


        adapter = MsgAdapter()
        val recyclerViewChat = rootview.findViewById<RecyclerView>(R.id.recyclerViewChat)
        recyclerViewChat.layoutManager = LinearLayoutManager(act)
        recyclerViewChat.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.229.181.103")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)

        getMsg(act)

        val addMsg = rootview.findViewById<Button>(R.id.btn_addMsg)
        addMsg.setOnClickListener {
            val chatInflater = act.layoutInflater
            val view = chatInflater.inflate(R.layout.alert_layout_msg, null)
            val builder = AlertDialog.Builder(act)
                            .setView(view)
                            .show()
            val editMsg = view.findViewById<EditText>(R.id.editMsg)
            val btnSend = view.findViewById<TextView>(R.id.textSend)
            val btnCancel = view.findViewById<TextView>(R.id.textCancel)

            btnSend.setOnClickListener {
                if (editMsg.text.isEmpty()){
                    Toast.makeText(act, "請填入訊息...", Toast.LENGTH_LONG).show()
                } else {
                    body = SendMsgBody(editMsg.text.toString())
                    sendMsg(act)
                    getMsg(act)
                }
                builder.dismiss()
            }
            btnCancel.setOnClickListener {
                builder.dismiss()
            }

        }

        return rootview

    }

    private fun sendMsg(act: BuyActivity) {
        apiInterface.sendMsg(act.token, body).enqueue(object : Callback<SendMsgData> {
            override fun onFailure(call: Call<SendMsgData>, t: Throwable) {
                Toast.makeText(act, "sendMsg_OnFailure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<SendMsgData>, response: Response<SendMsgData>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Toast.makeText(act, data!!.msg, Toast.LENGTH_LONG).show()
                    getMsg(act)
                }
            }
        })
    }

    private fun getMsg(act: BuyActivity) {
        apiInterface.getMsg(act.token).enqueue(object : Callback<MsgData> {
            override fun onFailure(call: Call<MsgData>, t: Throwable) {
                Toast.makeText(act, "getMsg_OnFailure", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MsgData>, response: Response<MsgData>) {
                if (response.isSuccessful) {
                    val msgList = response.body()!!.sheepallmsg
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
                    if (act.type == "fire"){
                        type = R.drawable.fireicon
                    } else if (act.type == "water"){
                        type = R.drawable.watericon
                    } else if (act.type == "grass"){
                        type = R.drawable.grassicon
                    }
                    adapter.update(msgList, type, act.userName)
                }
            }
        })
    }


}
