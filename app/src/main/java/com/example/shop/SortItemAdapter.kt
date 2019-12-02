package com.example.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shop.getSortItem.Item
import kotlinx.android.synthetic.main.cell_layout.view.*

class SortItemAdapter:RecyclerView.Adapter<SortItemAdapter.ViewHolder>() {

    val list = listOf<String>("糧食", "軍事", "特殊")
    var sortid :Int = 0

    private var inputList = mutableListOf<Item>()
    private var itemClickListener: clickedListener? = null

    interface clickedListener{
        fun modifyItemData(id:Int, item_name:String, sort_id:Int, sort_name:String, price:Int, stock:Int ,pic:String)
    }

    fun setclickedListener(checkedListener: clickedListener){
        this.itemClickListener = checkedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return inputList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindViewHolder(inputList[position])

    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val pic = itemView.imagePic
        val name = itemView.textName
        val price = itemView.textPrice
        val stock = itemView.textStock
        val sort = itemView.textSort

        fun bindViewHolder(item: Item){

            Glide.with(itemView).load(item.pic)
                .transform(RoundedCorners(5))
                .into(pic)
            name.text = item.item_name
            price.text = item.price.toString()
            stock.text = item.stock.toString()
            sort.text = item.sort_name

            if (item.stock == 0){
                itemView.alpha = 0.5.toFloat()
            } else {
                itemView.alpha = 1.0.toFloat()
            }

            if (item.stock <= 0){
                itemView.setOnClickListener {
                }
            }
            else{
                itemView.setOnClickListener {
                    if (item.sort_name == "糧食") {
                        sortid=1
                        itemClickListener?.modifyItemData(item.id, item.item_name, sortid, item.sort_name, item.price, item.stock ?: 0, item.pic ?: "")
                    }
                    else if(item.sort_name == "軍事") {
                        sortid = 2
                        itemClickListener?.modifyItemData(item.id, item.item_name, sortid, item.sort_name, item.price, item.stock ?: 0, item.pic ?: "")
                    }
                    else if(item.sort_name == "特殊") {
                        sortid = 3
                        itemClickListener?.modifyItemData(item.id, item.item_name, sortid, item.sort_name, item.price, item.stock ?: 0, item.pic ?: "")
                    }
                }
            }






        }
    }


    fun update(newList: MutableList<Item>){
        inputList = newList
        notifyDataSetChanged()
    }

}
