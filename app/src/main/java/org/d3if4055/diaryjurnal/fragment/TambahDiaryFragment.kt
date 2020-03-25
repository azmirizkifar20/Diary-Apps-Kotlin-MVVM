package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import org.d3if4055.diaryjurnal.MainActivity
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentTambahDiaryBinding
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModel
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModelFactory

class TambahDiaryFragment : Fragment() {

    private lateinit var binding: FragmentTambahDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        // memunculkan action button insert di toolbar
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tambah_diary, container, false)

        return binding.root
    }

    // create overflow menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_button, menu)
    }

    // ketika item di overflow menu di pilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // memanggil diaryViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactory(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        return when(item.itemId) {
            R.id.item_action -> {
                if (inputCheck(diaryViewModel)) {
                    // navigate ke fragment sebelumnya
                    requireView().findNavController().popBackStack()
                    Toast.makeText(requireContext(), R.string.success_insert, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.null_message, Toast.LENGTH_SHORT).show()
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
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
