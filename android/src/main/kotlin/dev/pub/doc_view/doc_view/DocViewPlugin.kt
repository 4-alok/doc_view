package dev.pub.doc_view.doc_view

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
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
    private var renderFile:File? = null
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
            result.notImplemented()
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
        if(renderFile == null || fileType != path.split(".").last()){
            renderFile = File(path)
            fileType = path.split(".").last()
            android.util.Log.d("Android", "Loaded file")
        }
    }

    private fun singleThreadTask(@NonNull call: MethodCall, task: String, result: Result){
        val thread = Thread(
            SingleThreadTask(call, task, result, context, renderFile!!)
        )
        thread.start()
    }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

class SingleThreadTask(
    private val call: MethodCall,
    private val task: String,
    private val result: Result,
    private val context:  android.content.Context,
    private val file:File
    ): Runnable {
    override fun run() {
        val path:String = call.argument<String>("path")!!
        when (val fileType: String = path.split(".").last()) {
            "pdf" -> {
                PDFBoxResourceLoader.init(context);
                if (task == PAGE_COUNT){
                    val pageCount: Int = PdfTask.pageCount(file)
                    Handler(Looper.getMainLooper()).post {
                        result.success(pageCount)
                    }
                } else if (task == GET_IMAGE) {
                    val index:Int = call.argument<Int>("index")!!
                    PdfTask.getPageImage(index, file)
                    Handler(Looper.getMainLooper()).post {
                        result.success(index)
                    }
                } else if (task == FETCH_TEXT){
                    val index:Int = call.argument<Int>("index")!!
                    val text:String = PdfTask.fetchText(index, file)
                    Handler(Looper.getMainLooper()).post {
                        result.success(text)
                    }
                }
            }
            "docx" -> {
                android.util.Log.d("Android", fileType + "No task")
            }
            "ppt" -> {
                android.util.Log.d("Android", fileType + "No task")
            }
        }
    }
}
