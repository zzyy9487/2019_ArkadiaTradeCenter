package com.example.shop.getMsg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shop.R
import kotlinx.android.synthetic.main.cell_layout_msg.view.*

class MsgAdapter:RecyclerView.Adapter<MsgAdapter.ViewHolder>() {

    var whattype :Int = 0
    var whatname :String = ""
    private var inputList = listOf<Sheepallmsg>()
//    private var itemClickListener: clickedListener? = null
//
//    interface clickedListener{
//        fun modifyItemData(id:Int, item_name:String, sort_id:Int, sort_name:String, price:Int, stock:Int ,pic:String)
//    }
//
//    fun setclickedListener(checkedListener: clickedListener){
//        this.itemClickListener = checkedListener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_layout_msg, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return inputList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindViewHolder(inputList[position])

    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val userName = itemView.textChatUserName
        val userImage = itemView.imageChatUser
        val userSay = itemView.textUserSay
        val masterName = itemView.textChatMasterName
        val masterimage = itemView.imageChatMaster
        val masterSay = itemView.textChatMasterSay

        fun bindViewHolder(sheepallmsg: Sheepallmsg){

            Glide.with(itemView).load(whattype)
                .transform(CircleCrop())
                .into(userImage)
            userName.text = whatname
            userSay.text = sheepallmsg.sheep_msg

            Glide.with(itemView).load(R.drawable.kaka)
                .transform(CircleCrop())
                .into(masterimage)
            masterName.text = "Pikachu"
            masterSay.text = sheepallmsg.wolf_msg?:"尚未回覆"
            if (masterSay.text == "尚未回覆"){
                Glide.with(itemView).load(R.drawable.wolf)
                    .transform(RoundedCorners(20))
                    .into(masterimage)
                masterName.text = "Wolf"
            }

        }
    }

    fun update(newList: List<Sheepallmsg>, type:Int, name:String){
        inputList = newList
        whattype = type
        whatname = name
        notifyDataSetChanged()
    }

}
