package cc.leishui.bilitx.network.bili

import cc.leishui.bilitx.bean.biliBean.Desc
import cc.leishui.bilitx.bean.biliBean.PlayUrl
import cc.leishui.bilitx.bean.biliBean.Rcmd
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface BiliService {
    //获取推荐
    @GET("x/web-interface/index/top/feed/rcmd")
    fun getRcmd(): Call<Rcmd>

    //分页获取推荐
    @GET("x/web-interface/index/top/feed/rcmd")
    fun getRcmdByPage(@Query("fresh_idx") idx: Int, @Query("ps") size: Int): Call<Rcmd>

    //获取视频链接
    @GET("x/player/playurl")
    fun getPlayUrl(@Query("bvid") bvid: String, @Query("cid") cid: Int): Call<PlayUrl>

    //获取评论
    @GET("x/v2/reply")
    fun getReplyByPage(
        @Query("oid") oid: String,
        @Query("type") type: Int,
        @Query("ps") size:Int,
        @Query("pn") idx:Int,
        @Query("sort") sort:Int,
        @Query("nohot") nohot:Int
    ):Call<ResponseBody>

    //获取简介
    @GET("x/web-interface/archive/desc")
    fun getDesc(@Query("bvid") bvid: String): Call<Desc>

    //获取视频超详细信息
    @GET("x/web-interface/view/detail")
    fun getDetail(@Query("bvid") bvid: String): Call<ResponseBody>

}