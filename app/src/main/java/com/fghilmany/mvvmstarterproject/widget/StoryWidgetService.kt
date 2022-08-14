package com.fghilmany.mvvmstarterproject.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StoryWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return StoryRemoteViewsFactory(this.applicationContext)
    }
}