package cc.leishui.bilitx.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.vp.TjPageAdapter
import cc.leishui.bilitx.fragment.main.TjFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


open class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TjPageAdapter(activity)
        adapter.addFragment(TjFragment(), "直播")
        adapter.addFragment(TjFragment(), "推荐")
        adapter.addFragment(TjFragment(), "热门")
        adapter.addFragment(TjFragment(), "追番")
        adapter.addFragment(TjFragment(), "影视")
        adapter.addFragment(TjFragment(), "新征程")
        val viewpager = view.findViewById<ViewPager2>(R.id.vp_main)
        val tableLayout = view.findViewById<TabLayout>(R.id.tl_main)
        viewpager.adapter = adapter
        viewpager.currentItem = 1
        viewpager.offscreenPageLimit=6
        TabLayoutMediator(tableLayout, viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

}