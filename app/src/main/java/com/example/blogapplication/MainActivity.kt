package com.example.blogapplication

import android.content.res.ColorStateList
import android.graphics.Color   // ✅ Correct import
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapplication.Model.BlogItemModel
import com.example.blogapplication.adapter.BlogAdapter
import com.example.blogapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BlogAdapter.OnItemClickListener {

    private lateinit var blogAdapter: BlogAdapter
    private lateinit var recyclerView: RecyclerView
    private val blogs = mutableListOf<BlogItemModel>()
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ✅ Set FAB background white
        binding.floatingActionButton.backgroundTintList =
            ColorStateList.valueOf(Color.WHITE)

        // ✅ If you want the icon to be black:
        binding.floatingActionButton.imageTintList =
            ColorStateList.valueOf(Color.BLACK)
    }

    override fun onReadMoreClick(blog: BlogItemModel) {
        Toast.makeText(this, "Read More: ${blog.heading2}", Toast.LENGTH_SHORT).show()
    }

    override fun onLikeClick(blog: BlogItemModel, likeCountView: TextView) {
        blog.likeCounts2 += 1
        likeCountView.text = blog.likeCounts2.toString()
        Toast.makeText(this, "Liked: ${blog.heading2}", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveClick(blog: BlogItemModel) {
        Toast.makeText(this, "Saved: ${blog.heading2}", Toast.LENGTH_SHORT).show()
    }
}
