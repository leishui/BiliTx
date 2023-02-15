package cc.leishui.bilitx.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.TypedValue
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cc.leishui.bilitx.R
import cc.leishui.bilitx.utils.ContextTool.Companion.context
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.xuexiang.xui.adapter.simple.AdapterItem
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.utils.ResUtils.getResources
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("StaticFieldLeak")
object Utils {

    fun Context.getThemeColor(id:Int):Int{
        val typedValue = TypedValue()
        this.theme.resolveAttribute(id, typedValue, true)
        return typedValue.data
    }

    fun tri(function: () -> Unit){
        try {
            function()
        }catch (_:Exception){}
    }
    fun String.toast(){
        Toast.makeText(ContextTool.getContext(), this, Toast.LENGTH_SHORT).show()
    }
    fun ImageView.load(url: String) {
        Glide.with(context).load(url).error(R.drawable.loading).placeholder(R.drawable.loading)
            .into(this)
    }
    fun ImageView.load(url: String,placeHolder:Int) {
        Glide.with(context).load(url).error(placeHolder).placeholder(placeHolder)
            .into(this)
    }

    fun Int.std(): String {
        return if (this < 10000) {
            "$this"
        } else {
            //千位
            var i = this % 10000 / 1000
            //百位
            val ii = this % 1000 / 100
            if (ii >= 5 && i < 9) i += 1
            if (i == 0) "${this / 10000}万"
            else "${this / 10000}.${i}万"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun Long.time(): String {
        return SimpleDateFormat("yyyy-M-d H:mm").format(this * 1000)
    }

    fun Int.dur(): String {
        val h = this / 3600
        val m = this / 60 - h * 60
        val s = this - h * 3600 - m * 60
        var txt = ""
        if (h != 0 && m < 10)
            txt += "$h:0$m:"
        if (h != 0 && m >= 10)
            txt += "$h:$m:"
        if (h == 0)
            txt += "$m:"
        txt += if (s < 10)
            "0$s"
        else
            "$s"
        return txt
    }

    fun textCopyThenPost(textCopied: String) {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clip board text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            toast("已复制到剪切板")
    }

    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    fun getReplyTextWidth(): Int {
        return getScreenWidth() - dip2px(70f)
    }

    interface AlertEditListener {
        fun onNegative()
        fun onPositive(content: String)
    }

    fun dialogEdit(
        title: String,
        content: String,
        context: Context?,
        alertListener: AlertEditListener
    ) {
        val builder = AlertDialog.Builder(context!!, R.style.MyAlertButton)
        builder.setTitle(title)
        val et = EditText(context)
        if (content.startsWith("["))
            et.hint = content
        else
            et.setText(content)
        et.isSingleLine = true
        builder.setView(et)
        builder.setNegativeButton("取消") { _, _ ->
            alertListener.onNegative()
        }
        builder.setPositiveButton(
            "确定"
        ) { _, _ ->
            alertListener.onPositive(et.text.toString())
        }
        builder.setCancelable(false)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }


    //    val urls = arrayListOf(
//        R.drawable.ic_bv_1,
//        R.drawable.ic_bv_2,
//        R.drawable.ic_bv_3,
//        R.drawable.ic_bv_4
//    )
    private val titles: Array<String> = arrayOf(
        "伪装者:胡歌演绎'痞子特工'",
        "无心法师:生死离别!月牙遭虐杀",
        "花千骨:尊上沦为花千骨",
        "综艺饭:胖轩偷看夏天洗澡掀波澜",
        "碟中谍4:阿汤哥高塔命悬一线,超越不可能"
    )
    private var urls_main = arrayOf(
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt11.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt12.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt13.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt14.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt15.png"
    )
    private var urls_lesson = arrayOf(
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt21.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt22.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt23.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt24.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt25.png"
    )
    private var urls_post = arrayOf(
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt31.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt32.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt33.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt34.png",
        "https://yztx.entergx.cn/resource/get?id=0&name=lbt35.png"
    )

    fun getBannerListMain(): List<BannerItem>? {
        val list = ArrayList<BannerItem>()
        for (i in urls_main.indices) {
            val item = BannerItem()
            item.imgUrl = urls_main[i]
            item.title = titles[i]
            list.add(item)
        }
        return list
    }

    fun getBannerListLesson(): List<BannerItem>? {
        val list = ArrayList<BannerItem>()
        for (i in urls_lesson.indices) {
            val item = BannerItem()
            item.imgUrl = urls_lesson[i]
            item.title = titles[i]
            list.add(item)
        }
        return list
    }

    fun getBannerListPost(): List<BannerItem>? {
        val list = ArrayList<BannerItem>()
        for (i in urls_post.indices) {
            val item = BannerItem()
            item.imgUrl = urls_post[i]
            item.title = titles[i]
            list.add(item)
        }
        return list
    }

    fun bottomSheetDialog(context: Context, father_id: Long) {
        val build =
            MaterialDialog.Builder(context).customView(R.layout.layout_comment, false)
                .title("自定义对话框")
                .positiveText("R.string.lab_submit")
                .negativeText("R.string.lab_cancel").build()
        build.show()
    }

    @SuppressLint("SimpleDateFormat")
    fun transToString(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(time * 1000))
    }

    @SuppressLint("SimpleDateFormat")
    fun getYear(): String {
        return SimpleDateFormat("yyyy").format(System.currentTimeMillis())
    }

    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    fun dip2px(dpValue: Float): Int { // 获取当前手机的像素密度
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt() // 四舍五入取整
    }

    // 获得屏幕的宽度
    fun getScreenWidth(): Int {
        return getResources(context).displayMetrics.widthPixels
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getStatusBarHeight(): Int {
        var statusBarHeight1 = -1
        //获取status_bar_height资源的ID
        //获取status_bar_height资源的ID
        val resourceId: Int =
            getResources(context).getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources(context).getDimensionPixelSize(resourceId)
        }
        return statusBarHeight1
    }

    //弹出短时的Toast
//    fun toast(context: Context, string: CharSequence) {
//        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
//    }

    //弹出短时的Toast
    fun toast(string: CharSequence) {
        Toast.makeText(ContextTool.getContext(), string, Toast.LENGTH_SHORT).show()
    }

    /**
     * 设置背景颜色
     *
     * @param bgAlpha
     */
    fun setBackgroundAlpha(bgAlpha: Float, mContext: Context) {
        val lp = (mContext as Activity).window.attributes
        lp.alpha = bgAlpha
        mContext.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mContext.window.attributes = lp
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    fun setEditTextInhibitInputSpace(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            if (source == " ")
                ""
            else
                source!!
        }
        editText.filters = arrayOf(filter)
    }
    //==========图片选择===========//

    //==========图片选择===========//
    /**
     * 获取图片选择的配置
     *
     * @param fragment
     * @return
     */
    fun getPictureSelector(fragment: Fragment?): PictureSelectionModel {
        return PictureSelector.create(fragment)
            .openGallery(PictureMimeType.ofImage())
            .maxSelectNum(8)
            .minSelectNum(1)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .enableCrop(false)
            .compress(true)
            .previewEggs(true)
    }

    fun getPictureSelector(activity: Activity?): PictureSelectionModel {
        return PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .maxSelectNum(8)
            .minSelectNum(1)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .enableCrop(false)
            .compress(true)
            .previewEggs(true)
    }

    fun getGridItems(context: Context): MutableList<AdapterItem> {
        val list: MutableList<AdapterItem> = ArrayList()
        val titles = ResUtils.getStringArray(R.array.grid_titles_entry)
        val icons =
            ResUtils.getDrawableArray(context, R.array.grid_icons_entry)
        for (i in titles.indices) {
            list.add(AdapterItem(titles[i], icons[i]))
        }
        return list
    }


}