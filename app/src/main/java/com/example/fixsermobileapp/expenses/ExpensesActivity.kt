package com.example.fixsermobileapp.expenses

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.fixsermobileapp.R
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fixsermobileapp.expenses.adapter.CustomAdapterExpense
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.retrofit.ExpenseService
import com.example.fixsermobileapp.retrofit.NetWorkClient
import com.example.fixsermobileapp.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fixsermobileapp.expenses.adapter.SearchExpenseByDateAdapter
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.facebook.shimmer.ShimmerFrameLayout





class ExpensesActivity : AppCompatActivity() {

    //Views
    lateinit var txtSearchByAll:TextView
    lateinit var txtExpenseByDate:TextView
    lateinit var edtChooseDate:TextInputLayout
    lateinit var edtChooseDate1:TextInputLayout
    lateinit var edtChooseDate2:TextInputLayout
    //Relative Layout
    lateinit var parentRelativeExpense:RelativeLayout
    lateinit var mProgressBar:ProgressBar
    private lateinit var nestedSV: NestedScrollView
    lateinit var dialog:BottomSheetDialog

    //Pagination
    var isLoading2:Boolean = false
    var isLastPage2:Boolean =false
    private val PAGE_START = -1
    var currentPage:Int = 0
    var TOTAL_PAGES:Int = 5

    //Dates
    var cal = Calendar.getInstance()
    var isOneDate:Boolean = false
    var isTwoDates1:Boolean = false
    var isTwoDates2:Boolean = false
    var searchDate:String = ""
    var searchBetweenDates1:String = ""
    var searchBetweenDates2:String = ""

    var expenseCategory2:String? = null

    //RecycleViews
    private var layoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewCategory: RecyclerView? = null

    //Adapters
    private lateinit var searchExpenseAdapter:SearchExpenseByDateAdapter
    lateinit var expenseAdapter: CustomAdapterExpense
    //List
    private var expensesCategories: ArrayList<String>? = ArrayList()
    private var mExpenseList: ArrayList<Expense>? = null

    //Services
    private lateinit var service:ExpenseService

    //Chargement Shimmer
    private var mFrameLayout: ShimmerFrameLayout? = null
    //Interface
    private val mOnSearchExpenseClickListener = object:OnSearchExpenseClickListener{
        override fun onSearchItem(position: Int, expenseCategorie: String) {
            expenseCategory2 = expenseCategorie
            getExpenseByCategory()
            txtSearchByAll.text = expenseCategory2.toString()
            txtSearchByAll.background = ContextCompat.getDrawable(this@ExpensesActivity, R.drawable.rounded_corner)
            //txtSearchByAll.setTextColor(resources.getColor(R.color.white,resources.newTheme()))
            Toast.makeText(this@ExpensesActivity, expenseCategory2,Toast.LENGTH_SHORT).show()
        }

    }

    private val mOnExpenseItemClickListener2 = object :OnExpenseItemClickListener2{
        override fun getExpenseId(position: Int, model: Expense) {

            val intent = Intent(this@ExpensesActivity, ExpenseDetails::class.java)
            intent.putExtra("id_expense",model.id_expense)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_bottom)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        initialisation()

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


        //myOnClickListener =  MyOnClickListener(this
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this@ExpensesActivity)
        recyclerView!!.layoutManager = layoutManager
        //expenseAdapter = CustomAdapterExpense()
        //scrollListener()
        getExpenses()
        //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
        //on below lines we are
        //Implementation On clicked Listeners
        txtSearchByAll.setOnClickListener(View.OnClickListener {

            showButtomSheetModal()
        })

        txtExpenseByDate.setOnClickListener(View.OnClickListener {
            showButtomSheetDateModal()
        })

