package com.os.daviscupindia.ui.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.os.daviscupindia.databinding.GalleryItmeBinding
import com.os.daviscupindia.ui.home.model.GalleryData


class RvGalleryAdapter(
    var itemsViewModel: ArrayList<GalleryData>,
    var mcontext: Context,
) : RecyclerView.Adapter<RvGalleryAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: GalleryItmeBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
                    .asBitmap()
                    .load(url)
                    .thumbnail(0.3f)
                    .listener(object: RequestListener<Bitmap> {
                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding?.imageview.setImageBitmap(resource)
                            return true
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    })
                    .preload()
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return itemsViewModel.size
    }
}
