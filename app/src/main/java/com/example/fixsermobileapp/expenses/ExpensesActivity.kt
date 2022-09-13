package com.example.fixsermobileapp.expenses

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.fixsermobileapp.R
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixsermobileapp.expenses.adapter.CustomAdapterExpense
import com.example.fixsermobileapp.expenses.adapter.PaginationScrollListener
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.retrofit.ExpenseService
import com.example.fixsermobileapp.retrofit.NetWorkClient
import com.example.fixsermobileapp.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ExpensesActivity : AppCompatActivity() {
    lateinit var expenseAdapter: CustomAdapterExpense
    //private var expenseAdapter: RecyclerView.Adapter<CustomAdapterExpense.MyViewHolder>? = null
    //private var layoutManager: RecyclerView.LayoutManager? = null
    private var layoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var mExpenseList: ArrayList<Expense>? = null
    lateinit var myOnClickListener: View.OnClickListener
    private var removedItems: ArrayList<Int>? = null
    //Pagination
    var isLoading:Boolean = false
    var isLastPage:Boolean =false
    private val PAGE_START = 1
    var currentPage:Int = PAGE_START
    var TOTAL_PAGES:Int = 5
    lateinit var mProgressBar:ProgressBar
    private lateinit var service:ExpenseService
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
        service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        //myOnClickListener =  MyOnClickListener(this
        recyclerView = findViewById(R.id.my_recycler_view_expense)
        mProgressBar = findViewById(R.id.idPBLoading)

        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this@ExpensesActivity)
        recyclerView!!.layoutManager = layoutManager
        expenseAdapter = CustomAdapterExpense()
        scrollListener()
        //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
        //on below lines we are

    }

    fun getExpenses(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show()

        val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()

        val call: Call<ArrayList<Expense>> = service.getAllExpenses()
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()

                    //recyclerView!!.itemAnimator = DefaultItemAnimator()
                    //On bellow lines we are initialising our adapter
                    expenseAdapter = CustomAdapterExpense()
                    //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
                    //on below lines we are
                    recyclerView!!.adapter = expenseAdapter
                    progressDialog.hide()
                }
                else{
                    progressDialog.hide()
                    Toast.makeText(this@ExpensesActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                progressDialog.hide()
                println(t)
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_SHORT).show()
            }


        })
    }

    fun scrollListener(){
        recyclerView!!.addOnScrollListener(object:PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                Toast.makeText(this@ExpensesActivity, ""+currentPage,Toast.LENGTH_SHORT).show()
                loadNextPage()
                //getExpenses()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
        loadFirstPage()
        //getExpenses()
    }

    fun loadNextPage(){
        val call: Call<ArrayList<Expense>> = service.getAllExpenses()
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    expenseAdapter.removeLoadingFooter()
                    isLoading = false
                    mExpenseList = response.body()
                    expenseAdapter.addAll(mExpenseList!!)
                    recyclerView!!.adapter = expenseAdapter
                    if (currentPage != TOTAL_PAGES) expenseAdapter.addLoadingFooter()
                    else isLastPage = true

                }
                else{
                    Toast.makeText(this@ExpensesActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loadFirstPage(){

        //val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        val call: Call<ArrayList<Expense>> = service.getAllExpenses()
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()
                    mProgressBar.visibility = View.GONE
                    expenseAdapter.addAll(mExpenseList!!)
                    recyclerView!!.adapter = expenseAdapter
                    if (currentPage <= TOTAL_PAGES) expenseAdapter.addLoadingFooter()
                    else isLastPage = true
                }
                else{
                    mProgressBar.visibility = View.GONE
                    Toast.makeText(this@ExpensesActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                mProgressBar.visibility = View.GONE
                println("The ERROR..........................."+t+"..........................")
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_SHORT).show()

            }


        })

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

    override fun onResume() {
        super.onResume()
        //loadNextPage()
        Toast.makeText(this, "Resume",Toast.LENGTH_SHORT).show()
    }
}