package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshopnetapp.Model.CartProduct
import com.example.myshopnetapp.Utilities.getProductPrice
import com.example.myshopnetapp.databinding.CartProductItemBinding

class CartProductAdapter: RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    //the inner class was use to display cart products in the recycleView
    //binding is used to add product's image, name and prices in specific location
    inner class CartProductViewHolder( val binding: CartProductItemBinding): RecyclerView.ViewHolder(binding.root){
        // Bind the data from a given CartProduct object to this ViewHolder's views.
        fun bind(cartProduct: CartProduct){
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(imageViewCartProduct)
                textViewProductNameAddToCart.text = cartProduct.product.name
                textViewProductQuantity.text = cartProduct.quantity.toString()
                // Calculate the product price after any percentage discount and format it as a string with two decimal places.
                val priceAfterPercentage = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                textViewProductPriceAddToCart.text = "£ ${String.format("%.2f",priceAfterPercentage)}"

                // Display the product size or an empty string if none is selected.
                textViewProductCartSize.text = cartProduct.selectedSize ?: ""
                // Clear any existing image in the size selection ImageView.
                imageViewProductSize.setImageResource(android.R.color.transparent)
            }
        }

    }

    //differ is used to make the recyclerview faster within the app
    private val diffCallback = object: DiffUtil.ItemCallback<CartProduct>(){

        // Check whether two CartProduct objects represent the same item.
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        // Check whether two CartProduct objects have the same contents.
        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem ==newItem
        }
    }
    //the differ will allow to get and update the list
    val differ = AsyncListDiffer(this,diffCallback)

    // Create a new ViewHolder to represent a CartProduct row.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    // Bind the data from a CartProduct object to a given ViewHolder's views.
    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        // Set up click listeners for the CartProduct row, and invoke the appropriate lambda when clicked.
        holder.itemView.setOnClickListener{
            onProductClick?.invoke(cartProduct)
        }

        holder.binding.imageViewPlusIcon.setOnClickListener{
            onPlusClick?.invoke(cartProduct)
        }

        holder.binding.imageViewMinusIcon.setOnClickListener{
            onMinusClick?.invoke(cartProduct)
        }

    }

    // Return the number of items in the current list of CartProduct objects.
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Callbacks for when the item view, plus icon, and minus icon are clicked.
    // Each callback is a lambda expression that takes a cart product object as its argument and returns Unit.
    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null
}