package cc.leishui.bilitx.fragment.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.activity.NormalVideoActivity
import cc.leishui.bilitx.adapter.video.CommentAdapter
import cc.leishui.bilitx.network.bili.BiliRepo
import cc.leishui.bilitx.utils.Utils.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CommentFragment(private val oid: String) : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var adapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_v)
        refreshLayout = view.findViewById(R.id.rf_v)
        adapter = CommentAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


//    private fun dialog(father_id: Long) {
//        if (dialog == null) {
//            dialog =
//                MaterialDialog.Builder(requireContext()).customView(R.layout.layout_comment, false)
//                    .title("自定义对话框")
//                    .positiveText("R.string.lab_submit")
//                    .negativeText("R.string.lab_cancel").build()
//            CommentUtil.initViews(requireContext(), father_id, dialog!!)
//        }
//        CommentUtil.initListeners(father_id)
//        dialog?.show()
//    }

    private fun initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener {
            initData(0)
        }
        //上拉加载
        refreshLayout.setOnLoadMoreListener {
            initData(page)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        BiliRepo.getReplyByPage(oid,
            page, 20, {
                if (page == 0) {
                    (activity as NormalVideoActivity).setAccount(it.acount)
                    adapter.setHasTop(it.hasTop)
                    adapter.setMid(it.mid)
                    adapter.setData(it.replies)
                } else {
                    if (it.replies.isEmpty()) {
                        this@CommentFragment.page -= 1
                        "已全部加载".toast()
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else adapter.loadMore(it.replies)
                }
                if (page == 0)
                    refreshLayout.finishRefresh()
                else
                    refreshLayout.finishLoadMore()

            }, {
                "获取评论失败:请求异常".toast()
                if (page == 0) {
                    //mDelegateAdapter!!.setAdapters(mAdapters)
                    refreshLayout.finishRefresh()
                } else {
                    refreshLayout.finishLoadMore()
                }
            })
    }

}