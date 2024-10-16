package com.example.tseaafricaapp

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

class InstructionInput : Fragment() {
    private val instructionList = mutableListOf<String>()
   // private lateinit var instructionAdapter: InstructionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instruction_input, container, false)

        val txtInstruction: EditText = view.findViewById(R.id.txtInstruction)
        val btnAddInstruction: Button = view.findViewById(R.id.btnAddInstruction)

        val imageBtnClose: ImageButton = view.findViewById(R.id.imageBtnClose)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewInstruction)

       // instructionAdapter = InstructionAdapter(instructionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        //recyclerView.adapter = instructionAdapter


        imageBtnClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        btnAddInstruction.setOnClickListener {
            val instructionText = txtInstruction.text.toString().trim()

            if (instructionText.isNotEmpty()) {
                // Add instruction to the list
                instructionList.add(instructionText)

                // Notify the adapter that the data has changed
              //  instructionAdapter.notifyDataSetChanged()

                // Clear the input box
                txtInstruction.text.clear()
            }
        }
        return view
    }
}