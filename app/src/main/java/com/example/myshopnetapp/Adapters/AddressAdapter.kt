package com.example.myshopnetapp.Adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myshopnetapp.Model.Address
import com.example.myshopnetapp.R
import com.example.myshopnetapp.databinding.AddressViewItemBinding

class AddressAdapter : Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressViewItemBinding):
        ViewHolder(binding.root){
        fun bind(address: Address, isSelected:Boolean) {
            binding.apply {
                buttonAddress.text = address.houseNumber
                if (isSelected){
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.pinkThemeColor))
                }else{
                    buttonAddress.background = ColorDrawable(itemView.context.resources.getColor(R.color.colorDarkGrey))
                }

            }

        }

    }

    //differ is used to make the recyclerview faster within the app
    private val diffUtil = object: DiffUtil.ItemCallback<Address>(){
        // Check if the items are the same based on their id
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.houseNumber == newItem.houseNumber && oldItem.fullName == newItem.fullName
        }

        // Check if the contents of the items are the same
        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }
    //the differ will allow to get and update the list
    val differ = AsyncListDiffer(this,diffUtil)


    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    var selectedAddress = -1

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if(selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onClick?.invoke(address)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Address) -> Unit)? = null

}