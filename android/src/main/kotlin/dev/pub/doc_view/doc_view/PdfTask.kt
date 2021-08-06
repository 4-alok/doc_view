package dev.pub.doc_view.doc_view

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.IOException
import com.tom_roush.pdfbox.rendering.PDFRenderer
import com.tom_roush.pdfbox.text.PDFTextStripper
import io.flutter.plugin.common.MethodChannel
import java.io.FileOutputStream
import java.io.OutputStream

class PdfTask {
    companion object{
        @JvmStatic
        fun pageCount(renderFile:File, result: MethodChannel.Result) {
            try {
                val document: PDDocument? = PDDocument.load(renderFile)
                val pageCount: Int = document?.numberOfPages ?: -1
                document?.close()
                Handler(Looper.getMainLooper()).post {
                    result.success(pageCount)
                }
            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    result.success(-1)
                }
            }
        }

        @JvmStatic
        fun getPageImage(index: Int, renderFile:File, result: MethodChannel.Result) {
            try {
                val document: PDDocument? = PDDocument.load(renderFile)
                val pdfRenderer = PDFRenderer(document)
                val image:Bitmap = pdfRenderer.renderImageWithDPI(index, 100F, Bitmap.Config.RGB_565)
                val imgPath = renderFile.path + ".png"
                val stream: OutputStream = FileOutputStream(imgPath)
                image.compress(Bitmap.CompressFormat.PNG,100,stream)
                stream.flush()
                stream.close()
                document?.close()
                Handler(Looper.getMainLooper()).post {
                    result.success(index)
                }
            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    result.success(index)
                }
            }
        }

        @JvmStatic
        fun fetchText(index: Int, renderFile:File, result: MethodChannel.Result) {
            try {
                val doc: PDDocument? = PDDocument.load(renderFile)
                val stripper = PDFTextStripper();
                stripper.startPage = index
                stripper.endPage = index
                val text:String = stripper.getText(doc) ?: ""
                doc?.close()
                Handler(Looper.getMainLooper()).post {
                    result.success(text)
                }
            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    result.success("")
                }
            }
        }
    }
}