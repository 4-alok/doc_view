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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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

                if ( pageCount < 50 ) {
                    PdfTask.generatePdfThumbs(pdfRenderer, 0, pageCount, dir.path)
                } else {

                    val threadCount:Int = Runtime.getRuntime().availableProcessors()/2
                    val pool: ExecutorService = Executors.newFixedThreadPool(threadCount)

                    val quotient:Int = pageCount % threadCount
                    var pageIndex:Int = pageCount % threadCount
                    var startIndex = 0

                    for (i in 1 until threadCount){
                        Log.d(startIndex.toString(), pageIndex.toString())
//                        pool.execute(GenThumbs(pdfRenderer, startIndex, pageIndex, dir.path))
                        Log.d("--------------> Range", "$startIndex  -  $pageIndex")
                        startIndex = (pageIndex++)
                        pageIndex += quotient
                    }
//                    GenThumbs(pdfRenderer, startIndex, pageCount, dir.path)
                    Log.d("--------------> Range", "$startIndex  -  $pageIndex")
                    try {
                        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (e:InterruptedException) {
                        Log.d("Execute error", e.toString())
                    } finally {
                        doc.close()
                    }
                }

                Handler(Looper.getMainLooper()).post {
                    result.success("Done")
                }
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
