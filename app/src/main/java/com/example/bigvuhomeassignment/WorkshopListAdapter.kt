package com.example.bigvuhomeassignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import java.lang.Exception

class WorkshopListAdapter(
    private val mActivity: MainActivity,
    private val originalData: ArrayList<ArrayList<String>>,
    ): BaseAdapter(), Filterable {

    private var filteredData = originalData

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Any {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return filteredData.indexOf(getItem(position)).toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val inflater = mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var listItem = convertView
        if (listItem == null){
            listItem = inflater.inflate(R.layout.list_item, null)
        }

        //get views to set
        val author = listItem?.findViewById(R.id.authorTextView) as TextView
        val description = listItem?.findViewById(R.id.descriptionTextView) as TextView
        val image = listItem?.findViewById(R.id.imageView) as ImageView

        //set values to views
        val currData = filteredData[position]
        author.text = currData[0]
        description.text = currData[1]
        Picasso.get().load(currData[2]).into(image, object :com.squareup.picasso.Callback{
            val progress: ProgressBar = listItem.findViewById(R.id.progressBarImage)
            override fun onSuccess() {

                progress.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                progress.visibility = View.INVISIBLE
                val errorImage: ImageView = listItem.findViewById(R.id.errorImageView)
                errorImage.visibility = View.VISIBLE
            }

        })

        //set onClick to move to next fragment
        val onClickListener: (View) -> Unit = { view ->
            //reset search text
            val searchView = mActivity.findViewById(R.id.searchView) as SearchView
            searchView.setQuery("", false)

            //pass data of the clicked item to next fragment
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                currData[0],
                currData[1],
                currData[3],
                currData[4]
            )

            //navigate to next fragment
            view.findNavController().navigate(action)
        }

        listItem?.setOnClickListener(onClickListener)
        image.setOnClickListener(onClickListener)

        //finally return view
        return listItem
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null && constraint.isNotEmpty()){
                    val queryString = constraint.toString().lowercase()
                    val filters = originalData.filter { it[1].lowercase().contains(queryString) }

                    filterResults.count = filters.size
                    filterResults.values = filters
                }
                else {
                    filterResults.count = filteredData.size
                    filterResults.values = filteredData
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as ArrayList<ArrayList<String>>
                notifyDataSetChanged()
            }

        }
    }
}