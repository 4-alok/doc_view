package dev.pub.doc_view.doc_view

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import io.flutter.plugin.common.MethodChannel
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import io.flutter.plugin.common.MethodCall
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GenerateThumbnails(
    @NonNull private val file: File,
    @NonNull private val result: MethodChannel.Result,
    @NonNull private val context:  android.content.Context
    ): Runnable {

    override fun run() {
        when (val fileType: String = file.path.split(".").last()) {
            "pdf" -> {
                PDFBoxResourceLoader.init(context);
                val doc: PDDocument = PDDocument.load(file)
                val pageCount:Int = PdfTask.pageCount(file)
                val pdfRenderer = PDFRenderer(doc)
//                val cachePath:String = context.cacheDir.path
                val code:String = file.path.hashCode().toString()
                val dir = File("/storage/emulated/0/test/$code")

                createDir(dir)

                if ( pageCount < 50 ) {
                    PdfTask.generatePdfThumbs(pdfRenderer, 0, pageCount, dir.path)
                } else {
                    val threadCount:Int = Runtime.getRuntime().availableProcessors()/2

                    val pool: ExecutorService = Executors.newFixedThreadPool(threadCount/2)

                    val quotient:Int = pageCount / threadCount
                    var start = 0
                    var end = quotient
                    val remaining = pageCount - (pageCount%threadCount)

                    for (i in 1 until threadCount){
                        pool.execute(GenThumbs(pdfRenderer, start, end, dir.path))
                        start = end + 1
                        end += end
                    }
                    if(remaining < pageCount) {
                        PdfTask.generatePdfThumbs(pdfRenderer, remaining+1, pageCount - 1, dir.path)
                    }
                    pool.shutdown()
                    try {
                        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        doc.close()
                    } catch (e:InterruptedException) {
                        android.util.Log.d("Execute error", e.toString())
                    }
                    taskDone(result)
                }

            }
            "docx" -> {
                android.util.Log.d("Android", fileType)
            }
            "ppt" -> {
                android.util.Log.d("Android", fileType)
            }
        }
    }

    private fun createDir(dir:File){
        if(!dir.exists()){
            dir.mkdir()
        }
    }

    private fun taskDone(result: MethodChannel.Result) {
        Handler(Looper.getMainLooper()).post {
            android.util.Log.d("Task", "------------Done")
            result.success("Done")
        }
    }
}
