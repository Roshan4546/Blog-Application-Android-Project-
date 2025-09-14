package com.example.blogapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapplication.Model.BlogItemModel
import com.example.blogapplication.databinding.BlogItemBinding

class BlogAdapter(
    private val items: List<BlogItemModel>
) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding = BlogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(items[position]) // Pass listener
    }

    override fun getItemCount(): Int = items.size

    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(blogItemModel: BlogItemModel) {
            binding.heading2.text = blogItemModel.heading2
            binding.username2.text = blogItemModel.username2
            binding.date2.text = blogItemModel.date2
            binding.post2.text = blogItemModel.post2
            binding.likeCount2.text = blogItemModel.likeCounts2.toString()
            
        }
    }
}