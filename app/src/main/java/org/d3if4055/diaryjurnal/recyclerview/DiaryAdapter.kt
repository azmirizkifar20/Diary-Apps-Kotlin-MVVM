package org.d3if4055.diaryjurnal.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.databinding.RecyclerviewDiaryBinding
import org.d3if4055.diaryjurnal.utils.convertLongToDateString

class DiaryAdapter(private val diary: List<Diary>) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
    // buat var listener untuk interface RecyclerViewClickListener
    var listener: RecyclerViewClickListener? = null

    // buat inner class DiaryViewHolder untuk di extend di class DiaryAdapter
    inner class DiaryViewHolder(
        val recyclerviewDiaryBinding: RecyclerviewDiaryBinding // gunakan data binding dgn cara buat layout di xml recycler view
    ) : RecyclerView.ViewHolder(recyclerviewDiaryBinding.root)

    // implement function dari class Adapter
    override fun getItemCount() = diary.size

    // implement function dari class RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryViewHolder(
        // kita inflate file xml recyclerview nya
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.recyclerview_diary, parent, false)
    )

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        // set tangggal dan messagenya untuk ditampilkan
        holder.recyclerviewDiaryBinding.tvDate.text = convertLongToDateString(diary[position].tanggal)
        holder.recyclerviewDiaryBinding.tvMessage.text = diary[position].message

        // set onclick untuk tiap listnya
        holder.recyclerviewDiaryBinding.listDiary.setOnClickListener {
            // panggil turunan method dari interface RecyclerViewClickListener
            listener?.onRecyclerViewItemClicked(it, diary[position])
        }
    }
}