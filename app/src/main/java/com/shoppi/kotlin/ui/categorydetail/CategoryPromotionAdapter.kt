package com.shoppi.kotlin.ui.categorydetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shoppi.kotlin.databinding.ItemCategoryPromotionBinding
import com.shoppi.kotlin.model.Product
import com.shoppi.kotlin.ui.common.ProductClickListener

class CategoryPromotionAdapter(private val clickListener: ProductClickListener) :
        ListAdapter<Product, CategoryPromotionAdapter.CategoryPromotionViewHolder>(
            ProductDiffCallback()
        ) {

    interface OnItemClickListener {
        fun onItemClick(v: View, productId: String)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class CategoryPromotionViewHolder(private val binding: ItemCategoryPromotionBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product?) {
            binding.clickListener = clickListener
            binding.product = product
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CategoryPromotionViewHolder {
        val binding = ItemCategoryPromotionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryPromotionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CategoryPromotionViewHolder, position: Int
    ) {
        holder.bind(getItem(position))

    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(
        oldItem: Product, newItem: Product
    ): Boolean {
        return oldItem == newItem
    }
}
