package cc.leishui.bilitx.bean.biliBean

data class Reply(
    var uname: String = "",
    var content: String = "",
    var avatar: String = "",
    var time: String = "",
    var like: Int = 0,
    var replies: List<Reply> = ArrayList(),
    var level: Int = 0,
    var mid: Long = 0,
    var isSeniorMember: Int = 0,
    var entryTxt:String="",
    var titleTxt:String="",
    var rcount:Int=0,
    var mr:List<MatchResult> = ArrayList(),
    var urlMap:HashMap<String,String> = HashMap(),
    var sizeMap:HashMap<String,Int> = HashMap()
)

data class Replies(
    var count: Int = 0,
    var acount: Int = 0,
    var hasTop: Boolean = false,
    var replies: List<Reply> = ArrayList(),
    var mid: Long = 0,
)
