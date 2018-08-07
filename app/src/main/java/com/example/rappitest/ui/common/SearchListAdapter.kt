package com.example.rappitest.ui.common

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.rappitest.AppExecutors
import com.example.rappitest.R
import com.example.rappitest.databinding.SearchItemBinding
import com.example.rappitest.vo.ProductionBaseData

/**
 * A RecyclerView adapter for ProductionBaseData class.
 */
class SearchListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val showFullName: Boolean,
        private val clickCallback: ((ProductionBaseData) -> Unit)?
) : DataBoundListAdapter<ProductionBaseData, SearchItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<ProductionBaseData>() {
            override fun areItemsTheSame(oldItem: ProductionBaseData, newItem: ProductionBaseData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductionBaseData, newItem: ProductionBaseData): Boolean {
                return oldItem.overview == newItem.overview
            }
        }
) {

    override fun createBinding(parent: ViewGroup): SearchItemBinding {
        val binding = DataBindingUtil.inflate<SearchItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.search_item,
                parent,
                false,
                dataBindingComponent
        )
        binding.showFullName = showFullName
        binding.root.setOnClickListener {
            binding.item?.let {
                clickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: SearchItemBinding, item: ProductionBaseData) {
        binding.item = item
    }
}
