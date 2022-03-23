package com.os.daviscupindia.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.text.Html
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.os.daviscupindia.WebViewActivity
import com.os.daviscupindia.databinding.HeadlineNewsItmeBinding
import com.os.daviscupindia.ui.home.model.New
import java.text.SimpleDateFormat

import java.util.*


class NewsAdapter(
    var itemsViewModel: ArrayList<New>,
    var mcontext: Context,
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: HeadlineNewsItmeBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HeadlineNewsItmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                    .into(binding.img);



                binding.time.text = getDate(created_at)

                binding.dis.text = Html.fromHtml(description)
                binding?.root.setOnClickListener {
                    mcontext.startActivity(Intent(mcontext , WebViewActivity::class.java).putExtra("url",url).putExtra("title",""))

                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return itemsViewModel.size
    }
}

private fun getDate(date: String?): String? {
    return try {
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()
        )
        val tempDate = format.parse(date)
        DateFormat.format("EEEE, d MMM, ''yyyy", tempDate).toString()
    } catch (e: Exception) {
        ""
    }
}
//"EEE, MMM d, ''yy"
//EEE, dd MMM yyyy