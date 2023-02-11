package cc.leishui.bilitx.msg

import cc.leishui.bilitx.bean.page.SpringPage

data class PageMsg<T>(
    val status:Int,
    val content: SpringPage<T>
)