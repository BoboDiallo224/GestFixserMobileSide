package com.example.fixsermobileapp.enter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixsermobileapp.R
import com.example.fixsermobileapp.enter.adapter.ProductListEnterAdapter
import com.example.fixsermobileapp.enter.model.ProductAddEnterModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class AddEnterActivity : AppCompatActivity() {

    /**
     * To hold the reference to the items to be updated as a stack.
     * We can just remove and get the item with [Stack] in one shot.
     */
    private var modelToBeUpdated: Stack<ProductAddEnterModel> = Stack()

    /**
     * The listener which we have defined in [OnProductClickListener]. Will be added to the adapter
     * which constructing the adapter
     */
    private val mOnProductClickListener = object : OnProductAddEnterClickListener {
        override fun onUpdate(position: Int, model: ProductAddEnterModel) {

            // store this model that we want to update
            // we will .pop() it when we want to update
            // the item in the adapter
            modelToBeUpdated.add(model)

            // set the value of the clicked item in the edit text
            designListProdEnter.setText(model.designationListProdEnter)
            quantityListProdEnter.setText(model.qteListProdEnter!!)
            priUnitListProdEnter.setText(model.puListProdEnter.toString())
            montTotalListProdEnter.text = model.montTotListProdEnter.toString()
        }
        override fun onDelete(model: ProductAddEnterModel) {

            // just remove the item from list
            productListEnterAdapter.removeProduct(model)
        }
    }

    lateinit var btnAddListProdEnter:ImageView

    //Form
    private lateinit var designListProdEnter:EditText
    private lateinit var quantityListProdEnter:EditText
    private lateinit var priUnitListProdEnter:EditText
    private lateinit var montTotalListProdEnter:TextView
    private lateinit var txtTotalAmountEnter:TextView
    private lateinit var btnAddItemToListProdEnter:Button
    private lateinit var edtPayAmountEnter:EditText
    private lateinit var txvRestAmountEnter:TextView

    private lateinit var productListEnterAdapter:ProductListEnterAdapter
    lateinit var recycleViewItemProdAddEnter:RecyclerView

    //Variables
    private var totalAmountEnter = 0
    private var totalAmountListItem:Long = 0
    private var restAmountEnter:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_enter)

        recycleViewItemProdAddEnter = findViewById(R.id.rcv_item_prod_enter)
        recycleViewItemProdAddEnter.layoutManager = LinearLayoutManager(this)
        //recycleViewItemProdAddEnter.setHasFixedSize(true)

        productListEnterAdapter = ProductListEnterAdapter(this, mOnProductClickListener)
        recycleViewItemProdAddEnter.adapter = productListEnterAdapter

        //TextView and EditText
        edtPayAmountEnter = findViewById(R.id.edt_mnt_pay_add_enter)
        txvRestAmountEnter = findViewById(R.id.txv_rest_enter)
        //Total Amount Initialisation
        txtTotalAmountEnter = findViewById(R.id.txv_total_enter)
        txtTotalAmountEnter.text = productListEnterAdapter.mProductList.sumOf { it.montTotListProdEnter!!.toInt() }.toString()

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_add_enter)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.title = "Nouvelle Entrer"
        actionBar.setDisplayHomeAsUpEnabled(true)

        //Modal Sheet Bottom open event
        btnAddListProdEnter = findViewById(R.id.btn_addlistprod_enter)
        btnAddListProdEnter.setOnClickListener(View.OnClickListener {
            buttomSheetModalAddEnterProduct()
        })
        //
        onEdtPayAmountTextChangedlistener()
    }

    /*
    updateProduct.setOnClickListener {

    // we have nothing to update
    if (modelToBeUpdated.isEmpty()) return@setOnClickListener

    val name = productName.text.toString()
    val price = productUnit.text.toString()

    if (!name.isBlank() && !price.isBlank()) {
        val model = modelToBeUpdated.pop()
        model.name = name
        model.price = price
        mProductListAdapter.updateProduct(model)

        // reset the input
        productName.setText("")
        productUnit.setText("")
    }
}
    * */

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_confirm_enter){
            calculateTotalAndRestAmountEnter()
            //Toast.makeText(this,productListEnterAdapter.mProductList[0].qteListProdEnter.toString(), Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun buttomSheetModalAddEnterProduct(){

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_addlistprod_enter,null)
        //dialog.behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
        //dialog.behavior.isFitToContents
        val btnClose = view.findViewById<ImageButton>(R.id.btn_close_listprod_enter)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_item_prod_botmsheet_enter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productListEnterAdapter


        btnAddItemToListProdEnter = view.findViewById(R.id.btn_itemprod_enter)
        designListProdEnter = view.findViewById(R.id.edt_designation_prod_enter)
        quantityListProdEnter = view.findViewById(R.id.edt_qte_prod_enter)
        priUnitListProdEnter = view.findViewById(R.id.edt_pu_prod_enter)
        montTotalListProdEnter = view.findViewById(R.id.txt_montanttot_prod_enter)

        btnClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        //Add Item Product Enter to List
        btnAddItemToListProdEnter.setOnClickListener {
            addItemProduct()
            //
            totalAmountEnter = productListEnterAdapter.mProductList.sumOf { it.montTotListProdEnter!!.toInt() }
            //
            calculateTotalAndRestAmountEnter()
            /*txtTotalAmountEnter.text = String.format(Locale.getDefault(), "%,d", totalAmountEnter)
            txvRestAmountEnter.text = String.format(Locale.getDefault(), "%,d", restAmountEnter)*/
        }

        priUnitListProdEnter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Calculate Montant Total
                calculateTotalItemProd()

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        quantityListProdEnter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Calculate Montant Total
                calculateTotalItemProd()

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        dialog.setCancelable(true)
        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        //dialog.supportActionBar
        //dialog.window!!.setWindowAnimations(R.anim.slide_down!!)
        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    fun addItemProduct(){

        if (designListProdEnter.text.toString().trim().isNotBlank() &&
            quantityListProdEnter.text.toString().trim().isNotBlank() &&
            priUnitListProdEnter.text.toString().trim().isNotBlank() &&
            montTotalListProdEnter.text.toString().trim().isNotBlank()) {

            val designation = designListProdEnter.text.toString()
            val quantity = quantityListProdEnter.text.toString().toInt()
            val unitPrice = priUnitListProdEnter.text.toString().toLong()
            val montanTotal = totalAmountListItem

            // prepare id on incremental basis
            val id = productListEnterAdapter.getNextItemId().toLong()

            // prepare model for use
            val model = ProductAddEnterModel(id, designation, quantity,unitPrice, montanTotal)

            // add model to the adapter
            productListEnterAdapter.addProduct(model)

            // reset the input
            designListProdEnter.setText("")
            quantityListProdEnter.setText("")
            priUnitListProdEnter.setText("")
            montTotalListProdEnter.setText("")
        }
    }

    fun updateItemProduct(designation:String, qte:String, priUnit:String, montTotal:String){
        designListProdEnter.setText(designation)
        quantityListProdEnter.setText(qte)
        priUnitListProdEnter.setText(priUnit)
        montTotalListProdEnter.text = montTotal
    }

    fun calculateTotalItemProd(){
        if (quantityListProdEnter.text.toString().trim().isNotBlank() && quantityListProdEnter.text.toString().trim().isNotEmpty() &&
            priUnitListProdEnter.text.toString().trim().isNotBlank() && priUnitListProdEnter.text.toString().trim().isNotEmpty()){

            totalAmountListItem = quantityListProdEnter.text.toString().toInt() * priUnitListProdEnter.text.toString().toLong()
            //Format Total Amount List Item Product
            montTotalListProdEnter.text = String.format(Locale.getDefault(), "%,d", totalAmountListItem)
        }
        else
            Toast.makeText(this@AddEnterActivity, "Champ quantite ou prix untaire Vide", Toast.LENGTH_SHORT).show()
    }

    fun calculateRestAmountEnterOnEdtPayAmount(){
        totalAmountEnter = productListEnterAdapter.mProductList.sumOf { it.montTotListProdEnter!!.toInt() }
        if (edtPayAmountEnter.text.toString().trim().isNotEmpty() && edtPayAmountEnter.text.toString().trim().isNotBlank() &&
            totalAmountEnter >= edtPayAmountEnter.text.toString().trim().toLong() ){

                restAmountEnter = totalAmountEnter.minus(edtPayAmountEnter.text.toString().trim().toLong())
                //Set Text
                txvRestAmountEnter.text = String.format(Locale.getDefault(), "%,d", restAmountEnter)
        }
        else if (edtPayAmountEnter.text.toString().trim().isEmpty() && edtPayAmountEnter.text.toString().trim().isBlank()){

            restAmountEnter = 0
            //Set Text
            txvRestAmountEnter.text = String.format(Locale.getDefault(), "%,d", restAmountEnter)
        }
        else
            Toast.makeText(this, "Le Montant payer ne doit pas être supperireur au Montant Total", Toast.LENGTH_SHORT).show()
    }

    fun calculateTotalAndRestAmountEnter(){
        totalAmountEnter = productListEnterAdapter.mProductList.sumOf { it.montTotListProdEnter!!.toInt() }

        if (edtPayAmountEnter.text.toString().trim().isNotEmpty() && edtPayAmountEnter.text.toString().trim().isNotBlank() &&
            totalAmountEnter >= edtPayAmountEnter.text.toString().trim().toLong() ){
            restAmountEnter = totalAmountEnter.minus(edtPayAmountEnter.text.toString().trim().toLong())
            //Toast.makeText(this, restAmountEnter.toString(), Toast.LENGTH_LONG).show()
        }

        //Set Text
        txtTotalAmountEnter.text = String.format(Locale.getDefault(), "%,d", totalAmountEnter)
        txvRestAmountEnter.text = String.format(Locale.getDefault(), "%,d", restAmountEnter)
        /*if (productListEnterAdapter.mProductList.isNotEmpty()){
            *//*for (i in productListEnterAdapter.mProductList.indices){

                totalQuantityEnter = productListEnterAdapter.mProductList[i].qteListProdEnter!!.plus()
                Toast.makeText(this, totalQuantityEnter.toString(), Toast.LENGTH_LONG).show()
                //old = 0
            }*//*
            totalAmountEnter = productListEnterAdapter.mProductList.sumOf { it.montTotListProdEnter!!.toInt() }

        }*/
    }

    fun onEdtPayAmountTextChangedlistener(){
        edtPayAmountEnter.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                calculateRestAmountEnterOnEdtPayAmount()
                //calculateTotalAndRestAmountEnter()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

}

