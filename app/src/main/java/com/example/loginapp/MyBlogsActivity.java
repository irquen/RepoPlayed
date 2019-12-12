package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loginapp.Adapters.MyApplication;
import com.example.loginapp.Blog;
import com.example.loginapp.EditGame;
import com.example.loginapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

public class MyBlogsActivity extends AppCompatActivity {
    private RecyclerView mResultListOwner;
    private DatabaseReference mPostReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editv1);

        mPostReference = FirebaseDatabase.getInstance().getReference("Blog");
        mResultListOwner = (RecyclerView) findViewById(R.id.result_post);

        mResultListOwner.setHasFixedSize(true);
        mResultListOwner.setLayoutManager(new LinearLayoutManager(this));
        firebaseSearch();


    }

    private void firebaseSearch() {

        //Query firebaseSearchQueryBlog = mPostReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        Query admins = mPostReference.orderByChild("creator").equalTo(user.getUid());

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.profile_owner_blogs,
                BlogViewHolder.class,
                admins


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {

                //String listPostKey = getRef(i).getKey();
                blogViewHolder.setId(getRef(i).getKey());
                blogViewHolder.setCategoryBlog(blog.getCateg());
                blogViewHolder.setBlog(blog);
                blogViewHolder.setBlogTitle(blog.getTitle());
                blogViewHolder.setImgBlog(getApplicationContext(), blog.getimageUrl());

            }
        };

        mResultListOwner.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        View mPostView;
        public Blog blogAux;
        TextView blog_category;
        TextView blog_title;
        ImageView blog_image;
        String id;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostView = itemView;

        }

        public void setCategoryBlog(String category) {
            blog_category = (TextView) mPostView.findViewById(R.id.category_post);
            blog_category.setText(category);
            blog_category.setOnClickListener(this);
        }

        public void setBlogTitle(String category) {
            blog_title = (TextView) mPostView.findViewById(R.id.title_post);
            blog_title.setText(category);
            blog_title.setOnClickListener(this);
        }

        public void setImgBlog(Context ctx, String image) {
            blog_image = (ImageView) mPostView.findViewById(R.id.icon_result);
            Glide.with(MyApplication.getAppContext()).load(image).into(blog_image);
            blog_image.setOnClickListener(this);
        }


        public void setBlog(Blog blog) {
            blogAux = blog;
        }

        public void setId(String id){
            this.id = id;
        }

        public String getBId(){
            return this.id;
        }

        @Override

        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = getBId();
            Intent BlogPage = new Intent(MyApplication.getAppContext(), EditGame.class);
            BlogPage.putExtra("title",blogAux.getTitle());
            //BlogPage.putExtra("console", blogAux.getConso());
            BlogPage.putExtra("descrip", blogAux.getDesc());
            //BlogPage.putExtra("category", blogAux.getCateg());
            BlogPage.putExtra("imageurl", blogAux.getimageUrl());
            BlogPage.putExtra("vbid", getBId());

            //ERROR NOVIEMBRE 21    Calling startActivity() from outside of an Activity
            BlogPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //End

            MyApplication.getAppContext().startActivity(BlogPage);
        }

        }
}



