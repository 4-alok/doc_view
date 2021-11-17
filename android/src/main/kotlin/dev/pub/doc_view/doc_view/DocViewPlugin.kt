package dev.pub.doc_view.doc_view

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File

const val PAGE_COUNT:String = "pageCount"
const val GEN_THUMBS:String = "genThumbs"
const val GET_IMAGE:String = "getImage"
const val FETCH_TEXT:String = "fetchText"

/** DocViewPlugin */
class DocViewPlugin: FlutterPlugin, MethodCallHandler {
    private var file:File? = null
    private var document: PDDocument? = null
    private var fileType: String = ""
    private lateinit var channel : MethodChannel
    private lateinit var context :  android.content.Context

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "doc_view")
    channel.setMethodCallHandler(this)
      context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
        GET_IMAGE -> {
            initFile(call.argument<String>("path")!!)
            singleThreadTask(call, GET_IMAGE, result)
        }
        PAGE_COUNT -> {
            initFile(call.argument<String>("path")!!)
            singleThreadTask(call, PAGE_COUNT, result)
        }
        GEN_THUMBS -> {
            initFile(call.argument<String>("path")!!)
            val thread = Thread(
                GenerateThumbnails(file!!, result, context, document!!)
            )
            thread.start()
        }
        FETCH_TEXT -> {
            initFile(call.argument<String>("path")!!)
            singleThreadTask(call, FETCH_TEXT, result)
        }
        else -> {
          result.notImplemented()
        }
    }
  }

    private fun initFile(path: String) {
        if(file == null || fileType != path.split(".").last()){
            file = File(path)
            fileType = path.split(".").last()
            if (fileType == "pdf"){
                PDFBoxResourceLoader.init(context);
                document = PDDocument.load(file)
            }

        }
    }

    private fun singleThreadTask(@NonNull call: MethodCall, task: String, result: Result){
        val thread = Thread(
            SingleThreadTask(call, task, result, context, file!!, document)
        )
        thread.start()
    }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    file = null
    document = null
    channel.setMethodCallHandler(null)
  }
}

class SingleThreadTask(
    private val call: MethodCall,
    private val task: String,
    private val result: Result,
    private val context:  android.content.Context,
    private val file:File,
    private val document: PDDocument?
    ): Runnable {
    override fun run() {
        val path:String = call.argument<String>("path")!!
        when (val fileType: String = path.split(".").last()) {
            "pdf" -> {
                when (task) {
                    PAGE_COUNT -> {
                        val pageCount:Int = PdfTask.pageCount(document!!)
                        Handler(Looper.getMainLooper()).post {
                            result.success(pageCount)
                        }
                    }
                    GET_IMAGE -> {
                        PdfTask.getPageImage(
                            call.argument<Int>("index")!!,
                            document!!,
                            result
                        )
                    }
                    FETCH_TEXT -> {
                        PdfTask.fetchText(
                            call.argument<Int>("index")!!,
                            file,
                            result
                        )
                    }
                }
            }
            "docx" -> {
                android.util.Log.d("Android", "$fileType Not implementer")
            }
            "ppt" -> {
                android.util.Log.d("Android", "$fileType Not implementer")
            }
        }
    }
}
