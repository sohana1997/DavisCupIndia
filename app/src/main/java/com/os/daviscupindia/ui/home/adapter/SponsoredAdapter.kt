package com.os.daviscupindia.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.os.daviscupindia.databinding.SponsoredItmeBinding
import com.os.daviscupindia.ui.home.model.Sponsor


abstract class SponsoredAdapter(
    var itemsViewModel: ArrayList<Sponsor>,
    var mcontext: Context,
) : RecyclerView.Adapter<SponsoredAdapter.ViewHolder>() {
    abstract fun load()

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: SponsoredItmeBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SponsoredItmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(itemsViewModel[position]){
                Glide.with(mcontext)
                    .load(image)
                    .into(binding.appImg);
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return itemsViewModel.size
    }
}
