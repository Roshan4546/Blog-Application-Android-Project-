package com.example.blogapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogapplication.adapter.ArticleAdpater
import com.example.blogapplication.adapter.BlogAdapter
import com.example.blogapplication.databinding.ActivityAddArticleBinding
import com.example.blogapplication.databinding.ActivityArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticleActivity : AppCompatActivity() {
    private val binding: ActivityArticleBinding by lazy {
        ActivityArticleBinding.inflate(layoutInflater);
    }
    private lateinit var databaseRefer: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var blogAdapter: ArticleAdpater
    private val Edit_Blog_Req = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val curruserId = auth.currentUser?.uid
        val recyclerView = binding.articleRecycle
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (curruserId != null) {


            blogAdapter =
                ArticleAdpater(this, emptyList(), object : ArticleAdpater.OnItemClickListener {
                    override fun onEdit(blogItem: BlogItemModel) {
                        val intent = Intent(this@ArticleActivity, EditActivity::class.java)
                        intent.putExtra("blogItem", blogItem)
                        startActivityForResult(intent, Edit_Blog_Req)
                    }

                    override fun onRead(blogItem: BlogItemModel) {
                        val intent = Intent(this@ArticleActivity, ReadMoreActivity::class.java)
                        intent.putExtra("blogItem", blogItem)
                        startActivity(intent)
                    }

                    override fun onDelete(blogItem: BlogItemModel) {
                        deleteBlogPost(blogItem)
                    }

                })
        }
        recyclerView.adapter = blogAdapter
        // get data from Database

        databaseRefer =
            FirebaseDatabase.getInstance("https://blog-app-35da3-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("blogs")
        databaseRefer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blogSavedList = ArrayList<BlogItemModel>()
                for (postSnapshot in snapshot.children) {
                    val blogSaved = postSnapshot.getValue(BlogItemModel::class.java)
                    if (blogSaved != null && curruserId == blogSaved.userId) {
                        blogSavedList.add(blogSaved)
                    }
                }
                blogAdapter.setData(blogSavedList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ArticleActivity,
                    "Error loading Saved Blogs",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun deleteBlogPost(blogItem: BlogItemModel) {
        val postId = blogItem.postId
        if (postId != null) {
            val blogPostRef = databaseRefer.child(postId)
            blogPostRef.removeValue().addOnSuccessListener {
                Toast.makeText(this, "Blog Post Deleted Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Blog Post Deleted unSuccessfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

}