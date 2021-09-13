package com.example.bigvuhomeassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bigvuhomeassignment.databinding.FragmentFirstBinding
import org.json.JSONArray

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val requestUrl: String =
        "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val jsonArrReq = JsonArrayRequest(
            Request.Method.GET, requestUrl, null,
            { response ->
                onResponse(response)
            },
            {
                Toast.makeText(activity, "An error occurred,\nPlease check your internet connection", Toast.LENGTH_LONG).show()
                println("An error occurred,\nPlease check your internet connection")
            })

        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(jsonArrReq) }
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onResponse(response: JSONArray){

        //convert jsonArray to a mutable ArrayList for the WorkshopListAdapter
        val dataList = ArrayList<ArrayList<String>>()
        for (i in 0 until response.length()){
            val currList = ArrayList<String>()
            val currObject = response.getJSONObject(i)
            currList.add(currObject.getString("name"))
            currList.add(currObject.getString("description"))
            currList.add(currObject.getString("image"))
            currList.add(currObject.getString("text"))
            currList.add(currObject.getString("video"))
            dataList.add(currList)
        }

        //create the adapter
        val adapter = activity?.let { WorkshopListAdapter(it as MainActivity, dataList) }
        binding.listView.adapter = adapter

        //make loading and spinner disappear, now that the adapter is set
        binding.loadingTextView.visibility = View.INVISIBLE
        binding.progressBarMain.visibility = View.INVISIBLE

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                (activity as MainActivity).hideKeyboardIfShown()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter?.filter?.filter(p0)
                return true
            }

        })

        binding.searchView.setOnCloseListener(SearchView.OnCloseListener {
            binding.searchView.setQuery("", false)
            adapter?.filter?.filter(binding.searchView.query)
            return@OnCloseListener false
        })


    }
}