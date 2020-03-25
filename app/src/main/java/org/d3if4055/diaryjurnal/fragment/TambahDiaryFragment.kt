package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tambah_diary, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // memanggil diaryViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        // action tombol create
        binding.fabCreate.setOnClickListener {
            // panggil fun inputCheck, jika true maka masuk ke kondisi if
            if (inputCheck(diaryViewModel)) {
                // navigate ke fragment sebelumnya
                it.findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), R.string.null_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // fungsi input check, dengan kasih return tipe boolean
    private fun inputCheck(diaryViewModel: DiaryViewModel): Boolean {
        // cek input jika edit text kosong maka kasih nilai false
        return when {
            binding.etDiary.text.trim().isEmpty() -> false
            else -> {
                doInsert(diaryViewModel)
                true
            }
        }
    }

    // fungsi doInsert dipanggil ketika lolos pengecekan di fun inputCheck
    private fun doInsert(diaryViewModel: DiaryViewModel) {
        // ambil value di edit text
        val message = binding.etDiary.text.toString()
        // panggil fungsi onClickInsert di viewModel
        diaryViewModel.onClickInsert(message)
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Cerita Hari Ini"
    }

}
