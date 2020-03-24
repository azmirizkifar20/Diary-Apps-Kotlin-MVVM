package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import org.d3if4055.diaryjurnal.MainActivity
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentTambahDiaryBinding
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModel
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModelFactor

class TambahDiaryFragment : Fragment() {

    private lateinit var binding: FragmentTambahDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_tambah_diary, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DiaryViewModel::class.java)

        binding.fabCreate.setOnClickListener {
            diaryViewModel.onClickInsert(binding.etDiary.text.toString())
            it.findNavController().navigate(R.id.action_tambahDiaryFragment_to_homeFragment)
        }
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Cerita Hari Ini"
    }

}
