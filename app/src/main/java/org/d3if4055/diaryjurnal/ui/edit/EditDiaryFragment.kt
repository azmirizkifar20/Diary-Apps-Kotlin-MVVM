package org.d3if4055.diaryjurnal.ui.edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.diaryjurnal.ui.MainActivity

import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentEditDiaryBinding
import org.d3if4055.diaryjurnal.ui.home.DiaryViewModel

@Suppress("SpellCheckingInspection")
class EditDiaryFragment : Fragment() {

    private lateinit var binding: FragmentEditDiaryBinding
    private lateinit var viewModel: DiaryViewModel
    private lateinit var data: Diary

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_edit_diary, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModel.Factory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            data = arguments?.getParcelable("dataDiary")!!
            binding.etDiaryUpdate.setText(data.message)
        }

    }

    // create overflow menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_button, menu)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item_action -> {
                if (inputCheck()) {
                    // back stack
                    requireView().findNavController().popBackStack()
                    Snackbar.make(requireView(), getString(R.string.success_update), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), getString(R.string.null_message), Snackbar.LENGTH_SHORT).show()
                }
                true
            }

            R.id.hapus_diary -> {
                showDialogToDelete()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val register = menu.findItem(R.id.theme)
        register.isVisible = false
    }

    private fun inputCheck(): Boolean {
        return when {
            binding.etDiaryUpdate.text.trim().isEmpty() -> false
            else -> {
                doUpdate()
                true
            }
        }
    }

    private fun doUpdate() {
        val message = binding.etDiaryUpdate.text.toString()
        val date = System.currentTimeMillis()

        val diary = Diary(data.id, message, date)
        viewModel.onClickUpdate(diary)
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Update Cerita"
    }

    private fun showDialogToDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Peringatan!")
        builder.setMessage("Yakin ingin menghapus cerita?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            dialog.dismiss()
            viewModel.onClickDelete(data.id)
            requireView().findNavController().popBackStack()
            Snackbar.make(requireView(), getString(R.string.success_remove), Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
}
