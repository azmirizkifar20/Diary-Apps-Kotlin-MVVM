package org.d3if4055.diaryjurnal.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.d3if4055.diaryjurnal.ui.MainActivity
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentHomeBinding
import org.d3if4055.diaryjurnal.utils.RecyclerViewClickListener

@Suppress("SpellCheckingInspection")
class HomeFragment : Fragment(),
    RecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: DiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModel.Factory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        // set lifecycle
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init UI
        initUI()

        // fab tulis diary action
        binding.fabTulisDiary.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tambahDiaryFragment)
        }

    }

    private fun initUI() {
        viewModel.diary.observe(viewLifecycleOwner, Observer {
            val diaryAdapter = DiaryAdapter(it)
            val recyclerView = binding.rvDiary

            recyclerView.apply {
                this.adapter = diaryAdapter
                this.layoutManager = LinearLayoutManager(requireContext())

            }

            // set click listener
            diaryAdapter.listener = this
        })
    }

    override fun onItemClicked(view: View, diary: Diary) {
        when(view.id) {
            R.id.list_diary -> {
                val bundle = bundleOf("dataDiary" to diary)
                view.findNavController().navigate(R.id.action_homeFragment_to_editDiaryFragment, bundle)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.hapus_diary -> {
                showDialogToDelete()
                true
            }
            R.id.theme -> {
                if (isDarkModeOn()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    })
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogToDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Peringatan!")
        builder.setMessage("Yakin ingin menghapus semua cerita?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            viewModel.onClickClear()
            dialog.dismiss()
            Snackbar.make(requireView(), getString(R.string.success_remove), Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Diary Saya"
    }
}
