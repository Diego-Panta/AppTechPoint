package com.example.apptechpoint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class StoresAdapter(
    private val stores: List<ComputerStore>,
    private val onStoreClick: (ComputerStore) -> Unit
) : RecyclerView.Adapter<StoresAdapter.StoreViewHolder>() {

    class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view.findViewById(R.id.store_card)
        val nameTextView: TextView = view.findViewById(R.id.tv_store_name)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_store_description)
        val storesTextView: TextView = view.findViewById(R.id.tv_store_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_store, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]

        holder.nameTextView.text = store.name
        holder.descriptionTextView.text = store.description
        holder.storesTextView.text = store.stores.joinToString(", ")

        holder.cardView.setOnClickListener {
            onStoreClick(store)
        }
    }

    override fun getItemCount(): Int = stores.size
}