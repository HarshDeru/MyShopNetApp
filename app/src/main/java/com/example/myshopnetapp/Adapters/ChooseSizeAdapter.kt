package com.example.myshopnetapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopnetapp.databinding.ChooseProductSizeItemBinding

// Define an adapter class for the RecyclerView
class ChooseSizeAdapter:RecyclerView.Adapter<ChooseSizeAdapter.ChooseSizeViewHolder>() {

    private var selectedPosition = -1

    // Define a ViewHolder class for each item in the RecyclerView
    inner class ChooseSizeViewHolder(private val binding : ChooseProductSizeItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(size:String, position:Int){
                    //if size is selected then display textview as ticked else hide ticked
                    binding.textViewSize.text = size
                    if(position == selectedPosition){
                        binding.apply{
                            sizeShadow.visibility = View.VISIBLE
                        }
                    } else {
                        binding.apply {
                            sizeShadow.visibility = View.INVISIBLE
                        }
                    }
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ChooseSizeViewHolder{
        return ChooseSizeViewHolder(
            ChooseProductSizeItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    // Bind the data to the ViewHolder for each item in the RecyclerView
    override fun onBindViewHolder(holder: ChooseSizeViewHolder, position: Int){
        val size = differ.currentList[position]
        holder.bind(size, position)
        //if the selected position is greater or equal to 0 then notify the item
        //this way i will be able to select and unselect items.
        holder.itemView.setOnClickListener{
            if(selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)

        }
    }

    // Return the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Define an onClick listener for the items in the RecyclerView
    var onItemClick: ((String) -> Unit)? = null
}