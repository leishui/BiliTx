package cc.leishui.bilitx.msg

import cc.leishui.bilitx.bean.bean.Lesson
import cc.leishui.bilitx.bean.page.SpringPage

data class LessonMsg(
    val status:Int,
    val content: SpringPage<Lesson>
)