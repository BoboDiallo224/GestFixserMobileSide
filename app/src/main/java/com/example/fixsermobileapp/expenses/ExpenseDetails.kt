package com.example.fixsermobileapp.expenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.expenses.adapter.ExpenseFreshDetailAdapter
import com.example.fixsermobileapp.expenses.adapter.ExpensePaymentDetailAdapter
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.expenses.entities.FreshExpense
import com.example.fixsermobileapp.expenses.entities.PaymentExpenseType
import com.example.fixsermobileapp.retrofit.ExpenseService
import com.example.fixsermobileapp.retrofit.NetWorkClient
import com.example.fixsermobileapp.utils.Constants
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpenseDetails : AppCompatActivity() {

    lateinit var txtDateDetailExpense:TextView
    lateinit var txtDesignExpenseDetail:TextView
    lateinit var txtCategDetExpense:TextView
    lateinit var txtQteDetExpense:TextView
    lateinit var txtUnitPriceDetExpense:TextView
    lateinit var txtAmountTotalDetExpense:TextView
    lateinit var txtAmountDetExpenseFresh:TextView
    lateinit var txtTotAmountExpAndFreshDet:TextView
    lateinit var txtShowAllResultDetailExpense1:TextView
    lateinit var txtShowAllResultDetailExpense2:TextView
    lateinit var txtAmountPayDetExp:TextView
    lateinit var txtRestAmountDetExp:TextView
    lateinit var toolbar:MaterialToolbar
    lateinit var imgV_addListProdExpense:ImageView
    lateinit var linLayDisplayAmountPay:LinearLayout
    lateinit var crdIndicDetailExpens1:CardView
    lateinit var crdIndicDetailExpens2:CardView
    //Adapters
    lateinit var expenseFreshDetailAdapter:ExpenseFreshDetailAdapter
    lateinit var expensePaymentDetailAdapter: ExpensePaymentDetailAdapter
    //List
    private var mFreshList:ArrayList<FreshExpense> = ArrayList()
    private var mPaymentExpenseList:ArrayList<PaymentExpenseType> = ArrayList()

    //RecycleView
    private var layoutManager: LinearLayoutManager? = null
    private var layoutManagerPayment: LinearLayoutManager? = null
    lateinit var rcvDetailFresh:RecyclerView
    lateinit var rcvDetailPayment:RecyclerView

    //Services
    private lateinit var service: ExpenseService

    //Variables
    var idExpense:Long? = null
    //Dates
    var currentDate:String = ""
    var formatedDate:String = ""
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        initialization()

        setSupportActionBar(toolbar)
        //actionbar
        val actionbar = supportActionBar!!
        //set actionbar title
        actionbar.title = "Detail Depense"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.elevation = 0F

        val extras = intent.extras
        if (extras != null) idExpense = extras.getLong("id_expense")
        if (idExpense !== null) getExpenseById()

        //Clicked Listerner
        imgV_addListProdExpense.setOnClickListener(View.OnClickListener {
            openDailogPayment()
        })

    }

    fun initialization(){
        txtDateDetailExpense = findViewById(R.id.txtDateDetailExpense)
        txtDesignExpenseDetail = findViewById(R.id.txt_designExpenseDetail)
        txtCategDetExpense = findViewById(R.id.txtCategDetExpense)
        txtQteDetExpense = findViewById(R.id.txtQteDetExpense)
        txtUnitPriceDetExpense = findViewById(R.id.txtUnitPriceDetExpense)
        txtAmountTotalDetExpense = findViewById(R.id.txtAmountTotalDetExpense)
        txtAmountDetExpenseFresh = findViewById(R.id.txtAmountDetExpenseFresh)
        txtTotAmountExpAndFreshDet = findViewById(R.id.txtTotAmountExpAndFreshDet)
        txtShowAllResultDetailExpense1 = findViewById(R.id.txtShowAllResultDetailExpense1)
        txtShowAllResultDetailExpense2 = findViewById(R.id.txtShowAllResultDetailExpense2)
        txtAmountPayDetExp = findViewById(R.id.txtAmountPayDetExp)
        txtRestAmountDetExp = findViewById(R.id.txtRestAmountDetExp)
        linLayDisplayAmountPay = findViewById(R.id.linLayDisplayAmountPay)
        toolbar = findViewById(R.id.toolbarDetailExpense)
        imgV_addListProdExpense = findViewById(R.id.imgV_addListProdExpense)
        crdIndicDetailExpens1 = findViewById(R.id.crdIndicDetailExpens1)
        crdIndicDetailExpens2 = findViewById(R.id.crdIndicDetailExpens2)
        //List users
        rcvDetailFresh = findViewById(R.id.rcvDetailFresh)
        rcvDetailPayment = findViewById(R.id.rcvDetailPayment)
        expenseFreshDetailAdapter = ExpenseFreshDetailAdapter()
        expensePaymentDetailAdapter = ExpensePaymentDetailAdapter()
        layoutManager = LinearLayoutManager(this@ExpenseDetails)
        layoutManagerPayment = LinearLayoutManager(this@ExpenseDetails)
        rcvDetailPayment.layoutManager = layoutManagerPayment
        rcvDetailFresh.layoutManager = layoutManager
        //rcvDetailPayment!!.layoutManager = layoutManager
        service = NetWorkClient.getClient(Constants.BASE_URL).create()
    }

    fun getExpenseById(){

        service.getExpenseById(idExpense!!).enqueue(object : Callback<Expense>{
            override fun onResponse(call: Call<Expense>, response: Response<Expense>) {
                if (response.isSuccessful){
                    val expense = response.body()

                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
                    val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                    val dateStr: Date? = formatter.parse(expense!!.expense_date!!)
                    val formatedDate = targetFormat.format(dateStr!!)
                    txtDateDetailExpense.text = formatedDate

                    txtDesignExpenseDetail.text = expense.expense_designation
                    txtCategDetExpense.text = expense.expense_type
                    txtQteDetExpense.text = expense.expense_quantity.toString()
                    txtUnitPriceDetExpense.text = expense.expense_unit_price.toString()

                    //val totalAmount = expense.expense_unit_price!!.times(expense.expense_quantity!!)
                    //Check if expense type is Hangard
                    val totalAmount = if (expense.expense_type == resources.getString(R.string.typeHangard)){
                        linLayDisplayAmountPay.visibility = View.VISIBLE
                        expense.expense_unit_price!!
                    }
                    else{
                        expense.expense_unit_price!!.times(expense.expense_quantity!!)
                    }
                    val totalAmountFresh = expense.freshExpense!!.sumOf { it.fresh_expense_amount!! }
                    txtAmountTotalDetExpense.text = totalAmount.toString()
                    txtAmountDetExpenseFresh.text = totalAmountFresh.toString()
                    txtTotAmountExpAndFreshDet.text = totalAmount.plus(totalAmountFresh).toString()
                    //Fresh Expense
                    mFreshList = expense.freshExpense!!
                    expenseFreshDetailAdapter.addAll(mFreshList)
                    rcvDetailFresh.setHasFixedSize(true)
                    rcvDetailFresh.itemAnimator = DefaultItemAnimator()
                    val dividerItemDecoration = DividerItemDecoration(rcvDetailFresh.context, DividerItemDecoration.VERTICAL)
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this@ExpenseDetails, R.drawable.divider)!!)
                    rcvDetailFresh.addItemDecoration(dividerItemDecoration)
                    rcvDetailFresh.adapter = expenseFreshDetailAdapter

                    //List Payment
                    mPaymentExpenseList = expense.paymentExpenseTypes!!
                    //Calculate TotalPayed and RestPayed
                    val totalAmountPayed = mPaymentExpenseList.sumOf { it.amountPayAddExpense!! }
                    //Set Text to TextView
                    txtAmountPayDetExp.text = totalAmountPayed.toString()
                    val restAmountPayed = expense.expense_unit_price!!.minus(totalAmountPayed)
                    txtRestAmountDetExp.text = restAmountPayed.toString()
                    //
                    expensePaymentDetailAdapter.addAll(mPaymentExpenseList)
                    rcvDetailPayment.addItemDecoration(dividerItemDecoration)
                    rcvDetailPayment.adapter = expensePaymentDetailAdapter

                    //Set Visibilities to Gone when lists are empty
                    if (mFreshList.isEmpty()){
                        crdIndicDetailExpens1.visibility = View.GONE
                        rcvDetailFresh.visibility = View.GONE
                    }
                    if (mPaymentExpenseList.isEmpty()){
                        crdIndicDetailExpens2.visibility = View.GONE
                        rcvDetailPayment.visibility = View.GONE
                        txtShowAllResultDetailExpense2.visibility = View.GONE
                    }

                }
                /*Toast.makeText(this@ExpenseDetails, ""+response.body()!!.expense_designation,
                    Toast.LENGTH_SHORT).show()*/
            }

            override fun onFailure(call: Call<Expense>, t: Throwable) {
                Toast.makeText(this@ExpenseDetails, "ERROR",
                    Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun openDailogPayment(){
        val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.btm_sht_dlog_add_exp_payment, null)
        // on below line we are creating a variable for our button which we are using to dismiss our dialog.
        val btnClose = view.findViewById<ImageButton>(R.id.btn_closeAddPaiExp)
        val edtDatePayExp = view.findViewById<EditText>(R.id.edtDatePayExp)
        val edtAmountPayExp = view.findViewById<EditText>(R.id.edtAmountPayExp)
        val txtRestAmountExp = view.findViewById<TextView>(R.id.txtRestAmountExp)
        val btnAddExpPay = view.findViewById<Button>(R.id.btnAddExpPay)
        updateDateInView()
        //Display Current Date on Edittext
        edtDatePayExp.setText(currentDate)

        //adding click listener for our adding payment button
        btnAddExpPay.setOnClickListener(View.OnClickListener {
            if (validate(edtAmountPayExp) && formatedDate.isNotEmpty()){

                val paymentExpenseType = PaymentExpenseType()
                paymentExpenseType.amountPayAddExpense = edtAmountPayExp.text.toString().toDouble()
                paymentExpenseType.payment_expense_date = formatedDate

                if (txtAmountPayDetExp.toString().isNotEmpty() && txtAmountTotalDetExpense.toString().isNotEmpty()){

                    val totalAmountPayInList = txtAmountPayDetExp.text.toString().toDouble()
                    val totalAmountPay = totalAmountPayInList.plus(paymentExpenseType.amountPayAddExpense!!)
                    val totalAmountExpense = txtAmountTotalDetExpense.text.toString().toDouble()
                    //Check if
                    if (totalAmountExpense >= totalAmountPay){
                        //Call service and insert Payment
                        service.addExpensePayment(paymentExpenseType,idExpense!!).
                        enqueue(object :Callback<PaymentExpenseType>{
                            override fun onResponse(call: Call<PaymentExpenseType>,
                                                    response: Response<PaymentExpenseType>) {


                            }

                            override fun onFailure(call: Call<PaymentExpenseType>, t: Throwable) {

                            }

                        })
                    }
                }

                /*Toast.makeText(this, ""+totalAmountPay.toString()+"\n"+
                        totalAmountExpense.toString(),Toast.LENGTH_LONG).show()*/
            }
        })

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

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val myFormat2 = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
        //edtChooseDate.editText!!.setText(sdf.format(cal.time))
        currentDate = sdf.format(cal.time)
        formatedDate = sdf2.format(cal.time)
    }

    private fun validate(mEditText: EditText): Boolean {
        // check the lenght of the enter data in EditText and give error if its empty
        if (mEditText.text.isNotBlank() && mEditText.text.isNotEmpty()) {
            return true // returns true if field is not empty
        }
        mEditText.error = "Please Fill This"
        mEditText.requestFocus()
        return false
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_update, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menuUpdateExpense){
            //Toast.makeText(this, "Open Activity", Toast.LENGTH_SHORT).show()
            /*val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)*/
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_bottom)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}