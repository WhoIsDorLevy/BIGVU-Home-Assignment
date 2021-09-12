package com.example.bigvuhomeassignment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.SimpleAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.util.*
import kotlin.collections.HashMap

class CustomSimpleAdapter(
    mContext: Context,
    private val data: MutableList<HashMap<String, String>>,
    @LayoutRes
    res: Int,
    from: Array<String>,
    @IdRes
    to: IntArray,
    private val jsonArray: JSONArray
) :

// Passing these params to SimpleAdapter
    SimpleAdapter(mContext, data, res, from, to) {
    private var currData: MutableList<HashMap<String, String>> = data
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // Get the view in our case list_item.xml
        val view = super.getView(position, convertView, parent)

        val currJSONObj = jsonArray.getJSONObject(position)
        val avatarImageView = view.findViewById<ImageView>(R.id.imageView)
        val imageURL = currJSONObj["image"] as String

        //load image into view
        Picasso.get().load(imageURL).into(avatarImageView)

        //jump to workShop details in case of a click
        view.setOnClickListener( View.OnClickListener { view ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(
                currJSONObj["name"] as String,
                currJSONObj["description"] as String,
                currJSONObj["text"] as String,
                currJSONObj["video"] as String
            )
            view.findNavController().navigate(action)
        })



        // Finally returning our view
        return view
    }


    //implement search method
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val queryString = p0?.toString()?.lowercase(Locale.getDefault())
                val filterResults = FilterResults()

                //get all results which include the query string
                currData = if (queryString==null || queryString.isEmpty())
                    data
                else
                    data.filter { it["description"]?.lowercase(Locale.getDefault())?.contains(queryString) == true } as MutableList<HashMap<String, String>>
                filterResults.values = currData
                filterResults.count = currData.size
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                currData = p1?.values as MutableList<HashMap<String, String>>
                notifyDataSetChanged()
            }

        }
    }
}