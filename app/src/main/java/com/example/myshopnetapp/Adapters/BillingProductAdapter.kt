package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myshopnetapp.Model.CartProduct
import com.example.myshopnetapp.Utilities.getProductPrice
import com.example.myshopnetapp.databinding.BillingProductsViewItemBinding
class BillingProductAdapter:Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    inner class BillingProductViewHolder(val binding: BillingProductsViewItemBinding):
        ViewHolder(binding.root){

            fun bind (billingProduct: CartProduct){
                binding.apply {
                    Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                    textViewProductCartName.text = billingProduct.product.name
                    textViewBillingProductQuantity.text = billingProduct.quantity.toString()

                    val priceAfterPercentage = billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                    textViewProductCartPrice.text = "$ ${String.format("%.2f",priceAfterPercentage)}"

                    // Display the product size or an empty string if none is selected.
                    textViewCartProductSize.text = billingProduct.selectedSize ?: ""
                    // Clear any existing image in the size selection ImageView.
                    imageCartProductSize.setImageResource(android.R.color.transparent)
                }
            }

    }

    //differ is used to make the recyclerview faster within the app
    private val diffUtil = object: DiffUtil.ItemCallback<CartProduct>(){
        // Check if the items are the same based on their id
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        // Check if the contents of the items are the same
        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }
    //the differ will allow to get and update the list
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val billingProduct = differ.currentList[position]
        holder.bind(billingProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}