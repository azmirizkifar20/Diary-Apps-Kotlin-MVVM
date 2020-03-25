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
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentEditDiaryBinding
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModel
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModelFactory

class EditDiaryFragment : Fragment() {

    private lateinit var binding: FragmentEditDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        // memunculkan action button update di toolbar
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_diary, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get argument yang dikirim di homeFragment
        if (arguments != null) {
            val message = arguments!!.getString("message")

            // tampilkan message pada edit text
            binding.etDiaryUpdate.setText(message)
        }

    }

    // create overflow menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_button, menu)
        inflater.inflate(R.menu.menu, menu)
    }

    // ketika item di overflow menu di pilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // memanggil diaryViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactory(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        return when (item.itemId) {
            // jika klik tombol ceklis
            R.id.item_action -> {
                // panggil fun inputCheck, jika true maka masuk ke kondisi if
                if (inputCheck(arguments!!.getLong("id"), diaryViewModel)) {
                    // navigate ke fragment sebelumnya
                    requireView().findNavController().popBackStack()
                    Toast.makeText(requireContext(), R.string.success_update, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.null_message, Toast.LENGTH_SHORT).show()
                }
                return true
            }

            R.id.hapus_diary -> {
                // panggil fun onCLickDelete di diaryViewModel
                diaryViewModel.onClickDelete(arguments!!.getLong("id"))
                // navigate ke fragment sebelumnya
                requireView().findNavController().popBackStack()
                Toast.makeText(requireContext(), R.string.success_remove, Toast.LENGTH_SHORT).show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // fungsi input check, dengan kasih return tipe boolean
    private fun inputCheck(id: Long, diaryViewModel: DiaryViewModel): Boolean {
        // cek input jika edit text kosong maka kasih nilai false
        return when {
            binding.etDiaryUpdate.text.trim().isEmpty() -> false

            else -> {
                doUpdate(id, diaryViewModel)
                true
            }
        }
    }

    // fungsi doUpdate dipanggil ketika lolos pengecekan di fun inputCheck
    private fun doUpdate(id: Long, diaryViewModel: DiaryViewModel) {
        // ambil value di edit text yg sudah di ganti
        val message = binding.etDiaryUpdate.text.toString()
        val date = System.currentTimeMillis()

        // tampung data ke dalam konstruktor Diary
        val diary = Diary(id, message, date)
        // panggil fungsi onClickUpdate di viewModel
        diaryViewModel.onClickUpdate(diary)
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Update Cerita"
    }

}
