package com.ambe.adttest.ui.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ambe.adttest.R
import com.ambe.adttest.helper.Const
import com.ambe.adttest.model.Homes
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {

    private var homeSelected: Homes? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeSelected = arguments?.getParcelable(Const.HOME)

        if (homeSelected != null) {
            Glide.with(context!!).load(Const.BASE_URL+homeSelected?.media?.url).into(img_deltail)
            txt_des.text = homeSelected?.created_at
        }
    }


}
