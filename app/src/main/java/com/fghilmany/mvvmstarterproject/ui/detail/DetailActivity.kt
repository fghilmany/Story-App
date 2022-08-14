package com.fghilmany.mvvmstarterproject.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fghilmany.mvvmstarterproject.core.data.remote.response.ListStoryItem
import com.fghilmany.mvvmstarterproject.databinding.ActivityDetailBinding
import com.fghilmany.mvvmstarterproject.ui.home.HomeAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val data = extras?.getParcelable<ListStoryItem>(HomeAdapter.EXTRA_STORY)
        with(binding) {
            data?.apply {
                tvName.text = name
                tvDesc.text = description
                Glide.with(this@DetailActivity)
                    .load(photoUrl)
                    .into(ivStory)
            }
        }
    }
}