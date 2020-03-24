package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // fab action
        binding.fab1.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tambahDiaryFragment)
        }

        // untuk ambil diaryViewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DiaryViewModel::class.java)

        // observe diary
        // karena diary isinya LiveData<List<Diary>>, maka perlu di observe agar bisa mendapatkan List<Diary>
        diaryViewModel.diary.observe(viewLifecycleOwner, Observer {
            val recyclerView = binding.rvDiary
            val adapter = DiaryAdapter(it)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
            adapter.listener = this
        })

    }

    // turunan method dari interface RecyclerViewClickListener
    override fun onRecyclerViewItemClicked(view: View, diary: Diary) {

        when(view.id) {
            R.id.list_diary -> {
                val editFragment = EditDiaryFragment()
                val args = Bundle()

                args.putString("message", diary.message)
                editFragment.arguments = args

                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentHomeNav,editFragment)
                transaction?.addToBackStack(null)

                transaction?.commit()
//                view.findNavController().navigate(R.id.action_homeFragment_to_editDiaryFragment)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val application = requireNotNull(this.activity).application
        val dataSource = DiaryDatabase.getInstance(application).DiaryDao
        val viewModelFactory = DiaryViewModelFactor(dataSource, application)
        val diaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(DiaryViewModel::class.java)

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
