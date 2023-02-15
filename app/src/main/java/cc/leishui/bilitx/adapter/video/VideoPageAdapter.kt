package cc.leishui.bilitx.adapter.video

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VideoPageAdapter(
    fm: FragmentManager,
    private val fragmentT: Fragment,
    private val fragmentF: Fragment
) : FragmentPagerAdapter(
    fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var title = "评论"
    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return fragmentT
            1 -> return fragmentF
        }
        return fragmentT
    }

    fun setTitle(title:String){
        this.title = title
        notifyDataSetChanged()
    }
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->return "简介"
            1->return title
        }
        return null
    }


}