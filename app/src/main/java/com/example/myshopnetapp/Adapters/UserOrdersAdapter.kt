package com.example.myshopnetapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myshopnetapp.Model.Order
import com.example.myshopnetapp.databinding.OrderItemBinding
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myshopnetapp.Model.OrderStatus
import com.example.myshopnetapp.Model.getOrderStatus
import com.example.myshopnetapp.R

class UserOrdersAdapter : Adapter<UserOrdersAdapter.UserOrdersViewHolder>() {

    inner class UserOrdersViewHolder(private val binding: OrderItemBinding): ViewHolder(binding.root){
        fun bind(order:Order){
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resource = itemView.resources

                val colorDrawable = when(getOrderStatus(order.orderStatus)){
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resource.getColor(R.color.colorYellow))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resource.getColor(R.color.colorGreen))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resource.getColor(R.color.colorGreen))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resource.getColor(R.color.colorGreen))
                    }
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resource.getColor(R.color.colorRed))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resource.getColor(R.color.colorRed))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
            }

        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrdersViewHolder {
        return UserOrdersViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: UserOrdersViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            onClick?.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Order) -> Unit)? = null


}