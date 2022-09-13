package com.example.fixsermobileapp.expenses

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.fixsermobileapp.R
import android.widget.EditText
import com.example.fixsermobileapp.expenses.entities.Expense
import com.example.fixsermobileapp.retrofit.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.expenses.adapter.ExpenseFreshAdapter
import com.example.fixsermobileapp.expenses.adapter.ExpenseTypeAdapter
import com.example.fixsermobileapp.expenses.entities.TypeExpense
import com.example.fixsermobileapp.expenses.entities.FreshExpense
import com.example.fixsermobileapp.retrofit.ExpenseService
import com.example.fixsermobileapp.retrofit.NetWorkClient
import com.example.fixsermobileapp.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.create
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddExpenseActivity : AppCompatActivity() {

    lateinit var edtDesignationExpense:EditText
    lateinit var edtQuantityExpense:EditText
    lateinit var edtUnitPriceExpense:EditText
    lateinit var spinTypeExpense:Spinner
    lateinit var edtdateExpense:EditText
    lateinit var btnaddExpense:Button
    lateinit var txtAddExpenseFresh:TextView
    lateinit var txtTotalAmountExpense:TextView

    lateinit var edtDesignationFresh:EditText
    lateinit var edtAmountFresh:EditText

    var currentDate:Date? = null
    var dateString:String? = null
    var sqlDate:java.sql.Date? = null
    var selecteTypeExpense:String = ""
    var totalAmountExpense:Double = 0.0
    private var typeListExpense = arrayListOf<TypeExpense>()
    lateinit var expenseFreshAdapter:ExpenseFreshAdapter
    private var modelToBeUpdated:Stack<FreshExpense> = Stack()

    private val mOnExpenseItemClickListener =
        object : OnExpenseItemClickListener {
            override fun onUpdate(position: Int, model: FreshExpense) {
                // store this model that we want to update
                // we will .pop() it when we want to update
                // the item in the adapter
                modelToBeUpdated.add(model)
                edtDesignationFresh.setText(model.fresh_expense_designation)
                edtAmountFresh.setText(model.fresh_expense_amount.toString())
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_menu_add_expense)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.black_white))
        val actionbar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)

        //binding id
        initialisation()

        expenseFreshAdapter = ExpenseFreshAdapter(this@AddExpenseActivity,mOnExpenseItemClickListener)

        dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        //val sdf = SimpleDateFormat("dd/MM/yyyy") yyyy-MM-dd HH:mm:ss
        val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        /*val sdf2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateString = sdf2.format(Date())*/
        currentDate = sdf2.parse(dateString)

        Toast.makeText(this,""+sqlDate, Toast.LENGTH_SHORT).show()
        edtdateExpense.setText(dateString)

        //Call Function that will display type expense data in spinner
        populateSpinnerTypeExpense()

        //Edit Text Changed Listerner
        EditTextChangedListener()

        //When user
        txtAddExpenseFresh.setOnClickListener(View.OnClickListener {
            buttomSheetModalAddFresh()
        })

        btnaddExpense.setOnClickListener(View.OnClickListener {
            if (validate(edtDesignationExpense) && validate(edtQuantityExpense) && validate(edtUnitPriceExpense) ){
                //&& expenseFreshAdapter.freshExpenseList.isNotEmpty()
                createExpense()
            }

        })

    }

    private fun initialisation(){
        edtDesignationExpense = findViewById(R.id.libelle_add_expense)
        edtQuantityExpense = findViewById(R.id.quantite_add_expense)
        edtUnitPriceExpense = findViewById(R.id.pu_add_expense)
        edtdateExpense = findViewById(R.id.date_add_expense)
        btnaddExpense = findViewById(R.id.btn_add_expense)
        spinTypeExpense = findViewById(R.id.spinner_expense)
        txtAddExpenseFresh = findViewById(R.id.txt_add_expense_fresh)
        txtTotalAmountExpense = findViewById(R.id.total_amount_expense)
    }

    private fun populateSpinnerTypeExpense(){
        // Create an ArrayAdapter using a simple spinner layout and languages array
        /*val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, expensesTypes)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinTypeExpense.adapter = aa
        selecteTypeExpense = expensesTypes[0]
        //Toast.makeText(this,""+selecteTypeExpense,Toast.LENGTH_SHORT).show()
        spinTypeExpense.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                (parent.getChildAt(0) as TextView).setTextColor(
                    getResources().getColor(R.color.myspinner_text_color)
                )
                (parent.getChildAt(0) as TextView).setTextSize(20F)
                selecteTypeExpense = expensesTypes[position]
                //Toast.makeText(this@AddExpenseActivity,""+selecteTypeExpense,Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

        }*/

        val typeExpense = TypeExpense()
        typeExpense.design_type_expense = "Hangard"
        //typeExpense.design_type_expense = "Fonctionnement"
        typeListExpense.add(typeExpense)

        val adapter = ExpenseTypeAdapter(this, typeListExpense)

        spinTypeExpense.adapter = adapter

        spinTypeExpense.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                /*Toast.makeText(this@AddExpenseActivity, "" + (parent?.getItemAtPosition(pos) as TypeExpense).design_type_expense,
                    Toast.LENGTH_SHORT).show()*/
                selecteTypeExpense = typeListExpense[pos].design_type_expense!!
                //Toast.makeText(this@AddExpenseActivity, selecteTypeExpense, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        typeListExpense.add(TypeExpense(design_type_expense = "Fonctionnement"))
        typeListExpense.add(TypeExpense(design_type_expense = "Reparation & Maintenance"))
        adapter.notifyDataSetChanged()

    }

    private fun validate(editText: EditText): Boolean {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.text.isNotBlank() && editText.text.isNotEmpty()) {
            return true // returns true if field is not empty
        }
        editText.error = "Please Fill This"
        editText.requestFocus()
        return false
    }

    private fun convertEditTextToDouble(editText: EditText):Double{
        return editText.text.trim().toString().toDouble()
    }

    fun createExpense(){
       val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show() // show progress dialog
        val expense = Expense()
        expense.expense_designation = edtDesignationExpense.text.toString()
        expense.expense_quantity = edtQuantityExpense.text.toString().toInt()
        expense.expense_unit_price = edtUnitPriceExpense.text.toString().toDouble()
        expense.expense_type = selecteTypeExpense
        expense.expense_date = dateString
        expense.freshExpense = expenseFreshAdapter.freshExpenseList

        // Api is a class in which we define a method getClient() that returns the API Interface class object
        // registration is a POST request type method in which we are sending our field's data
        // enqueue is used for callback response and error

        val service = NetWorkClient.getClient(Constants.BASE_URL).create<ExpenseService>()

        val call:Call<Expense> = service.createExpense(expense)
        call.enqueue(object :Callback<Expense>{
            override fun onResponse(call: Call<Expense>, response: Response<Expense>) {
                if (response.isSuccessful){
                    Toast.makeText(this@AddExpenseActivity,"Inserted",Toast.LENGTH_SHORT).show()
                    //Reset values
                    edtUnitPriceExpense.setText(""); edtDesignationExpense.setText(""); edtQuantityExpense.setText("")
                    expenseFreshAdapter.freshExpenseList.clear(); txtAddExpenseFresh.text = "Ajout Frais"

                    progressDialog.hide()
                }
                else{
                    progressDialog.hide()
                    Toast.makeText(this@AddExpenseActivity,"500",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Expense>, t: Throwable) {
                progressDialog.hide()
                Toast.makeText(this@AddExpenseActivity,"failed"+t,Toast.LENGTH_LONG).show()
            }

        })

    }

    fun buttomSheetModalAddFresh(){
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_add_fresh_expense, null)
        // on below line we are creating a variable for our button which we are using to dismiss our dialog.
        val btnClose = view.findViewById<ImageButton>(R.id.btn_close_addFreshEnter)

        //Initialisation
        edtDesignationFresh = view.findViewById<EditText>(R.id.edt_designation_freshExpense)
        edtAmountFresh = view.findViewById<EditText>(R.id.edt_amountFreshExpense)
        val btnAddFreshOnList = view.findViewById<Button>(R.id.btn_addFreshExpense)
        val btnUpdateFreshOnList = view.findViewById<Button>(R.id.btn_updateFreshExpense)
        val recycleViewFresh = view.findViewById<RecyclerView>(R.id.rcv_freshExpense)

        //expenseFreshAdapter = ExpenseFreshAdapter(this@AddExpenseActivity,mOnExpenseItemClickListener)
        recycleViewFresh.layoutManager = LinearLayoutManager(this)
        recycleViewFresh.adapter = expenseFreshAdapter

        btnAddFreshOnList.setOnClickListener(View.OnClickListener {

            if (validate(edtDesignationFresh) && validate(edtAmountFresh)){
                // prepare id on incremental basis
                val id = expenseFreshAdapter.getNextItemId().toLong()
                val designation = edtDesignationFresh.text.toString()
                val amountFresh = edtAmountFresh.text.toString().toDouble()

                // prepare model for use
                val model = FreshExpense(null, id,designation, amountFresh)

                // add model to the adapter
                expenseFreshAdapter.addFreshExpense(model)

                // reset the input
                edtDesignationFresh.setText("")
                edtAmountFresh.setText("")

                //Display fresh to textView fresh
                if (expenseFreshAdapter.freshExpenseList.isNotEmpty())
                    for (i in expenseFreshAdapter.freshExpenseList){
                        txtAddExpenseFresh.text = i.fresh_expense_designation+","+i.fresh_expense_amount
                        //Toast.makeText(this, i.fresh_expense_designation, Toast.LENGTH_SHORT).show()
                         //totalFreshAmount+= i.fresh_expense_amount!!.toDouble()
                    }
                //Set Total Amount
                calculateTotalExpense()
            }
        })

        btnUpdateFreshOnList.setOnClickListener(View.OnClickListener {

            // we have nothing to update
            if (modelToBeUpdated.isEmpty()) return@OnClickListener

            val designatin = edtDesignationFresh.text.toString()
            val amountprice = edtAmountFresh.text.toString().toDouble()

            if (validate(edtDesignationFresh) && validate(edtAmountFresh)) {
                val model = modelToBeUpdated.pop()
                model.fresh_expense_designation = designatin
                model.fresh_expense_amount = amountprice
                expenseFreshAdapter.updateFreshExpense(model)

                // reset the input
                edtDesignationFresh.setText("")
                edtAmountFresh.setText("")
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

    fun EditTextChangedListener(){

        edtUnitPriceExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTotalExpense()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edtQuantityExpense.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateTotalExpense()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    fun calculateTotalExpense(){
        //Get Total freshAnount from list
        var totalFreshAmount = 0.0
        if (expenseFreshAdapter.freshExpenseList.isNotEmpty())
            totalFreshAmount = expenseFreshAdapter.freshExpenseList.sumOf { it.fresh_expense_amount!! }

        var result = 0.0
        if (edtUnitPriceExpense.text.isNotEmpty() && edtUnitPriceExpense.text.isNotBlank() && validate(edtQuantityExpense)){
            result = convertEditTextToDouble(edtUnitPriceExpense).times(convertEditTextToDouble(edtQuantityExpense))

            totalAmountExpense = result.plus(totalFreshAmount)
            //txtTotalAmountExpense.text = String.format(Locale.getDefault(), "%,d", totalAmountExpense)
        }
        else
            totalAmountExpense = totalFreshAmount

        txtTotalAmountExpense.text = totalAmountExpense.toString()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_down, R.anim.slide_in_right)
        return true
    }
}