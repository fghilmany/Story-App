package com.fghilmany.mvvmstarterproject.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.fghilmany.mvvmstarterproject.R
import com.fghilmany.mvvmstarterproject.core.data.remote.response.ListStoryItem
import com.fghilmany.mvvmstarterproject.ui.home.HomeViewModel
import com.fghilmany.mvvmstarterproject.widget.StoryAppWidget.Companion.EXTRA_ITEM
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class StoryRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory, KoinComponent, LifecycleOwner {

    private val viewModel: HomeViewModel by inject()

    private var mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    private val widgetItems = ArrayList<Bitmap?>()
    private val listDataItem = ArrayList<ListStoryItem>()

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }


    override fun onCreate() {
        // TODO: Cari cara buat get data dari lokal di widget 
    }

    override fun onDataSetChanged() {
        widgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_story))

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        Timber.e(widgetItems.size.toString())
        return widgetItems.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, widgetItems[p0])
        val extras = bundleOf(
            EXTRA_ITEM to p0
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            Timber.e(e)
            null
        }
    }
}