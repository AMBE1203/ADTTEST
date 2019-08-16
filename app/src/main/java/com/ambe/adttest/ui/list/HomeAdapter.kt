package com.ambe.adttest.ui.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ambe.adttest.R
import com.ambe.adttest.helper.Const
import com.ambe.adttest.interfaces.IOnClickItemHome
import com.ambe.adttest.model.Homes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_home.view.*
import java.text.SimpleDateFormat

/**
 *  Created by AMBE on 16/8/2019 at 10:13 AM.
 */

class HomeAdapter(var iOnClickItemHome: IOnClickItemHome) : ListAdapter<Homes, HomeAdapter.HomeViewHolder>(HomeDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HomeViewHolder {

        return HomeViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home, parent, false), iOnClickItemHome)
    }

    override fun onBindViewHolder(p0: HomeViewHolder, p1: Int) {
        p0.apply {
            bind(getItem(p1))
        }
    }


    companion object {
        val HomeDiffCallback = object : DiffUtil.ItemCallback<Homes>() {
            override fun areItemsTheSame(p0: Homes, p1: Homes): Boolean {
                return p0.id == p1.id
            }

            override fun areContentsTheSame(p0: Homes, p1: Homes): Boolean {
                return p0 == p1
            }

        }

        private val formatDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private val formatDate1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")


    }

    inner class HomeViewHolder(itemview: View, var iOnClickItemHome: IOnClickItemHome) : RecyclerView.ViewHolder(itemview) {
        fun bind(homes: Homes?) {
            if (homes != null) {

                var date = formatDate1.parse(homes.created_at)

                itemView.txt_create_at.text = formatDate.format(date)
                if (adapterPosition % 2 == 0) {
                    itemView.img_left.visibility = View.VISIBLE
                    itemView.img_right.visibility = View.INVISIBLE


                    Glide.with(itemView.context).load(Const.BASE_URL + homes.media.url).centerCrop().apply(RequestOptions.circleCropTransform()).into(itemView.img_left)

                } else {
                    itemView.img_left.visibility = View.INVISIBLE
                    itemView.img_right.visibility = View.VISIBLE


                    Glide.with(itemView.context).load(Const.BASE_URL + homes.media.url).apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16))).into(itemView.img_right)

                }
                itemView.setOnClickListener { iOnClickItemHome.onClickItem(homes) }

            }

        }


    }

}