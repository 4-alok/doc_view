package dev.pub.doc_view.doc_view

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import io.flutter.plugin.common.MethodChannel
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import io.flutter.plugin.common.MethodCall
import java.io.File

class GenerateThumbnails(
    @NonNull private val file: File,
    @NonNull private val result: MethodChannel.Result,
    @NonNull call: MethodCall,
    @NonNull private val context:  android.content.Context
    ): Runnable {

    override fun run() {
        when (val fileType: String = file.path.split(".").last()) {
            "pdf" -> {
                PDFBoxResourceLoader.init(context);
                val doc: PDDocument = PDDocument.load(file)
                val pageCount:Int = PdfTask.pageCount(file)
                val pdfRenderer = PDFRenderer(doc)
                val cachePath:String = context.cacheDir.path
                val code:String = file.path.hashCode().toString()
                val dir = File("/storage/emulated/0/test/$code")
                if(!dir.exists()){
                    dir.mkdir()
                }

                PdfTask.generatePdfThumbs(pdfRenderer, 0, pageCount, dir.path)
                Handler(Looper.getMainLooper()).post {
                    result.success(dir.path)
                }
                doc.close()

            }
            "docx" -> {
                Log.d("Android", fileType)
            }
            "ppt" -> {
                Log.d("Android", fileType)
            }
        }
    }
}
