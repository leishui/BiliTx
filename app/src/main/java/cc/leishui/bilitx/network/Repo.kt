package cc.leishui.bilitx.network

import android.content.Context
import android.widget.Toast
import cc.leishui.bilitx.bean.bean.*
import cc.leishui.bilitx.bean.page.SpringPage
import cc.leishui.bilitx.msg.LessonMsg
import cc.leishui.bilitx.msg.Msg
import cc.leishui.bilitx.msg.PageMsg
import cc.leishui.bilitx.msg.SimpleMsg
import cc.leishui.bilitx.utils.SPUtils
import cc.leishui.bilitx.utils.Utils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object Repo {
    private fun <T> Call<T>.enqueue(
        success: (response: Response<T>) -> Unit,
        failure: () -> Unit
    ) {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                success(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                failure()
            }
        })
    }

    private fun filesToMultipartBodyParts(files: List<File>): List<MultipartBody.Part> {
        val parts: MutableList<MultipartBody.Part> =
            ArrayList(files.size)
        for (file in files) {
            val requestBody: RequestBody =
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val part =
                MultipartBody.Part.createFormData("files", file.name, requestBody)
            parts.add(part)
        }
        return parts
    }

    private fun fail() {
        Utils.toast("网络请求失败")
    }

    private var ipService: IpService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SPUtils.getServerUrl()).build().create(IpService::class.java)

    fun changeServer(url: String, context: Context?) {
        try {
            ipService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url).build().create(IpService::class.java)
            SPUtils.saveServerUrl(url)
        } catch (e: Exception) {
            Toast.makeText(context, "should started with http or https", Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserInfo(id: Long, callback: Callback<Msg<User>>) {
        ipService.getUserInfo(id).enqueue(callback)
    }

    fun getUserInfo(
        id: Long,
        success: (response: Response<Msg<User>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getUserInfo(id).enqueue(object :
            Callback<Msg<User>> {
            override fun onResponse(call: Call<Msg<User>>, response: Response<Msg<User>>) {
                success(response)
            }

            override fun onFailure(call: Call<Msg<User>>, t: Throwable) {
                failure()
            }
        })
    }

    fun getCode(
        phone: Long, success: (response: Response<SimpleMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getCode(phone).enqueue(success, failure)
    }

    fun loginPhone(
        phone: Long, password: String, success: (response: Response<Msg<User>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.logInByPhone(phone, password).enqueue(success, failure)
    }

    fun signIn(
        phone: Long,
        password: String,
        code: Int,
        identity: Int,
        date: Long,
        success: (response: Response<Msg<User>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.signIn(phone, password, code, identity, date).enqueue(success, failure)
    }

    fun getLesson(
        success: (response: Response<LessonMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getLesson(0, 14).enqueue(success, failure)
    }

    fun getLessonByPage(
        page: Int, size: Int, success: (response: Response<LessonMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getLesson(page, size).enqueue(success, failure)
    }

    //浏览
    fun view(res_id: Long, type: Int) {
        ipService.view(res_id, type).enqueue({}, {})
    }

    fun getLessonSetByPage(
        page: Int, size: Int, success: (response: Response<PageMsg<LessonSet>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getLessonSet(page, size).enqueue(success, failure)
    }

    fun getCommentsAndReplies(
        commentId: Long,
        type: Int,
        page: Int,
        size: Int,
        success: (response: Response<PageMsg<Comment>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getCommentsAndReplies(commentId, type, page, size).enqueue(success, failure)
    }

    fun getReplies(
        father_id: Long,
        page: Int,
        size: Int,
        success: (response: Response<PageMsg<Reply>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getReplies(father_id, page, size).enqueue(success, failure)
    }

    fun saveComment(
        comment_id: Long,
        content: String,
        user_id: Long,
        type: Int,
        success: (response: Response<SimpleMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.saveComment(comment_id, content, user_id, type).enqueue(success, failure)
    }

    fun saveReply(
        reply_father: Long,
        replier_id: Long,
        reply_content: String,
        type: Int,
        comment_id: Long,
        reply_second_father: Long,
        second_replier_id: Long,
        success: (response: Response<SimpleMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.saveReply(
            reply_father,
            replier_id,
            reply_content,
            type,
            comment_id,
            reply_second_father,
            second_replier_id
        ).enqueue(success, failure)
    }

    fun uploadFiles(
        files: ArrayList<File>,
        upId: Long,
        success: (response: Response<Msg<ArrayList<String>>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.uploadFiles(filesToMultipartBodyParts(files), upId).enqueue(success, failure)
    }

    fun uploadPost(
        name: String,
        content: String,
        resources: String?,
        upId: Long,
        sourceType: Boolean,
        type: Long,
        success: (response: Response<SimpleMsg>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.uploadPost(name, content, resources, upId, sourceType, type)
            .enqueue(success, failure)
    }

    fun getPostsByPage(
        page: Int, size: Int, success: (response: Response<Msg<SpringPage<Post>>>) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getPostsByPage(page, size).enqueue(success, failure)
    }
}