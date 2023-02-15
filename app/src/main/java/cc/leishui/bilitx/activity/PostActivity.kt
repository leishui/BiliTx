package cc.leishui.bilitx.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.bean.bean.Post
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.SPUtils
import cc.leishui.bilitx.utils.Utils
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class PostActivity : AppCompatActivity() {

    private var page = 0
    lateinit var post: Post
    lateinit var recyclerView: RecyclerView
    lateinit var refreshLayout: SmartRefreshLayout
    //private lateinit var commentAdapter: SimpleDelegateAdapter<Comment>
    var mDelegateAdapter: DelegateAdapter? = null
    val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()
    private var popupWindow: PopupWindow? = null
    private var popComment: PopupWindow? = null
    private lateinit var review: View
    //private lateinit var bottomAdapter: SingleDelegateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_v_post)
        review = LayoutInflater.from(this@PostActivity).inflate(
            R.layout.fragment_v_post, null
        )
        initPost()
        initViews()
        initListeners()
        initPopComment()
        initPopOpen()
    }

    private fun initPost() {
        post = intent.getSerializableExtra("post") as Post
    }

    fun initViews() {
        recyclerView = findViewById(R.id.rv_v)
        refreshLayout = findViewById(R.id.rf_v)
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(this)
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)

//        commentAdapter = object : SimpleDelegateAdapter<Comment>(
//            R.layout.item_comment,
//            LinearLayoutHelper()
//        ) {
//            @SuppressLint("SetTextI18n")
//            override fun bindData(
//                holder: RecyclerViewHolder,
//                position: Int,
//                model: Comment?
//            ) {
//                if (model != null) {
//                    val llReply = holder.findViewById<LinearLayout>(R.id.ll_comment_reply)
//                    val replyCount = model.reply_count
//                    if (replyCount > 0) {
//                        Log.d("bindReplyData", model.commentId.toString())
//                        llReply.removeAllViews()
//                        model.replies.content.forEach { reply ->
//                            val ss: SpannableString
//                            val replierName = reply.replier_name
//                            if (reply.replySecondFather == 0L) {
//                                ss = SpannableString(replierName + ":" + reply.reply_content)
//                            } else {
//                                val secondReplierName = reply.second_replier_name
//                                ss =
//                                    SpannableString(replierName + " 回复 " + secondReplierName + ":" + reply.reply_content)
//                                val length = ("$replierName 回复 ").length
//                                ss.setSpan(
//                                    ForegroundColorSpan(Color.RED),
//                                    length,
//                                    secondReplierName.length + length,
//                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                                )
//
//                            }
//                            ss.setSpan(
//                                ForegroundColorSpan(Color.RED),
//                                0,
//                                replierName.length,
//                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                            )
//                            llReply.addView(TextView(this@PostActivity).apply {
//                                text = ss
//                                setOnClickListener {
//                                    CommentUtil.dialog(
//                                        this@PostActivity,
//                                        model.id
//                                    )
//                                }
//                                setPadding(DefaultValues.repliesPadding)
//                            })
//                        }
//                        if (replyCount > 3) {
//                            llReply.addView(TextView(this@PostActivity).apply {
//                                text = "共${replyCount}条评论>>"
//                                setTextColor(Color.RED)
//                                setOnClickListener {
//                                    CommentUtil.dialog(
//                                        this@PostActivity,
//                                        model.id
//                                    )
//                                }
//                                setPadding(DefaultValues.repliesPadding)
//                            })
//                        }
//
//                    } else {
//                        llReply.removeAllViews()
//                    }
//                    holder.text(R.id.tv_up_name_comment, model.commentator_name)
//                    holder.text(R.id.tv_comment_content, model.comment_content)
//                    holder.text(R.id.tv_up_time_intro, Utils.transToString(model.comment_time))
//                    holder.image(R.id.ri_avatar_comment, model.commentator_url)
//                    holder.click(R.id.tv_comment_content) {
//                        Utils.dialogEdit(
//                            "回复给 ${model.commentator_name}",
//                            "[回复内容]:",
//                            this@PostActivity,
//                            object : Utils.AlertEditListener {
//                                override fun onNegative() {
//
//                                }
//
//                                override fun onPositive(content: String) {
//                                    sendReply(model.id, content, model.commentId, 0, 0)
//                                }
//                            })
//                    }
//                }
//            }
//        }
//        bottomAdapter = object : SingleDelegateAdapter(R.layout.adapter_vlayout_bottom_item) {
//            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
//
//            }
//
//        }
//        val topAdapter = object : SingleDelegateAdapter(R.layout.activity_post) {
//            override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
//                holder.text(R.id.tv_post_title, post.post_name)
//                holder.text(R.id.tv_post_content, post.post_content)
//                val linearLayout = holder.findViewById<LinearLayout>(R.id.ll_post_photo)
//                linearLayout.removeAllViews()
//                if (post.postResourcesList != null)
//                    for (s in post.postResourcesList) {
//                        val iv = ImageView(this@PostActivity)
//                        val layoutParams =
//                            LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT
//                            )
//                        iv.layoutParams = layoutParams
//                        iv.setPadding(5, 5, 5, 5)
//                        iv.scaleType = ImageView.ScaleType.CENTER_CROP
//                        Glide.with(this@PostActivity).asBitmap().load(s)
//                            .placeholder(R.drawable.ic_gank)
//                            .error(R.drawable.ic_error).into(object :
//                                SimpleTarget<Bitmap>() {
//                                override fun onResourceReady(
//                                    resource: Bitmap,
//                                    transition: Transition<in Bitmap>?
//                                ) {
//                                    val width: Int = resource.width
//                                    val height: Int = resource.height
//                                    layoutParams.height =
//                                        Utils.getScreenWidth() * height / width
//                                    iv.layoutParams = layoutParams
//                                    iv.setImageBitmap(resource)
//                                }
//                            })
//                        //holder.image(i,s)
//                        linearLayout.addView(iv)
//                    }
//            }
//
//        }
//        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
//        mAdapters.add(topAdapter)
//        //mAdapters.add(ottomAdapter)
//        mAdapters.add(commentAdapter)
//        mAdapters.add(bottomAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
    }

    private fun sendReply(
        reply_father: Long,
        content: String,
        comment_id: Long,
        reply_second_father: Long,
        second_replier_id: Long
    ) {
        Repo.saveReply(
            reply_father,
            SPUtils.getUser().userId,
            content,
            ResourceType.POST,
            comment_id,
            reply_second_father,
            second_replier_id, {
                if (it.body()?.status == StatusType.SUCCESSFUL) {
                    Utils.toast("发送成功")
                    //initData(0)
                    return@saveReply
                }
                Utils.toast("发送失败：" + it.body()?.msg)
            }, { Utils.toast("发送失败") })
    }

    private fun initListeners() {
        findViewById<ImageButton>(R.id.iv_back_post).setOnClickListener { finish() }
        //下拉刷新
        refreshLayout.setOnRefreshListener {
            //initData(0)
        }
        //上拉加载
        refreshLayout.setOnLoadMoreListener {
            //initData(page)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
        //initData(0)
    }

//    private fun initData(page: Int) {
//        if (page == 0) this.page = 0
//        this.page += 1
//        Repo.getCommentsAndReplies(post.postId,
//            ResourceType.POST, page, 20, {
//                if (it.body()!!.status == StatusType.SUCCESSFUL) {
//                    if (page == 0) {
//                        commentAdapter.refresh(it.body()!!.content.content)
//                        mDelegateAdapter!!.setAdapters(mAdapters)
//                    } else {
//                        if (it.body()!!.content.empty) {
//                            this@PostActivity.page -= 1
//                            refreshLayout.finishLoadMoreWithNoMoreData()
//                            Utils.toast("已全部加载")
//                        } else commentAdapter.loadMore(it.body()!!.content.content)
//                    }
//                } else {
//                    Utils.toast("获取评论失败")
//                }
//                if (page == 0)
//                    refreshLayout.finishRefresh()
//                else
//                    refreshLayout.finishLoadMore()
//
//            }, {
//                Utils.toast("获取评论失败:请求异常")
//                if (page == 0) {
//                    mDelegateAdapter!!.setAdapters(mAdapters)
//                    refreshLayout.finishRefresh()
//                } else {
//                    refreshLayout.finishLoadMore()
//                }
//            })
//    }

    private fun initPopComment() {
        popComment = PopupWindow(this)
        popComment?.apply {
            contentView =
                LayoutInflater.from(this@PostActivity).inflate(R.layout.popup_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            //animationStyle = R.style.PopupSchool
            isFocusable = true
            val et = contentView.findViewById<EditText>(R.id.et_comment_content)
            contentView.findViewById<Button>(R.id.bt_comment_send)
                .setOnClickListener {
                    popComment?.dismiss()
                    sendComment(
                        post.postId,
                        et.text.toString(),
                        SPUtils.getUser().userId
                    )
                    et.text.clear()
                }
        }
    }

    private fun sendComment(comment_id: Long, content: String, user_id: Long) {
        Repo.saveComment(
            comment_id,
            content,
            user_id,
            ResourceType.POST, {
                if (it.body()?.status == StatusType.SUCCESSFUL) {
                    Utils.toast("发送成功")
                    //initData(0)
                    return@saveComment
                }
                Utils.toast("发送失败：" + it.body()?.msg)
            }, { Utils.toast("发送失败") })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initPopOpen() {
        popupWindow = PopupWindow(this)
        popupWindow?.apply {
            contentView =
                LayoutInflater.from(this@PostActivity)
                    .inflate(R.layout.popup_open_comment, null)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(getDrawable(R.color.white))
            animationStyle = R.style.PopupSchool
            contentView.findViewById<TextView>(R.id.tv_open_comment)
                .setOnClickListener {
                    //popupWindow?.dismiss()
                    popComment?.showAtLocation(
                        review,
                        Gravity.BOTTOM,
                        0,
                        0
                    )
                    val edit =
                        popComment?.contentView?.findViewById<EditText>(R.id.et_comment_content)
                    edit?.requestFocus()
                    val imm =
                        edit?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
                }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            popupWindow?.showAtLocation(
                review,
                Gravity.BOTTOM,
                0,
                0
            )
        } else if (ev?.action == MotionEvent.ACTION_DOWN) popupWindow?.dismiss()
        return super.dispatchTouchEvent(ev)
    }
}