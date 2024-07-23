package com.soni.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soni.R
import com.soni.services.web.models.Propertyassets
import java.io.File

class ImagesAdapter(
    val context: Context,
    val imageList: ArrayList<Propertyassets>,
    var allowAdd: Boolean = true,
    var CallDelete: ((id: String) -> Unit)
) : RecyclerView.Adapter<ImagesAdapter.ImagesAdapterViewHolder>() {
    class ImagesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImg = itemView.findViewById<ImageView>(R.id.iv_adapter_images)
        val ivDeleteImg = itemView.findViewById<ImageView>(R.id.iv_delete_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_adapter, parent, false)
        return ImagesAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImagesAdapterViewHolder, position: Int) {

        val photoUri: Uri = Uri.fromFile(File(imageList[position].assetUrl!!))

//        Glide.with(context)
//            .load(stringUri)
//            .into(holder.ivImg)
//        if (!allowAdd) {
//            holder.ivDeleteImg.visibility = View.INVISIBLE
//        }
        holder.ivDeleteImg.setOnClickListener {
            if (imageList[position].assetUrl!!.contains("https")) {
                CallDelete(imageList[position].id.toString())
            }
            imageList.remove(imageList[position])
            notifyItemRemoved(position)
        }
        Log.d("logg1", imageList[position].assetUrl.toString())
//        val photoUri: Uri = Uri.fromFile(File(imageList[position].asset_url!!))

        Glide.with(context).load(imageList[position].assetUrl.toString())
            .placeholder(R.drawable.logo).error(R.drawable.logo).into(holder.ivImg)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}