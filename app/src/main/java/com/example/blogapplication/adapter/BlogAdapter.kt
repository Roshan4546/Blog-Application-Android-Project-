package com.example.blogapplication.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapplication.BlogItemModel
import com.example.blogapplication.R
import com.example.blogapplication.ReadMoreActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogAdapter(private val blogList: List<BlogItemModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    private val databaseReference = FirebaseDatabase.getInstance(
        "https://blog-app-35da3-default-rtdb.asia-southeast1.firebasedatabase.app/"
    ).reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blog_item, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blog = blogList[position]
        val postId = blog.postId

        holder.title.text = blog.heading2 ?: ""
        holder.username.text = blog.username2 ?: ""
        holder.date.text = blog.date2 ?: ""
        holder.description.text = blog.post2 ?: ""
        holder.likeCount.text = blog.likeCounts2.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReadMoreActivity::class.java)
            intent.putExtra("blogItem", blog)
            context.startActivity(intent)
        }

        val currUser = FirebaseAuth.getInstance().currentUser
        if (postId != null && currUser != null) {
            val postLikeReference = databaseReference.child("blogs").child(postId).child("likes")
            postLikeReference.child(currUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.likeButton.setImageResource(R.drawable.red_heart)
                    } else {
                        holder.likeButton.setImageResource(R.drawable.black_heart)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            holder.likeButton.setImageResource(R.drawable.black_heart)
        }

        holder.likeButton.setOnClickListener {
            val currUserNow = FirebaseAuth.getInstance().currentUser
            if (currUserNow != null && postId != null) {
                handleLikeButton(postId, blog, holder, currUserNow.uid)
            } else {
                Toast.makeText(holder.itemView.context, "You have to login first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLikeButton(postId: String, blog: BlogItemModel, holder: BlogViewHolder, currUserId: String) {
        val userLikesRef = databaseReference.child("users").child(currUserId).child("likes")
        val postLikesRef = databaseReference.child("blogs").child(postId).child("likes")

        postLikesRef.child(currUserId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userLikesRef.child(postId).removeValue().addOnSuccessListener {
                        postLikesRef.child(currUserId).removeValue()
                        blog.likedBy?.remove(currUserId)
                        updateLikeButton(holder, false)

                        val newLikeCount = blog.likeCounts2 - 1
                        blog.likeCounts2 = newLikeCount
                        databaseReference.child("blogs").child(postId).child("likeCounts2").setValue(newLikeCount)
                        holder.likeCount.text = newLikeCount.toString()
                        notifyDataSetChanged()
                    }.addOnFailureListener { e ->
                        Log.e("likeClicked", "Failed to unlike blog: $e")
                    }
                } else {
                    userLikesRef.child(postId).setValue(true).addOnSuccessListener {
                        postLikesRef.child(currUserId).setValue(true)
                        blog.likedBy?.add(currUserId)
                        updateLikeButton(holder, true)

                        val newLikeCount = blog.likeCounts2 + 1
                        blog.likeCounts2 = newLikeCount
                        databaseReference.child("blogs").child(postId).child("likeCounts2").setValue(newLikeCount)
                        holder.likeCount.text = newLikeCount.toString()
                        notifyDataSetChanged()
                    }.addOnFailureListener { e ->
                        Log.e("likeClicked", "Failed to like blog: $e")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateLikeButton(holder: BlogViewHolder, liked: Boolean) {
        if (liked) {
            holder.likeButton.setImageResource(R.drawable.red_heart)
        } else {
            holder.likeButton.setImageResource(R.drawable.black_heart)
        }
    }

    override fun getItemCount() = blogList.size

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.heading2)
        val username: TextView = itemView.findViewById(R.id.username2)
        val date: TextView = itemView.findViewById(R.id.date2)
        val description: TextView = itemView.findViewById(R.id.post2)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount2)
        val likeButton: ImageButton = itemView.findViewById(R.id.like2)
    }
}
