package com.example.tseaafricaapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CookwareInput : Fragment() {




    interface OnCookwareListListener {
        fun onCookwareListUpdate(cookwareList: List<String>)
    }

    private var listener: OnCookwareListListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCookwareListListener) {
            listener = context
        }
    }


    val cookwareList = mutableListOf<String>()
   // private lateinit var cookwareAdapter: CookwareAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cookware_input, container, false)
        val txtCookware: EditText = view.findViewById(R.id.txtCookware)
        val btnAddCookware: Button = view.findViewById(R.id.btnAddCookware)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCookware)
        val imageBtnClose: ImageButton = view.findViewById(R.id.imageBtnClose)

        //cookwareAdapter = CookwareAdapter(cookwareList)
        recyclerView.layoutManager = LinearLayoutManager(context)
      //  recyclerView.adapter = cookwareAdapter

        btnAddCookware.setOnClickListener {
            val cookwareItem = txtCookware.text.toString().trim()
            if (cookwareItem.isNotEmpty()) {
                cookwareList.add(cookwareItem)
//                cookwareAdapter.notifyDataSetChanged()
                txtCookware.text.clear()
            }
        }
        imageBtnClose.setOnClickListener {
            listener?.onCookwareListUpdate(cookwareList)
            // Close the fragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        return view
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}