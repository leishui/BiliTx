package cc.leishui.bilitx.constant

import cc.leishui.bilitx.utils.Utils.dip2px

object DefaultValues {
    private const val DEFAULT_RESOURCE_SERVER = "https://yztx.entergx.cn/resource"

    //默认头像地址
    const val DEFAULT_AVATAR = "$DEFAULT_RESOURCE_SERVER?id=0&name=avatar.jpg"
    const val DEFAULT_COVER = "$DEFAULT_RESOURCE_SERVER?id=0&name=cover.jpg"
    val repliesPadding = dip2px(5f)
}