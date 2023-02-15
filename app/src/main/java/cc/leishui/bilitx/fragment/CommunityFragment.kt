package cc.leishui.bilitx.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.CommunityAdapter
import cc.leishui.bilitx.constant.StatusType
import cc.leishui.bilitx.network.Repo
import cc.leishui.bilitx.utils.Utils
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CommunityFragment : Fragment() {
    private var page = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var adapter: CommunityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_v)
        refreshLayout = view.findViewById(R.id.rf_v)
        // 1.设置VirtualLayoutManager
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        recyclerView.layoutManager = virtualLayoutManager
        adapter= CommunityAdapter(requireContext())
        // 3.设置DelegateAdapter
        recyclerView.adapter = adapter
    }

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
        //initData(0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(page: Int) {
        if (page == 0) this.page = 0
        this.page += 1
        Repo.getPostsByPage(page, 10, {
            if (it.body() != null && it.body()!!.status == StatusType.SUCCESSFUL) {
                if (page == 0) {
                    adapter.setData(it.body()!!.content.content)
                    adapter.notifyDataSetChanged()
                    //mDelegateAdapter!!.setAdapters(mAdapters)
                } else {
                    if (it.body()!!.content.empty) {
                        this@CommunityFragment.page -= 1
                        Utils.toast("已全部加载")
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else adapter.loadMore(it.body()!!.content.content)
                }
            } else {
                Utils.toast("获取贴子失败")
            }
            if (page == 0)
                refreshLayout.finishRefresh()
            else
                refreshLayout.finishLoadMore()
        }, {
            Utils.toast("获取贴子失败:请求异常")
            if (page == 0) {
                //mDelegateAdapter!!.setAdapters(mAdapters)
                refreshLayout.finishRefresh()
            } else {
                refreshLayout.finishLoadMore()
            }
        })
    }

}