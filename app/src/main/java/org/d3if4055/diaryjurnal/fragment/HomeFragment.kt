package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if4055.diaryjurnal.MainActivity
import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentHomeBinding
import org.d3if4055.diaryjurnal.recyclerview.DiaryAdapter
import org.d3if4055.diaryjurnal.recyclerview.RecyclerViewClickListener
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModel
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModelFactor

class HomeFragment : Fragment(),
    RecyclerViewClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        // memunculkan overflow menu
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // untuk ambil diaryViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        // observe diary
        // karena diary isinya LiveData<List<Diary>>, maka perlu di observe agar bisa mendapatkan List<Diary>
        diaryViewModel.diary.observe(viewLifecycleOwner, Observer {
            val adapter = DiaryAdapter(it)
            val recyclerView = binding.rvDiary
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

            // set listener untuk onRecyclerViewItemClicked
            adapter.listener = this
        })

        // fab tulis diary action
        binding.fabTulisDiary.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tambahDiaryFragment)
        }

    }

    // turunan method dari interface RecyclerViewClickListener
    override fun onRecyclerViewItemClicked(view: View, diary: Diary) {

        // kondisi jika view di recyclerview di klik
        when(view.id) {
            // jika id ini diklik (ctrl + klik list_diary untuk lihat)
            R.id.list_diary -> {
                // buat bundle untuk dikirim ke fragment edit diary
                val bundle = Bundle()
                bundle.putLong("id", diary.id)
                bundle.putString("message", diary.message)
                // navigate ke fragment edit diary sekaligus mengirim bundle
                view.findNavController().navigate(R.id.action_homeFragment_to_editDiaryFragment, bundle)
            }
        }

    }

    // untuk membuat overflow menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    // ketika item di overflow menu di pilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProvider(this, viewModelFactory).get(DiaryViewModel::class.java)

        return when (item.itemId) {
            R.id.hapus_diary -> {
                diaryViewModel.onClickClear()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Diary Saya (6706184055)"
    }
}
