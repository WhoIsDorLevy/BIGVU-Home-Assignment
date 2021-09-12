package com.example.bigvuhomeassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.bigvuhomeassignment.databinding.FragmentFirstBinding
import org.json.JSONArray
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val requestUrl: String =
        "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json"
    private var searchQuery: String = ""
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnSearchClickListener({

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onResponse(response: JSONArray){
        val list = ArrayList<HashMap<String,String>>()
        val from = arrayOf("name", "description")
        val to = intArrayOf(R.id.authorTextView, R.id.descriptionTextView)


        //create the mapping list for the regular SimpleAdapter
        for (i in 0 until response.length()) {
            val currMap = HashMap<String,String>()
            val currObject = response.getJSONObject(i)
            currMap["name"] = currObject["name"] as String
            currMap["description"] = currObject["description"] as String
            list.add(currMap)

        }

        val adapter =
            context?.let { CustomSimpleAdapter(it, list, R.layout.list_item, from, to, response) }!!

        binding.listView.adapter = adapter
        binding.loadingTextView.visibility = View.INVISIBLE
        binding.progressBarMain.visibility = View.INVISIBLE

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

    }
}