package cc.leishui.bilitx.network.bili

import cc.leishui.bilitx.bean.biliBean.*
import cc.leishui.bilitx.utils.Utils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object BiliRepo {
    private fun <T> Call<T>.enqueue(
        success: (response: T) -> Unit,
        failure: () -> Unit
    ) {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.body() != null)
                    success(response.body()!!)
                else
                    failure()
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                failure()
            }
        })
    }

    private fun fail() {
        Utils.toast("Bili网络请求失败")
    }

    private val ipService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.bilibili.com/").build().create(BiliService::class.java)

    fun getRcmdByPage(
        idx: Int,
        size: Int,
        success: (response: Rcmd) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getRcmdByPage(idx + 1, size).enqueue(success, failure)
    }

    fun getPlayUrl(
        bvid: String, cid: Int, success: (response: PlayUrl) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getPlayUrl(bvid, cid).enqueue(success, failure)
    }

    fun getVideoDetail(
        bvid: String, success: (response: Detail) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getDetail(bvid).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()?.let {
                    //Log.d("detail",it.bytes().toString())
                    val data = JSONObject(String(it.bytes())).getJSONObject("data")
                    val view = data.getJSONObject("View")
                    val detail = Detail()
                    detail.desc = view.getString("desc")
                    detail.copyright = view.getInt("copyright")
                    detail.tname = view.getString("tname")
                    detail.pubDate = view.getLong("pubdate")
                    detail.title = view.getString("title")
                    detail.bvid = view.getString("bvid")
                    val stat = view.getJSONObject("stat")
                    detail.like = stat.getInt("like")
                    detail.coin = stat.getInt("coin")
                    detail.collection = stat.getInt("favorite")
                    detail.share = stat.getInt("share")
                    detail.view = stat.getInt("view")
                    detail.danmaku = stat.getInt("danmaku")
                    val owner = view.getJSONObject("owner")
                    detail.uname = owner.getString("name")
                    detail.uface = owner.getString("face")
                    val card = data.getJSONObject("Card")
                    detail.archiveCount = card.getInt("archive_count")
                    detail.follower = card.getInt("follower")
                    val tags = data.getJSONArray("Tags")
                    val rtags = ArrayList<Tag>()
                    for (i in 0 until tags.length()) {
                        rtags.add(
                            Tag(
                                tags.getJSONObject(i).getString("tag_name"),
                                tags.getJSONObject(i).getInt("type")
                            )
                        )
                    }
                    detail.tags = rtags
                    val related = data.getJSONArray("Related")
                    val rrelated = ArrayList<BVideo>()
                    for (i in 0 until related.length()) {
                        val r = related.getJSONObject(i)
                        rrelated.add(
                            BVideo(
                                r.getString("bvid"),
                                r.getString("title"),
                                r.getString("pic"),
                                r.getJSONObject("owner").getString("name"),
                                r.getJSONObject("owner").getString("face"),
                                r.getJSONObject("stat").getInt("view"),
                                r.getJSONObject("stat").getInt("danmaku")
                            )
                        )
                    }
                    detail.related = rrelated
                    success(detail)
                    return
                }
                failure()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                failure()
            }
        })
    }

    fun getReplyByPage(
        oid: String, idx: Int, size: Int, success: (response: Replies) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getReplyByPage(oid, 1, size, idx + 1, 1, 0)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val replies = Replies()
                    response.body()?.let {
                        val json = String(it.bytes())
                        val jo = JSONObject(json)
                        if (jo.getInt("code") == 0) {
                            val data = jo.getJSONObject("data")
                            val rls = data.getJSONArray("replies")
                            val count = rls.length()
                            val array = ArrayList<Reply>()
                            try {
                                val topReplies = data.getJSONArray("top_replies")
                                if (idx == 0)
                                    for (i in 0 until topReplies.length()) {
                                        val jsonObject = topReplies.getJSONObject(i)
                                        addToReplyList(array, jsonObject)
                                        replies.hasTop = true
                                    }
                            } catch (_: java.lang.Exception) {
                            }
                            replies.count = count
                            replies.acount = data.getJSONObject("page").getInt("acount")
                            replies.mid = data.getJSONObject("upper").getLong("mid")
                            parseReplies(rls, array)
                            replies.replies = array
                            success(replies)
                            return
                        }
                        failure()
                        return
                    }
                    failure()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    failure()
                }
            })
    }

    private fun parseReplies(
        rls: JSONArray,
        array: ArrayList<Reply>
    ) {
        for (i in 0 until rls.length()) {
            val jsonObject = rls.getJSONObject(i)
            try {
                val replies = jsonObject.getJSONArray("replies")
                val rs = ArrayList<Reply>()
                for (j in 0 until replies.length()) {
                    val jsonObject2 = replies.getJSONObject(j)
                    addToReplyList(rs, jsonObject2)
                }
                addToReplyList(array, jsonObject, rs)
            } catch (_: Exception) {
                addToReplyList(array, jsonObject)
            }
        }
    }

    private fun addToReplyList(
        array: ArrayList<Reply>,
        jsonObject: JSONObject,
        rs: ArrayList<Reply> = ArrayList()
    ) {
        val member = jsonObject.getJSONObject("member")
        val content = jsonObject.getJSONObject("content")
        val msg = content.getString("message")
        val list = Regex("\\[[^]]+]").findAll(msg).toList()
        array.add(
            Reply(
                member.getString("uname"),
                msg,
                member.getString("avatar"),
                jsonObject.getJSONObject("reply_control")
                    .getString("time_desc"),
                jsonObject.getInt("like"),
                rs,
                member.getJSONObject("level_info").getInt("current_level"),
                member.getLong("mid"),
                member.getInt("is_senior_member"),
                rcount = jsonObject.getInt("rcount"),
                mr = list
            ).apply {
                try {
                    this.entryTxt = jsonObject.getJSONObject("reply_control")
                        .getString("sub_reply_entry_text")
                    this.titleTxt = jsonObject.getJSONObject("reply_control")
                        .getString("sub_reply_title_text")
                } catch (_: Exception) {
                }
                try {
                    val hm = HashMap<String, String>()
                    val sm = HashMap<String, Int>()
                    list.forEach {
                        hm[it.value] =
                            content.getJSONObject("emote").getJSONObject(it.value).getString("url")
                        sm[it.value] =
                            content.getJSONObject("emote").getJSONObject(it.value).getJSONObject("meta").getInt("size")
                    }
                    this.urlMap = hm
                    this.sizeMap = sm
                } catch (_: Exception) {
                }
            }
        )
    }


    fun getDesc(
        bvid: String, success: (response: Desc) -> Unit,
        failure: () -> Unit = { fail() }
    ) {
        ipService.getDesc(bvid).enqueue(success, failure)
    }
}