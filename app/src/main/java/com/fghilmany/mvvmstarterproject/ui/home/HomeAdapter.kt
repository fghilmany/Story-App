package com.fghilmany.mvvmstarterproject.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fghilmany.mvvmstarterproject.core.data.remote.response.ListStoryItem
import com.fghilmany.mvvmstarterproject.databinding.ItemHomeBinding
import com.fghilmany.mvvmstarterproject.ui.detail.DetailActivity

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val listStory = arrayListOf<ListStoryItem>()
    fun setList(list: List<ListStoryItem>?) {
        if (list != null) {
            listStory.clear()
            listStory.addAll(list)
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = listStory[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int = listStory.size

    class ViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ListStoryItem) {
            with(binding) {
                result.apply {
                    tvStory.text = name
                    Glide.with(binding.root)
                        .load(photoUrl)
                        .into(ivStory)

                    itemView.setOnClickListener {
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(ivStory, "image"),
                                Pair(tvStory, "name")
                            )
                        Intent(itemView.context, DetailActivity::class.java)
                            .apply {
                                putExtra(EXTRA_STORY, result)
                                itemView.context.startActivity(
                                    this,
                                    optionsCompat.toBundle()
                                )
                            }
                    }
                }
            }
        }

    }
}