package dev.pub.doc_view.doc_view

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.tom_roush.pdfbox.pdmodel.PDDocument.load as load1

class GenerateThumbnails(path: String, result: MethodChannel.Result, context:  android.content.Context, channel : MethodChannel) {
    init {
        when (val fileType: String = path.split(".").last()) {
            "pdf" -> {
                PDFBoxResourceLoader.init(context);
                generatePdfThumbnail(path)
            }
            "docx" -> {
                test(path, result)
                Log.d("Android", fileType)
            }
            "ppt" -> {
                Log.d("Android", fileType)
            }
        }
    }

    private fun test(path: String, result: MethodChannel.Result) {
        val service: ExecutorService = Executors.newSingleThreadExecutor()
        service.execute(
            MyRunnable(path, result)
        )
    }

    private fun generatePdfThumbnail(path: String) {
        return try {
            val renderFile: File = File(path)
            val document: PDDocument? = load1(renderFile)
            val pageCount: Int = document?.numberOfPages?: -1
        } catch (e : IOException) {
        }
    }
}

class MyRunnable(private val path: String, private val result: MethodChannel.Result): Runnable {
    override fun run() {
        Thread.sleep(2000)
        Log.d("Thread", path)
        sendSuccess("Hi From Executor Thread")
    }

    private fun sendSuccess(msg: String) {
        val res: MethodChannel.Result = this.result
        Handler(Looper.getMainLooper()).post { res.success(msg) }
    }
}