        nestedSV.setOnScrollChangeListener(OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                currentPage++
                // on below line we are making our progress bar visible.
                mProgressBar.visibility = View.VISIBLE

                if (currentPage < 20) {
                    // on below line we are again calling // a method to load data in our array list.
                    getExpenses()
                }
            }
        })
    }

    fun initialisation(){
        dialog = BottomSheetDialog(this)
        txtSearchByAll = findViewById(R.id.txtExpenseAllCategories)
        txtExpenseByDate = findViewById(R.id.txtExpenseByDate)
        recyclerView = findViewById(R.id.my_recycler_view_expense)
        mProgressBar = findViewById(R.id.idPBLoading)
        nestedSV = findViewById(R.id.idNestedSV)
        mFrameLayout = findViewById(R.id.shimmerLayout)
        parentRelativeExpense = findViewById(R.id.parent_relative_expense)
        service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
    }

    fun getExpenses(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        //progressDialog.show()
        mProgressBar.visibility = View.VISIBLE

        val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()

        val call: Call<ArrayList<Expense>> = service.getAllExpenses()
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()

                    //recyclerView!!.itemAnimator = DefaultItemAnimator()
                    //On bellow lines we are initialising our adapter
                    expenseAdapter = CustomAdapterExpense(mExpenseList!!,mOnExpenseItemClickListener2)
                    //expenseAdapter.addAll(mExpenseList!!)
                    //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
                    //on below lines we are
                    recyclerView!!.adapter = expenseAdapter
                    //Shimmer
                    mFrameLayout!!.startShimmer()
                    mFrameLayout!!.visibility = View.GONE
                    recyclerView!!.visibility = View.VISIBLE
                    //parentRelativeExpense.setBackgroundColor(resources.getColor(R.color.purple_200, null))

                    progressDialog.hide()
                    mProgressBar.visibility = View.GONE
                }
                else{
                    progressDialog.hide()
                    mProgressBar.visibility = View.GONE
                    mFrameLayout!!.stopShimmer()
                    mFrameLayout!!.visibility = View.GONE
                    Toast.makeText(this@ExpensesActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                progressDialog.hide()
                mFrameLayout!!.stopShimmer()
                mFrameLayout!!.visibility = View.GONE
                mProgressBar.visibility = View.GONE
                println(t)
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_SHORT).show()
            }


        })
    }

    fun showButtomSheetModal(){
        // on below line we are creating a new bottom sheet dialog.
        //val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_search_categories, null)
        // on below line we are creating a variable for our button which we are using to dismiss our dialog.
        recyclerViewCategory = view.findViewById(R.id.rcv_searchEnterCategories)
        val btnAllCategories = view.findViewById<TextView>(R.id.txt_searchEnterAllCategories)

        layoutManager = LinearLayoutManager(this@ExpensesActivity)
        //recyclerView!!.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this@ExpensesActivity,2)
        recyclerViewCategory!!.layoutManager = gridLayoutManager
        searchExpenseAdapter = SearchExpenseByDateAdapter(this@ExpensesActivity, mOnSearchExpenseClickListener)
        getExpenseCategories()
        recyclerViewCategory!!.adapter = searchExpenseAdapter

        btnAllCategories.setOnClickListener {
            // on below line we are calling a dismiss // method to close our dialog.
            getExpenses()
            txtSearchByAll.text = "All Categories"
            txtSearchByAll.background = ContextCompat.getDrawable(this@ExpensesActivity, R.drawable.rounded_corner)
            //txtSearchByAll.setTextColor(resources.getColor(R.color.white,resources.newTheme()))
            dialog.dismiss()
        }

        // below line is use to set cancelable to avoid // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)

        // on below line we are setting // content view to our view.
        dialog.setContentView(view)
        // on below line we are calling // a show method to display a dialog.
        dialog.show()
    }

    fun showButtomSheetDateModal(){
        // on below line we are creating a new bottom sheet dialog.
        //val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_searchby_date, null)

        val btnSearchByDate = view.findViewById<Button>(R.id.btn_resultByDate)
        val btnSearchBetweenDates = view.findViewById<Button>(R.id.btn_resultBetweenDates)

        edtChooseDate = view.findViewById(R.id.edtChooseDate)
        edtChooseDate1 = view.findViewById(R.id.edtChooseBetwDate1)
        edtChooseDate2 = view.findViewById(R.id.edtChooseBetwDate2)
        // create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        edtChooseDate.editText!!.setOnClickListener {
            DatePickerDialog(this@ExpensesActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

            isOneDate = true; isTwoDates1 = false ; isTwoDates2 = false
            // on below line we are calling a dismiss // method to close our dialog.
            //dialog.dismiss()
        }
        //Between Two Dates
        edtChooseDate1.editText!!.setOnClickListener {
            DatePickerDialog(this@ExpensesActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
            isOneDate = false; isTwoDates1 = true ; isTwoDates2 = false

        }

        edtChooseDate2.editText!!.setOnClickListener {
            DatePickerDialog(this@ExpensesActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
            isOneDate = false; isTwoDates1 = false ; isTwoDates2 = true
            // on below line we are calling a dismiss // method to close our dialog.
            //dialog.dismiss()
        }

        btnSearchByDate.setOnClickListener(View.OnClickListener {
            if (validate(edtChooseDate)){
                getExpenseByDate(searchDate)
                dialog.dismiss()
            }
        })

        btnSearchBetweenDates.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this@ExpensesActivity, searchBetweenDates1, Toast.LENGTH_SHORT).show()
            if (validate(edtChooseDate1) && validate(edtChooseDate2)) {
                getExpenseBetweenDates(searchBetweenDates1,searchBetweenDates2)
                dialog.dismiss()
            }
        })

        /*val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        // on below line we are adding on click listener // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss // method to close our dialog.
            dialog.dismiss()
        }*/

        // below line is use to set cancelable to avoid // closing of dialog box when clicking on the screen.
        dialog.setCancelable(true)
        dialog.behavior.skipCollapsed = true

        // on below line we are setting // content view to our view.
        dialog.setContentView(view)
        // on below line we are calling // a show method to display a dialog.
        dialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val myFormat2 = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val sdf2 = SimpleDateFormat(myFormat2, Locale.US)

        when {
            isOneDate -> {
                edtChooseDate.editText!!.setText(sdf.format(cal.time))
                searchDate = sdf2.format(cal.time)
                //Toast.makeText(this@ExpensesActivity, searchDate.toString(), Toast.LENGTH_SHORT).show()
            }
            isTwoDates1 -> {
                edtChooseDate1.editText!!.setText(sdf.format(cal.time))
                searchBetweenDates1 = sdf2.format(cal.time)
                //Toast.makeText(this@ExpensesActivity, searchBetweenDates1.toString(), Toast.LENGTH_SHORT).show()
            }
            isTwoDates2 -> {
                edtChooseDate2.editText!!.setText(sdf.format(cal.time))
                searchBetweenDates2 = sdf2.format(cal.time)
                //Toast.makeText(this@ExpensesActivity, searchBetweenDates2.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getExpenseCategories(){

        //val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        val call: Call<ArrayList<String>> = service.getExpenseCategories()
        call.enqueue(object :Callback<ArrayList<String>>{

            override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                if (response.isSuccessful){
                    expensesCategories = response.body()
                    searchExpenseAdapter.addAll(expensesCategories!!)
                }
                else{
                    Toast.makeText(this@ExpensesActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
                println("The ERROR..........................."+t+"..........................")
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_SHORT).show()

            }


        })

    }

    fun getExpenseByCategory(){
        mProgressBar.visibility = View.VISIBLE
        //val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        val call: Call<ArrayList<Expense>> = service.getExpenseByCategory(expenseCategory2!!)
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()

                    //recyclerView!!.itemAnimator = DefaultItemAnimator()
                    //On bellow lines we are initialising our adapter
                    //expenseAdapter = CustomAdapterExpense()
                    expenseAdapter = CustomAdapterExpense(mExpenseList!!,mOnExpenseItemClickListener2)
                    //expenseAdapter.addAll(mExpenseList!!)
                    //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
                    //on below lines we are
                    recyclerView!!.adapter = expenseAdapter
                    mProgressBar.visibility = View.GONE
                    dialog.dismiss()
                }
                else{
                    mProgressBar.visibility = View.GONE
                    Toast.makeText(this@ExpensesActivity,"Unreachable server error 500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                mProgressBar.visibility = View.GONE
                println(t)
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_LONG).show()
            }


        })

    }

    fun getExpenseBetweenDates(dte1:String, dte2:String){
        mProgressBar.visibility = View.VISIBLE
        //val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        val call: Call<ArrayList<Expense>> = service.getAllExpenseBetweenDates(dte1,dte2)
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()

                    //recyclerView!!.itemAnimator = DefaultItemAnimator()
                    //On bellow lines we are initialising our adapter
                    //expenseAdapter = CustomAdapterExpense()
                    expenseAdapter = CustomAdapterExpense(mExpenseList!!,mOnExpenseItemClickListener2)
                    //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
                    //on below lines we are
                    recyclerView!!.adapter = expenseAdapter
                    mProgressBar.visibility = View.GONE
                    dialog.dismiss()
                }
                else{
                    mProgressBar.visibility = View.GONE
                    Toast.makeText(this@ExpensesActivity,"Unreachable server error 500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                mProgressBar.visibility = View.GONE
                println(t)
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_LONG).show()
            }


        })

    }

    fun getExpenseByDate(dte1:String){
        mProgressBar.visibility = View.VISIBLE
        //val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()
        val call: Call<ArrayList<Expense>> = service.getAllExpenseByDate(dte1)
        call.enqueue(object :Callback<ArrayList<Expense>>{

            override fun onResponse(call: Call<ArrayList<Expense>>, response: Response<ArrayList<Expense>>) {
                if (response.isSuccessful){
                    mExpenseList = response.body()

                    //recyclerView!!.itemAnimator = DefaultItemAnimator()
                    //On bellow lines we are initialising our adapter
                    //expenseAdapter = CustomAdapterExpense()
                    expenseAdapter = CustomAdapterExpense(mExpenseList!!,mOnExpenseItemClickListener2)
                    //recyclerView!!.scrollToPosition(adapter!!.itemCount -1);
                    //on below lines we are
                    recyclerView!!.adapter = expenseAdapter
                    mProgressBar.visibility = View.GONE
                    dialog.dismiss()
                }
                else{
                    mProgressBar.visibility = View.GONE
                    Toast.makeText(this@ExpensesActivity,"Unreachable server error 500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<Expense>>, t: Throwable) {
                mProgressBar.visibility = View.GONE
                println(t)
                Toast.makeText(this@ExpensesActivity,"Failed"+t, Toast.LENGTH_LONG).show()
            }


        })

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

    private fun validate(mEditText: TextInputLayout): Boolean {
        // check the lenght of the enter data in EditText and give error if its empty
        if (mEditText.editText!!.text.isNotBlank() && mEditText.editText!!.text.isNotEmpty()) {
            return true // returns true if field is not empty
        }
        mEditText.editText!!.error = "Please Fill This"
        mEditText.editText!!.requestFocus()
        return false
    }

    override fun onResume() {
        super.onResume()
        //loadNextPage()
        mFrameLayout!!.startShimmer();
        getExpenses()
    }

    override fun onPause() {
        mFrameLayout!!.stopShimmer()
        super.onPause()
    }
}