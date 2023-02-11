package cc.leishui.bilitx.utils

import android.app.Application
import android.content.Context

class ContextTool: Application(){
    companion object {
        lateinit var  context:Application
        fun getContext(): Context {
            return context
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }



}