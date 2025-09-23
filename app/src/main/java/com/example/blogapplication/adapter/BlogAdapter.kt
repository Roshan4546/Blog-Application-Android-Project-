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
import com.google.firebase.database.*

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
        val currUser = FirebaseAuth.getInstance().currentUser

        holder.title.text = blog.heading2 ?: ""
        holder.username.text = blog.username2 ?: ""
        holder.date.text = blog.date2 ?: ""
        holder.description.text = blog.post2 ?: ""

        if (postId != null) {
            val postRef = databaseReference.child("blogs").child(postId)
            val likesRef = postRef.child("likes")
            val likeCountRef = postRef.child("likeCounts2")

            // Show like count
            likeCountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.getValue(Int::class.java) ?: 0
                    holder.likeCount.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            // Show liked/unliked icon for current user
            if (currUser != null) {
                likesRef.child(currUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        updateLikeButton(holder, snapshot.exists())
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            } else {
                updateLikeButton(holder, false)
            }
        } else {
            holder.likeCount.text = "0"
            updateLikeButton(holder, false)
        }

        // Like button click listener
        holder.likeButton.setOnClickListener {
            if (currUser != null && postId != null) {
                handleLikeButton(postId, holder, currUser.uid)
            } else {
                Toast.makeText(holder.itemView.context, "You have to login first", Toast.LENGTH_SHORT).show()
            }
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ReadMoreActivity::class.java)
            intent.putExtra("blogItem", blog)  // Pass BlogItemModel as Parcelable
            context.startActivity(intent)
        }

    }

    private fun handleLikeButton(postId: String, holder: BlogViewHolder, currUserId: String) {
        val postRef = databaseReference.child("blogs").child(postId)
        val likesRef = postRef.child("likes")
        val likeCountRef = postRef.child("likeCounts2")

        likesRef.child(currUserId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User already liked - remove like
                    likesRef.child(currUserId).removeValue().addOnSuccessListener {
                        likeCountRef.runTransaction(object : Transaction.Handler {
                            override fun doTransaction(currentData: MutableData): Transaction.Result {
                                val currentCount = currentData.getValue(Int::class.java) ?: 0
                                if (currentCount > 0) {
                                    currentData.value = currentCount - 1
                                }
                                return Transaction.success(currentData)
                            }
                            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                                updateLikeButton(holder, false)
                            }
                        })
                    }.addOnFailureListener { e ->
                        Log.e("BlogAdapter", "Failed to unlike post: $e")
                    }
                } else {
                    // User is liking the post - add like
                    likesRef.child(currUserId).setValue(true).addOnSuccessListener {
                        likeCountRef.runTransaction(object : Transaction.Handler {
                            override fun doTransaction(currentData: MutableData): Transaction.Result {
                                val currentCount = currentData.getValue(Int::class.java) ?: 0
                                currentData.value = currentCount + 1
                                return Transaction.success(currentData)
                            }
                            override fun onComplete(error: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                                updateLikeButton(holder, true)
                            }
                        })
                    }.addOnFailureListener { e ->
                        Log.e("BlogAdapter", "Failed to like post: $e")
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
