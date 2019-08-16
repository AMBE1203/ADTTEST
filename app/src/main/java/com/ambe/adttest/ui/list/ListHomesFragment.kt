package com.ambe.adttest.ui.list


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.ambe.adttest.R
import com.ambe.adttest.helper.Const
import com.ambe.adttest.helper.State
import com.ambe.adttest.helper.Utils
import com.ambe.adttest.interfaces.IOnClickItemHome
import com.ambe.adttest.model.Homes
import com.ambe.adttest.ui.BaseViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list_home.*


/**
 * A simple [Fragment] subclass.
 *
 */
class ListHomesFragment : Fragment(), IOnClickItemHome {
    override fun onClickItem(homes: Homes) {

        var bundle = Bundle()
        bundle.putParcelable(Const.HOME, homes)

        findNavController().navigate(R.id.action_listHomesFragment_to_detailFragment, bundle)
    }

    private lateinit var viewModel: ListHomeViewModel
    private lateinit var adapter: HomeAdapter
    private val TAG = ListHomesFragment::class.java.simpleName


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_home, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListHomeViewModel::class.java)
        setupUI()


    }

    private fun setObserveLive(viewModel: BaseViewModel) {
        viewModel.eventLoading.observe(this, Observer {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    if (it.peekContent()) {

                        progress_bar.visibility = View.VISIBLE

                    } else {
                        progress_bar.visibility = View.INVISIBLE

                    }
                }
            }
        })

        viewModel.eventError.observe(this, Observer {
            if (it != null) {

                txt_error.visibility = if (it.peekContent()) View.VISIBLE else View.INVISIBLE

            }

        })




        viewModel.eventFailure.observe(this, Observer {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    showFailure(it.peekContent())
                }
            }
        })
    }

    private fun showFailure(throwable: Throwable) {
        if (throwable.message != null) {

            txt_error.visibility = View.VISIBLE

            Log.e(TAG, throwable.message)
        } else {
            txt_error.visibility = View.INVISIBLE

        }
    }

    private fun setupUI() {

        if (!Utils.checkInternetConnection(context!!)) {
            Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    Const.CHECK_NETWORK_ERROR,
                    Snackbar.LENGTH_SHORT
            )
                    .show()
        }


        adapter = HomeAdapter(this)
        rcv_product.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcv_product.adapter = adapter

        viewModel.getLishHomes().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it?.homes)
        })

        setObserveLive(viewModel)

        txt_error.setOnClickListener { viewModel.retry() }

    }


}
