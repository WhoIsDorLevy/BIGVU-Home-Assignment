package com.example.bigvuhomeassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bigvuhomeassignment.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideKeyboardIfShown()

        //get arguments
        val args = arguments?.let { SecondFragmentArgs.fromBundle(it) }
        val description = args?.description
        val author = args?.author
        val text = args?.text
        val videoLink = args?.videoLink

        //set arguments on views
        binding.authorTextView2.text = getString(R.string.author_holder, author)
        binding.descriptionTextView2.text = getString(R.string.text_holder, description)
        binding.textTextView.text = getString(R.string.text_holder, text)
        binding.videoView.setVideoPath(videoLink)
        binding.videoView.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}