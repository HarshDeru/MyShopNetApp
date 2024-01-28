package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.databinding.ViewPageImageItemBinding

// Define an adapter class for the RecyclerView
class viewPagerImages: RecyclerView.Adapter<viewPagerImages.ViewPagerImageViewHolder>(){

    // Define a ViewHolder class for each item in the RecyclerView
    inner class ViewPagerImageViewHolder(val binding:ViewPageImageItemBinding) : ViewHolder(binding.root){

        //the fun below call in all the product details and add them in specified location
        fun bind (imagePath: String) {
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)
        }
    }

    //differ is used to make the recyclerview faster within the app
    private val diffCallback = object: DiffUtil.ItemCallback<String>(){
        // Check if the items are the same based on their id
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        // Check if the contents of the items are the same
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem ==newItem
        }
    }

    //the differ will allow to get and update the list
    val differ = AsyncListDiffer(this,diffCallback)

    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ViewPageImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    // Bind the data to the ViewHolder for each item in the RecyclerView
    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    // Return the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}