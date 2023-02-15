package cc.leishui.bilitx.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.MainAdapter
import cc.leishui.bilitx.network.bili.BiliRepo
import cc.leishui.bilitx.utils.Utils.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout


open class TjFragment : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var adapter:MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tj, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
    }

    open fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_v)
        refreshLayout = view.findViewById(R.id.rf_v)
        adapter = MainAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initListeners() {
        refreshLayout.setOnRefreshListener {
            initData(0)
        }
        refreshLayout.setOnLoadMoreListener {
            initData(page)
        }
        refreshLayout.autoRefresh() //第一次进入触发自动刷新，演示效果
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        BiliRepo.getRcmdByPage(page,20,{
            if (page == 0) {
                adapter.setData(it.data.item)
            } else {
                if (it.data.item.isEmpty()) {
                    this@TjFragment.page -= 1
                    "已全部加载".toast()
                    refreshLayout.finishLoadMoreWithNoMoreData()
                } else adapter.loadMore(it.data.item)
            }
            if (page == 0)
                refreshLayout.finishRefresh()
            else
                refreshLayout.finishLoadMore()
        }, {
            "获取评论失败".toast()
            if (page == 0) {
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
    }

}