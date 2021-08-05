package dev.pub.doc_view.doc_view

import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.IOException
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.FileOutputStream
import java.io.OutputStream

class PdfTask {
    companion object{
        fun pageCount(path: String): Int {
            return try {
                val renderFile = File(path)
                val document: PDDocument? = PDDocument.load(renderFile)
                val pageCount: Int = document?.numberOfPages ?: -1
                document?.close()
                pageCount
            } catch (e: IOException) {
                -1
            }
        }

        fun getPageImage(path: String, index: Int, context:  android.content.Context) {
            try {
                val renderFile = File(path)
                val document: PDDocument? = PDDocument.load(renderFile)
                val pdfRenderer = PDFRenderer(document)
                val image:Bitmap = pdfRenderer.renderImageWithDPI(index, 100F, Bitmap.Config.RGB_565)
                val imgPath = "$path.png"
                val stream: OutputStream = FileOutputStream(imgPath)
                val metrics = DisplayMetrics()
                image.compress(Bitmap.CompressFormat.PNG,100,stream)
                stream.flush()
                stream.close()
                document?.close()
            } catch (e: IOException) {}
        }
    }
}