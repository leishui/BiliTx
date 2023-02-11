package cc.leishui.bilitx.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.delegate.SimpleDelegateAdapter
import cc.leishui.bilitx.bean.bean.Reply
import cc.leishui.bilitx.constant.ResourceType
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog

object CommentUtil {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mNewsAdapter: SimpleDelegateAdapter<Reply>
    private var mDelegateAdapter: DelegateAdapter? = null
    private val mAdapters: MutableList<DelegateAdapter.Adapter<*>> =
        ArrayList()

    @SuppressLint("StaticFieldLeak")
    private var dialog: MaterialDialog? = null

    fun dialog(context: Context, father_id: Long) {
        if (dialog == null) {
            dialog = MaterialDialog.Builder(context).customView(R.layout.layout_comment, false)
                .title("自定义对话框")
                .positiveText("R.string.lab_submit")
                .negativeText("R.string.lab_cancel").build()
            initViews(context, father_id, dialog!!)
        }
        initListeners(father_id)
        dialog?.show()
    }

    fun initViews(context: Context, father_id: Long, build: MaterialDialog) {
        recyclerView = build.findViewById(R.id.rv_comment)
        refreshLayout = build.findViewById(R.id.srl_comment)
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(context)
        recyclerView.layoutManager = virtualLayoutManager

        // 2.设置RecycledViews复用池大小（可选）
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 20)
        recyclerView.setRecycledViewPool(viewPool)


        //资讯的标题
        val stickyLayoutHelper = StickyLayoutHelper()
        stickyLayoutHelper.setStickyStart(true)

        //资讯，线性布局
        mNewsAdapter = object : SimpleDelegateAdapter<Reply>(
            R.layout.item_comment,
            LinearLayoutHelper()
        ) {
            @SuppressLint("SetTextI18n")
            override fun bindData(
                holder: RecyclerViewHolder,
                position: Int,
                model: Reply?
            ) {
                if (model != null) {
                    if (model.replySecondFather > 0) {
                        holder.text(
                            R.id.tv_comment_content,
                            SpannableString("回复 " + model.second_replier_name + ":" + model.reply_content).apply {
                                setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    "回复 ".length,
                                    model.second_replier_name.length + "回复 ".length,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            })
                    } else
                        holder.text(R.id.tv_comment_content, model.reply_content)
                    holder.text(R.id.tv_up_name_comment, model.replier_name)
                    holder.text(R.id.tv_up_time_intro, Utils.transToString(model.reply_time))
                    holder.image(R.id.ri_avatar_comment, model.replier_url)
                    holder.click(R.id.tv_comment_content) {
                        Utils.dialogEdit(
                            "回复给 ${model.replier_name}",
                            "[回复内容]:",
                            context,
                            object : Utils.AlertEditListener {
                                override fun onNegative() {

                                }

                                override fun onPositive(content: String) {
                                    Repo.saveReply(model.replyFather,
                                        SPUtils.getUser().userId,
                                        content,
                                        ResourceType.POST,
                                        model.comment_id,
                                        model.id,
                                        model.replier_id,
                                        {
                                            if (it.body()?.status == StatusType.SUCCESSFUL) {
                                                Utils.toast("发送成功")
                                                initRepliesData(0, father_id)
                                                return@saveReply
                                            }
                                            Utils.toast("发送失败：" + it.body()?.msg)
                                        },
                                        { Utils.toast("发送失败") })
                                }
                            })
                    }
                }
            }
        }
        mDelegateAdapter = DelegateAdapter(virtualLayoutManager)
        mAdapters.add(mNewsAdapter)
        //mAdapters.add(bottomAdapter)

        // 3.设置DelegateAdapter
        recyclerView.adapter = mDelegateAdapter
        //initListeners(father_id)
    }

    fun initListeners(father_id: Long) {
        //下拉刷新
        refreshLayout.setOnRefreshListener {
            initRepliesData(0, father_id)
        }
        //上拉加载
        refreshLayout.setOnLoadMoreListener {
            initRepliesData(page, father_id)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
        //initData(0)
    }

    private fun initRepliesData(page: Int, father_id: Long) {
        if (page == 0) CommentUtil.page = 0
        CommentUtil.page += 1
        Repo.getReplies(father_id, page, 10, {
            if (it.body()!!.status == StatusType.SUCCESSFUL) {
                if (page == 0) {
                    mNewsAdapter.refresh(it.body()!!.content.content)
                    mDelegateAdapter!!.setAdapters(mAdapters)
                } else {
                    if (it.body()!!.content.empty) {
                        CommentUtil.page -= 1
                        Utils.toast("已全部加载")
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else mNewsAdapter.loadMore(it.body()!!.content.content)
                }
            } else {
                Utils.toast("获取评论失败")
            }
            if (page == 0)
                refreshLayout.finishRefresh()
            else
                refreshLayout.finishLoadMore()
        }, {
            Utils.toast("获取评论失败:请求异常")
            if (page == 0) {
                mDelegateAdapter!!.setAdapters(mAdapters)
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
    }
}