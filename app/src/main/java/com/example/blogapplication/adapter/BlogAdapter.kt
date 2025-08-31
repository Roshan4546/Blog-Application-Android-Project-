package com.example.blogapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.blogapplication.Model.BlogItemModel
import com.example.blogapplication.R
import com.example.blogapplication.databinding.ActivityWelcomeBinding
import com.example.blogapplication.databinding.BlogItemBinding

class BlogAdapter(private val items: List<BlogItemModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: BlogItemBinding = BlogItemBinding.inflate(inflater, parent, false)
        return BlogViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItemModel: BlogItemModel) {
            binding.heading.text = blogItemModel.heading
//            Glide.with(binding.profileimage2.context)
//                .load(blogItemModel.imageUrl)
//                .placeholder(R.drawable.p1) // fallback while loading
//                .error(R.drawable.p1)       // fallback if error
//                .into(binding.profileimage2)
            binding.username.text = blogItemModel.userName
            binding.date.text = blogItemModel.date
            binding.post.text = blogItemModel.post
            binding.likeCount?.text = blogItemModel.likecounts.toString()


        }

    }


}