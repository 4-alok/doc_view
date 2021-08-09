package dev.pub.doc_view.doc_view

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import com.tom_roush.pdfbox.text.PDFTextStripper
import io.flutter.plugin.common.MethodChannel
import java.io.*


class PdfTask {
    companion object {

        @JvmStatic
        fun pageCount(document: PDDocument): Int {
            return try {
                val pageCount: Int = document.numberOfPages ?: -1
//                document.close()
                pageCount
            } catch (e: IOException) {
                -1
            }
        }

        @JvmStatic
        fun getPageImage(index: Int, document: PDDocument, result: MethodChannel.Result) {
            try {
                val pdfRenderer = PDFRenderer(document)
                val image: Bitmap = pdfRenderer.renderImageWithDPI(index, 100F)
                val stream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageData = stream.toByteArray()

                Handler(Looper.getMainLooper()).post {
                    result.success(imageData)
                }
                stream.close()
//                document.close()

            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    result.error("", "PdfTask getPageImage", "Error at index $index")
                }
            }
        }

        @JvmStatic
        fun fetchText(index: Int, renderFile: File, result: MethodChannel.Result) {
            try {
                val doc: PDDocument? = PDDocument.load(renderFile)
                val stripper = PDFTextStripper();
                stripper.startPage = index
                stripper.endPage = index
                val text: String = stripper.getText(doc) ?: ""
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

        @JvmStatic
        fun generatePdfThumbs(pdfRenderer: PDFRenderer, start: Int, end: Int, path: String) {
            for (i in start..end) {
                try {
                    val image: Bitmap = pdfRenderer.renderImageWithDPI(i, 20F)
                    val imgPath = "$path/$i.png"
                    if (!File(imgPath).exists()) {
                        val stream: OutputStream = FileOutputStream(imgPath)
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.flush()
                        stream.close()
                    }
                } catch (e: IOException){
                    android.util.Log.d("Error GP", e.toString())
                }
            }
        }
    }
}

class GenThumbs(
    private val pdfRenderer: PDFRenderer,
    private val start: Int,
    private val  end: Int,
    private val path: String
    ) : Runnable {
    override fun run() {
        PdfTask.generatePdfThumbs(pdfRenderer, start, end, path)
    }
}
