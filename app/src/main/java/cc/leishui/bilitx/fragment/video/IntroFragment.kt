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
import cc.leishui.bilitx.adapter.video.IntroAdapter
import cc.leishui.bilitx.bean.biliBean.BVideo
import cc.leishui.bilitx.network.bili.BiliRepo

class IntroFragment(private val video: BVideo) : Fragment() {


    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val introAdapter = IntroAdapter(requireContext())
        view.findViewById<RecyclerView>(R.id.rv_intro).adapter = introAdapter
        view.findViewById<RecyclerView>(R.id.rv_intro).layoutManager = LinearLayoutManager(requireContext())
        BiliRepo.getVideoDetail(video.bvid,{
            introAdapter.setDetail(it)
            introAdapter.notifyDataSetChanged()
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }
}