package cc.leishui.bilitx.fragment.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.leishui.bilitx.R
import cc.leishui.bilitx.adapter.CatalogueAdapter
import cc.leishui.bilitx.bean.bean.LessonSet

class CatalogueFragment(private val lessonSet: LessonSet) : Fragment() {
    private lateinit var listener:CatalogueAdapter.OnCatalogueItemClick

    fun setCatalogueItemClick(listener: CatalogueAdapter.OnCatalogueItemClick){
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvCatalogue = view.findViewById<RecyclerView>(R.id.rv_catalogue)
        val catalogueAdapter = CatalogueAdapter(requireContext(), lessonSet)
        catalogueAdapter.setCatalogueItemClick(object :CatalogueAdapter.OnCatalogueItemClick{
            override fun onClick(position: Int) {
                listener.onClick(position)
            }
        })
        rvCatalogue.adapter = catalogueAdapter
        rvCatalogue.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalogue, container, false)
    }
}