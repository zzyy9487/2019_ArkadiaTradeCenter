package com.example.shop.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shop.R
import kotlinx.android.synthetic.main.cell_layout.view.*

class RecordAdapter:RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    private var inputList = listOf<RecordCell>()
    private var itemClickListener: clickedListener? = null

    interface clickedListener{
        fun worldEnd()
    }

    fun setclickedListener(checkedListener: clickedListener){
        this.itemClickListener = checkedListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_layout_record, parent, false)

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

        fun bindViewHolder(recordCell: RecordCell){

            Glide.with(itemView).load(recordCell.pic)
                .transform(RoundedCorners(5))
                .into(pic)
            name.text = recordCell.item_name
            price.text = recordCell.price.toString()
            stock.text = recordCell.stock.toString()
            sort.text = recordCell.sort_name
            if (name.text == "深水炸彈"){
                itemView.setOnClickListener {
                    itemClickListener?.worldEnd()
                }
            }
        }
    }

    fun update(newList: List<RecordCell>){
        inputList = newList
        notifyDataSetChanged()
    }

}
