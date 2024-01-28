package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.databinding.BestDealsViewItemBinding

// Define an adapter class for the RecyclerView
class BDealProductAdapters: RecyclerView.Adapter<BDealProductAdapters.BestDealProductsViewHolder>() {

    // Define a ViewHolder class for each item in the RecyclerView
    inner class BestDealProductsViewHolder(private val binding: BestDealsViewItemBinding): ViewHolder(binding.root){
        //the fun below call in all the product details and add them in specified location
        fun bind (product: Product) {
            binding.apply{
                Glide.with(itemView).load(product.images[0]).into(imageBestDeal)
                product.offerPercentage?.let{
                    val remainingPricePercentage = 1F -it
                    val priceAfterOffer = remainingPricePercentage * product.price
                    textViewNewPrice.text = "£ ${String.format("%.2f",priceAfterOffer)}"
                }
                textViewOldPrice.text = "£ ${product.price}"
                textViewDealProductName.text = product.name
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealProductsViewHolder {
        return BestDealProductsViewHolder(
            BestDealsViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    // Bind the data to the ViewHolder for each item in the RecyclerView
    override fun onBindViewHolder(holder: BestDealProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        // Set an onClick listener for each item in the RecyclerView
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