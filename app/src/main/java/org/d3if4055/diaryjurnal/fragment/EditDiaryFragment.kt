package org.d3if4055.diaryjurnal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.d3if4055.diaryjurnal.MainActivity

import org.d3if4055.diaryjurnal.R
import org.d3if4055.diaryjurnal.database.Diary
import org.d3if4055.diaryjurnal.database.DiaryDatabase
import org.d3if4055.diaryjurnal.databinding.FragmentEditDiaryBinding
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModel
import org.d3if4055.diaryjurnal.viewmodel.DiaryViewModelFactor

class EditDiaryFragment : Fragment() {

    private lateinit var binding: FragmentEditDiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        judul()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_diary, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun judul() {
        val getActivity = activity!! as MainActivity
        getActivity.supportActionBar?.title = "Update Cerita"
    }

}
