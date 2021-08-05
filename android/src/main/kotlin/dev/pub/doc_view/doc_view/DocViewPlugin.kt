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
import org.json.JSONObject
import java.util.*

const val PAGE_COUNT:String = "pageCount"
const val GEN_THUMBS:String = "genThumbs"
const val GET_IMAGE:String = "getImage"

/** DocViewPlugin */
class DocViewPlugin: FlutterPlugin, MethodCallHandler {
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
            singleThreadTask(call, GET_IMAGE, result)
        }
        PAGE_COUNT -> {
            singleThreadTask(call, PAGE_COUNT, result)
        }
        else -> {
          result.notImplemented()
        }
    }
  }

    private fun singleThreadTask(@NonNull call: MethodCall, task: String, result: Result){
        val thread = Thread(
            SingleThreadTask(call, task, result, context)
        )
        thread.start()
    }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

class SingleThreadTask(private val call: MethodCall, private val task: String, private val result: Result, private val context:  android.content.Context): Runnable {
    override fun run() {
        val path:String = call.argument<String>("path")!!
        when (val fileType: String = path.split(".").last()) {
            "pdf" -> {
                PDFBoxResourceLoader.init(context);
                if (task == PAGE_COUNT){
                    val pageCount: Int = PdfTask.pageCount(path)
                    Handler(Looper.getMainLooper()).post {
                        result.success(pageCount)
                    }
                } else if (task == GET_IMAGE) {
                    val index:Int = call.argument<Int>("index")!!
                    PdfTask.getPageImage(path, index, context)
                    Handler(Looper.getMainLooper()).post {
                        result.success(index)
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
