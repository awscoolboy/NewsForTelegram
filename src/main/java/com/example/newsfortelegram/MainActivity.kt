package com.example.newsfortelegram

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsfortelegram.models.NewsModel
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {


    var newsList : List<NewsModel>? = null
    var adapter: ListAdapter ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

        }

        adapter= ListAdapter(con = this, newsList = newsList)

        listView.layoutManager=GridLayoutManager(this,3)
        listView.adapter= adapter


        getData()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun getData(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://telegramnews-a456a.firebaseio.com/iran.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val gson = Gson()
                newsList = gson.fromJson(response, Array<NewsModel>::class.java).toList()
                adapter?.setList(newsList!!)
                adapter?.notifyDataSetChanged()
                progessFrame.visibility=View.GONE


            },
            Response.ErrorListener {})
        queue.add(stringRequest)
    }



    class ListAdapter(
        var con: Context,
        var newsList: List<NewsModel>?
    ) : RecyclerView.Adapter<ListAdapter.NewsItemHolder>(){




        fun setList (list:List<NewsModel>){
            newsList=list
        }

        inner class NewsItemHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
        {
            fun bind(newsItem : NewsModel){

                val nameTextView = itemView.findViewById(R.id.app_title) as TextView
                val logoImagView = itemView.findViewById(R.id.app_logo) as ImageView
                val moreImageView = itemView.findViewById(R.id.more) as ImageView

                nameTextView.text=newsItem.name
                Glide.with(con)
                    .load(newsItem.image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(logoImagView)


            }
        }

        override fun onBindViewHolder(holder: NewsItemHolder, pos: Int) {
            var newsItem = newsList?.get(pos)
               newsItem?.let { holder.bind(it) }

        }


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewsItemHolder {
            val v=LayoutInflater.from(con).inflate(R.layout.list_item,p0,false)
            return NewsItemHolder(v)
        }


        override fun getItemCount(): Int {

            if(newsList != null){
                return newsList!!.size
            }else{
                return 0
            }
        }




    }

}
