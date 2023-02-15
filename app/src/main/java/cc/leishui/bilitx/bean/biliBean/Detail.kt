package cc.leishui.bilitx.bean.biliBean

import cc.leishui.bilitx.bean.biliBean.Rcmd.DataEntity.ItemEntity
import java.io.Serializable

data class Detail(
    var bvid: String = "",
    var view: Int = 0,
    var danmaku: Int = 0,
    var copyright: Int = 1,
    var uname: String = "",
    var uface: String = "",
    var archiveCount: Int = 0,
    var follower: Int = 0,
    var tname: String = "",
    var title: String = "",
    var desc: String = "",
    var pubDate: Long = 0,
    var collection: Int = 0,
    var coin: Int = 0,
    var share: Int = 0,
    var like: Int = 0,
    var tags: ArrayList<Tag> = ArrayList(),
    var related: ArrayList<BVideo> = ArrayList()
)

data class Tag(
    var name: String = "",
    var type: Int = 0
)

data class BVideo(
    var bvid: String = "",
    var title: String = "",
    var pic: String = "",
    var uname: String = "",
    var uface: String = "",
    var view: Int = 0,
    var danmaku: Int = 0,
    var cid: Int = 0
) : Serializable {
    constructor(entity: ItemEntity) : this() {
        bvid = entity.bvid
        title = entity.bvid
        pic = entity.pic
        uname = entity.owner.name
        uface = entity.owner.face
        view = entity.stat.view
        danmaku = entity.stat.danmaku
        cid = entity.cid
    }
}
