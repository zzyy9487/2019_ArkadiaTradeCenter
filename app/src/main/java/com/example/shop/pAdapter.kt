package com.example.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.shop.getSortItem.Item
import com.example.shop.getSortItem.SortItemAdapter

class pAdapter(val context: Context):PagerAdapter(){

    val titlelist = listOf("糧食", "軍事", "特殊")
    val adapter = SortItemAdapter()
    lateinit var viewPagerRecyclerView:RecyclerView

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        return super.instantiateItem(container, position)
        val view = LayoutInflater.from(context).inflate(R.layout.viewpager_layout, container, false)
        viewPagerRecyclerView = view.findViewById(R.id.recyclerViewPager)
        viewPagerRecyclerView.layoutManager = LinearLayoutManager(context)
        viewPagerRecyclerView.adapter = adapter
        container.addView(view)
        return view
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titlelist[position]
    }

    override fun getCount(): Int {
        return titlelist.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}