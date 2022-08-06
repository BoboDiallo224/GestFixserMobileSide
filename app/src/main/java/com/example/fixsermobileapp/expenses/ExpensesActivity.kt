package com.example.fixsermobileapp.expenses

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.example.fixsermobileapp.R
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixsermobileapp.expenses.adapter.CustomAdapterExpense
import com.example.fixsermobileapp.expenses.entities.DataModelExpense
import com.google.android.material.bottomsheet.BottomSheetDialog

class ExpensesActivity : AppCompatActivity() {
    private var adapter: RecyclerView.Adapter<CustomAdapterExpense.MyViewHolder>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var data: ArrayList<DataModelExpense>? = null
    lateinit var myOnClickListener: View.OnClickListener
    private var removedItems: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        //actionbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_menu_expense)
        setSupportActionBar(toolbar)
        //actionbar
        val actionbar = supportActionBar!!
        //set actionbar title
        actionbar.title = "Depense"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.elevation = 0F

        val btn =findViewById<Button>(com.example.fixsermobileapp.R.id.btn)
        btn.setOnClickListener(View.OnClickListener {
            /*Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show()

            val redLayout = findViewById<View>(R.id.redLayout)
            val parent = findViewById<ViewGroup>(R.id.parent_relative)

            val transition: Transition = Slide(Gravity.BOTTOM)
            transition.setDuration(600)
            transition.addTarget(R.id.redLayout)

            TransitionManager.beginDelayedTransition(parent, transition)
            redLayout.visibility = if (redLayout.isGone) View.VISIBLE else View.GONE*/
            showButtomSheetModal()
        })
        //Test
        //myOnClickListener =  MyOnClickListener(this
        recyclerView = findViewById(R.id.my_recycler_view_expense)
        recyclerView!!.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        //recyclerView!!.itemAnimator = DefaultItemAnimator()
        /*data = ArrayList()
        for ( i in  MyData.nameArray.indices) {
            data!!.add(
                DataModelExpense(
                    MyData.nameArray[i],
                    MyData.versionArray.get(i),
                    MyData.id_.get(i),
                    MyData.drawableArray.get(i)
                )
            )

        }*/
        //On bellow lines we are initialising our adapter
        adapter = CustomAdapterExpense()
        //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
        //on below lines we are
        recyclerView!!.adapter = adapter
    }

    fun showButtomSheetModal(){
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_dateexepense_search, null)
        // on below line we are creating a variable for our button which we are using to dismiss our dialog.
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        // on below line we are adding on click listener // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)
        // on below line we are setting // content view to our view.
        dialog.setContentView(view)
        // on below line we are calling // a show method to display a dialog.
        dialog.show()
    }

    fun View.slideDown(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun View.slideUp(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_expense, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search_expense).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        // Initialise menu item search bar
        // with id and take its object
        val searchViewItem:MenuItem = menu.findItem(R.id.search_expense)
        val searchView:SearchView = searchViewItem.actionView as SearchView

        // Expand the search view and request focus
        searchViewItem.expandActionView();
        searchView.requestFocus();

        // search queryTextChange Listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                /*if (list.contains(query)) {
                        adapter.getFilter().filter(query);
                    }*/
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                /*adapter.getFilter().filter(query);*/
                Log.d("onQueryTextChange", "query: " + query)
                return true
            }
        })

        //Expand Collapse listener
        searchViewItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                //showToast("Action Collapse")
                return true
            }

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                //showToast("Action Expand")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_add_expense){
            //Toast.makeText(this, "Open Activity", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_bottom)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}