package cc.leishui.bilitx.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import cc.leishui.bilitx.bean.bean.User
import com.google.gson.Gson

@SuppressLint("StaticFieldLeak")
object SPUtils {
    private val context = ContextTool.getContext()
    private val userSp: SharedPreferences =
        context.getSharedPreferences("User", Context.MODE_PRIVATE)
    private val statusSp: SharedPreferences =
        context.getSharedPreferences("Status", Context.MODE_PRIVATE)
    private val userEditor = userSp.edit()
    private val statusEditor = statusSp.edit()

    @SuppressLint("CommitPrefEdits")
    fun saveIsLogin(isLogin: Boolean) {
        userEditor.putBoolean("isLogin", isLogin)
        userEditor.apply()
        saveIsPicker(true)
    }

    fun getIsLogin(): Boolean {
        return userSp.getBoolean("isLogin", false)
    }
    fun saveServerUrl(url:String){
        userEditor.putString("server_url", url)
        userEditor.apply()
    }
    fun getServerUrl():String{
        return userSp.getString("server_url","").toString()
    }
    private fun saveIsPicker(isLogin: Boolean) {
        statusEditor.putBoolean("isPicker", isLogin)
        statusEditor.apply()
    }

    fun getIsPicker(): Boolean {
        return statusSp.getBoolean("isPicker", false)
    }

    fun saveUser(user: User) {
        userEditor.putString("user", Gson().toJson(user))
        userEditor.apply()
    }

    fun getUser(): User {
        return Gson().fromJson(userSp.getString("user", ""), User::class.java)
    }
}