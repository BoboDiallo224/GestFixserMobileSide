package com.example.fixsermobileapp.exit_sale

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fixsermobileapp.R
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.*
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.ByteArrayOutputStream

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class ExitSaleActivity : AppCompatActivity() {
    // on below line we are creating
    // a variable for our image view.
    lateinit var generatePDFBtn: Button

    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pageWidth = 792

    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    // on below line we are creating a
    // constant code for runtime permissions.
    var PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit_sale)

        // on below line we are initializing our button with its id.
        generatePDFBtn = findViewById(R.id.idBtnGeneratePdf)

        // on below line we are initializing our bitmap and scaled bitmap.
        bmp = BitmapFactory.decodeResource(resources, R.drawable.logo)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        // on below line we are checking permission
        if (checkPermissions()) {
            // if permission is granted we are displaying a toast message.
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission()
        }

        // on below line we are adding on click listener for our generate button.
        generatePDFBtn.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                // on below line we are calling generate
                // PDF method to generate our PDF file.
                //generatePDF()
                crateInvoicePdf()
            }
        }

    }

    // on below line we are creating a generate PDF method
    // which is use to generate our PDF file.
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun generatePDF() {
        println("13"+5+1)
        // creating an object variable
        // for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, R.color.purple_200)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.purple_200))
        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)
        //GraphicsContext
        //canvas.drawRect(5F, canvas.getHeight()/2F, 30F, 30F, paint)
        canvas.drawLine(0F,0F,500F, 500F,paint)
        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GFG.pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }

    @Throws(FileNotFoundException::class)
    fun crateInvoicePdf(){
       val path:String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(path,"myInvoice.pdf")
        val outputStream = FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)

        /* bmp = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100,stream)
        val bitmapData = stream.toByteArray()
        val imageData:ImageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)*/


        //Table2
        val columnWith2 = floatArrayOf(62F,162F,112F,112F,112F)
        val table2 = Table(columnWith2)

        //Table2......01
        table2.addCell(Cell().add(Paragraph("No")).setBold())
        table2.addCell(Cell().add(Paragraph("Designation")).setBold())
        table2.addCell(Cell().add(Paragraph("Rate")))
        table2.addCell(Cell().add(Paragraph("Quantity")).setBold())
        table2.addCell(Cell().add(Paragraph("Price")).setBold())

        //Table2......02
        table2.addCell(Cell().add(Paragraph("1")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))

        //Table2......03
        table2.addCell(Cell().add(Paragraph("1")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))

        //Table2......04
        table2.addCell(Cell().add(Paragraph("1")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("")))

        //Table2......05
        table2.addCell(Cell().add(Paragraph("1")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("TEST")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("")))

        //Table2......06
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("Sub-Total")))
        table2.addCell(Cell().add(Paragraph("1100")))

        //Table2......07
        table2.addCell(Cell().add(Paragraph("Terms & Conditions")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("Gst (12%)")))
        table2.addCell(Cell().add(Paragraph("132")))

        //Table2......08
        table2.addCell(Cell(1,2).add(Paragraph("Goods are not returnable or exchangeable")))
        //table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("")))
        table2.addCell(Cell().add(Paragraph("Grand Total")))
        table2.addCell(Cell().add(Paragraph("125547")))

        bmp = BitmapFactory.decodeResource(resources, R.drawable.componie_name)
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100,stream)
        val bitmapData = stream.toByteArray()
        val imageData:ImageData = ImageDataFactory.create(bitmapData)
        val imageHoderComponieName = Image(imageData)
        imageHoderComponieName.setHeight(120F)
        imageHoderComponieName.setWidth(300F)
        imageHoderComponieName.setTextAlignment(TextAlignment.JUSTIFIED_ALL)

        val dr = getDrawable(R.drawable.logo)
        val bitmap = (dr as BitmapDrawable?)!!.bitmap
        val stream2 = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream2)
        val bitmapData2 = stream2.toByteArray()
        val imageData2:ImageData = ImageDataFactory.create(bitmapData2)
        val image2 = Image(imageData2)
        image2.setHeight(120F)

        val columnWithTbleFooter = floatArrayOf(50F,650F)
        val tableFooter = Table(columnWithTbleFooter)

        tableFooter.addCell(Cell(3,1).add(image2))
        tableFooter.addCell(Cell(3,1).add(imageHoderComponieName))

        val columnWithTableInfoClient = floatArrayOf(250F)
        val tableInfoClient = Table(columnWithTableInfoClient)

        val textClient = Text("Infs Client:\n Portos Foullaylou\n Conakry Rep de Guinne").setBold()
        val paragraphInfoClient = Paragraph()
        paragraphInfoClient.add(textClient)
        tableInfoClient.addCell(Cell().add(paragraphInfoClient)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
        //
        val textInvoiceNumber = Text("\nFacture N° 002").setBold()
        val paragraphInvoiceNumber = Paragraph()
        paragraphInvoiceNumber.add(textInvoiceNumber)
        //
        val textDateInvoice = Text("Date Demission 14/07/2020").setBold().setTextAlignment(TextAlignment.CENTER)
        val paragraphDateInvoice = Paragraph()
        paragraphDateInvoice.add(textDateInvoice)

        //document.add(image)
        document.add(tableFooter)
        document.add(paragraphInvoiceNumber)
        document.add(paragraphDateInvoice)
        document.add(tableInfoClient)
        document.add(Paragraph("\n"))
        document.add(table2)
        document.add(Paragraph("\n\n\n\n\n\n(Authorized Signatory)\n\n\n").setTextAlignment(TextAlignment.RIGHT))

        document.close()
        Toast.makeText(this,"Pdf Created",Toast.LENGTH_SHORT).show()

    }

    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        // on below line we are returning true if both the
        // permissions are granted anf returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    // on below line we are creating a function to request permission.
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }

    // on below line we are calling
    // on request permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    // if permissions are granted we are displaying a toast message.
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}