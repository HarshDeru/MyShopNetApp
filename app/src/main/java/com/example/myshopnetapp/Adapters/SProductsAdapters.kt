package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.databinding.SpecialViewItemBinding

// Define an adapter class for the RecyclerView
class SProductsAdapters: RecyclerView.Adapter<SProductsAdapters.SProductsViewHolder>() {

    //the inner class was use to display special products in the recycleView of home's special product
    //binding is used to add product's image, name and prices in specific location
    inner class SProductsViewHolder(private val binding: SpecialViewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imageSpecialProduct)
                textViewAdName.text = product.name
                textViewAdPrice.text = product.price.toString()
                }
            }

        }

    //differ is used to make the recyclerview faster within the app
    private val diffCallback = object: DiffUtil.ItemCallback<Product>(){
        // Check if the items are the same based on their id
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if the contents of the items are the same
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem ==newItem
        }
    }
    //the differ will allow to get and update the list
    val differ = AsyncListDiffer(this,diffCallback)

    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SProductsViewHolder {
        return SProductsViewHolder(
            SpecialViewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    // Bind the data to the ViewHolder for each item in the RecyclerView
    override fun onBindViewHolder(holder: SProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    // Return the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Define an onClick listener for the items in the RecyclerView
    var onClick: ((Product) -> Unit)? = null

}