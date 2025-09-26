package com.example.blogapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.blogapplication.databinding.ActivityEditBinding
import com.google.firebase.database.FirebaseDatabase

class EditActivity : AppCompatActivity() {
    private val binding: ActivityEditBinding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val blogItemModel = intent.getParcelableExtra<BlogItemModel>("blogItem")

        binding.blogTitle.editText?.setText(blogItemModel?.heading2)
        binding.blogDescription.editText?.setText(blogItemModel?.post2)

        binding.addbutton.setOnClickListener{
            val upDatedTitle = binding.blogTitle.editText.toString().trim()
            val upDatedDescription = binding.blogDescription.editText.toString().trim()


            if(upDatedTitle.isEmpty() && upDatedDescription.isEmpty()){
                Toast.makeText(this, "Please Fill all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                blogItemModel?.heading2 = upDatedTitle
                blogItemModel?.post2 = upDatedDescription

                if(blogItemModel != null){
                    updatedDatabase(blogItemModel)
                }
            }
        }
    }

    private fun updatedDatabase(blogItemModel: BlogItemModel) {
        val databaseRefer = FirebaseDatabase.getInstance("https://blog-app-35da3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("blogs")
        val postId = blogItemModel.postId

        if (postId != null) {
            databaseRefer.child(postId).setValue(blogItemModel)
                .addOnSuccessListener {
                    Toast.makeText(this, "Blog Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Blog Updated Unsuccessfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
        }
    }
}