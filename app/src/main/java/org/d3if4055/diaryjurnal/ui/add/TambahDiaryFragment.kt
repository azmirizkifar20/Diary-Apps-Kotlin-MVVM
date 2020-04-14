package org.d3if4055.diaryjurnal.ui.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.diaryjurnal.ui.MainActivity
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentTambahDiaryBinding
import org.d3if4055.diaryjurnal.ui.home.DiaryViewModel

@Suppress("SpellCheckingInspection")
class TambahDiaryFragment : Fragment() {

    private lateinit var binding: FragmentTambahDiaryBinding
    private lateinit var viewModel: DiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        // show action bar
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_tambah_diary, container, false)

        // set viewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModel.Factory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_button, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.item_action -> {
                if (inputCheck()) {
                    // navigate ke fragment sebelumnya
                    requireView().findNavController().popBackStack()
                    Snackbar.make(requireView(), getString(R.string.success_insert), Snackbar.LENGTH_SHORT).show()

                } else {
                    Snackbar.make(requireView(), getString(R.string.null_message), Snackbar.LENGTH_SHORT).show()
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun inputCheck(): Boolean {
        return when {
            binding.etDiary.text.trim().isEmpty() -> false
            else -> {
                viewModel.onClickInsert(binding.etDiary.text.toString())
                true
            }

        }
    }


    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Cerita Hari Ini"
    }

}
