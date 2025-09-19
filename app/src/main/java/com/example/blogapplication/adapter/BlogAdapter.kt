package com.example.blogapplication.adapter

//import BlogItemModel
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapplication.BlogItemModel
import com.example.blogapplication.R
import com.example.blogapplication.ReadMoreActivity

class BlogAdapter(private val blogList: List<BlogItemModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blog_item, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]
        holder.title.text = blog.heading2 ?: ""
        holder.username.text = blog.username2 ?: ""
        holder.date.text = blog.date2 ?: ""
        holder.description.text = blog.post2 ?: ""
        holder.likeCount.text = blog.likeCounts2?.toString() ?: "0"

        // ✅ Correct onClick
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReadMoreActivity::class.java)
            intent.putExtra("blogItem", blog) // blog is Parcelable now
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = blogList.size

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.heading2)
        val username: TextView = itemView.findViewById(R.id.username2)
        val date: TextView = itemView.findViewById(R.id.date2)
        val description: TextView = itemView.findViewById(R.id.post2)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount2)
    }
}