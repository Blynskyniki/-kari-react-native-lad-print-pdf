package com.rnLadPrintPdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.print.PrintAttributes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.printManager
import java.io.File
import java.io.FileOutputStream

/**
 * Печать PDF и сохранение PDF из base64
 */
class RNLadPrintPdfModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "RNLadPrintPdf"
    }

    private fun sendEvent(reactContext: ReactContext,
                          eventName: String,
                          params: WritableMap?) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }

    override fun getConstants(): Map<String, Any>? {
        return HashMap()
    }


    @ReactMethod
    fun printPdf(path: String, promise: Promise) {
        if (ContextCompat.checkSelfPermission(this.reactContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.currentActivity!!,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.currentActivity!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                if (ContextCompat.checkSelfPermission(this.reactContext,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    promise.resolve(mapError(java.lang.Exception("Нет прав на чтение с файловой системы")))
                }
            }
        }
        this.print(this.reactContext,path){data ->
            promise.resolve(data)

        }
    }

    @ReactMethod
    fun saveBase64ToPdf(name:String,base64: String, promise: Promise) {
        if (ContextCompat.checkSelfPermission(this.reactContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.currentActivity!!,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.currentActivity!!,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                if (ContextCompat.checkSelfPermission(this.reactContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    promise.resolve(mapError(java.lang.Exception("Нет прав на запись в файлову систем")))
                }
            }
        }


        doAsync {
            savePdf(name,base64){ data ->
                promise.resolve(data)
                return@savePdf
            }
            return@doAsync
        }

    }

    @ReactMethod
    fun printBase64Pdf(base64: String, promise: Promise) {
        this.printBase64(this.reactContext,base64){data ->
            promise.resolve(data)

        }
    }


    private fun savePdf(name:String,base64: String,result:(data:WritableMap)->Unit){
        try {
            val stringPath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/.${name}.pdf"
            val dwldsPath = File(stringPath);
            val pdfAsBytes = Base64.decode(base64, 0);
            val os = FileOutputStream(dwldsPath, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            val params = Arguments.createMap()
            params.putString("path", stringPath)
            params.putBoolean("save", true)
            result(params)

        }catch (e:java.lang.Exception){
            result(mapError(e))
        }
    }

    private fun printBase64(cntx: ReactContext, base64: String,result:(data:WritableMap)->Unit) {

        try {
                val pdfAsBytes = Base64.decode(base64, 0);
                val printManager = getCurrentActivity()!!.printManager  //.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = PdfDocumentAdapter(cntx, pdfAsBytes);
                printManager.print("Document", printAdapter, PrintAttributes.Builder().build());
                val params = Arguments.createMap()
                params.putBoolean("send", true)
                result(params)

            } catch (e: java.lang.Exception) {
                Log.e(reactContext.packageName, e.toString())
                result( mapError(e))

            }


    }

    private fun print(cntx: ReactContext, path: String,result:(data:WritableMap)->Unit) {

            try {

                val printManager = getCurrentActivity()!!.printManager  //.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = PdfDocumentAdapter(cntx, path);
                printManager.print("Document", printAdapter, PrintAttributes.Builder().build());
                val params = Arguments.createMap()
                params.putBoolean("send", true)
                result(params)

            } catch (e: java.lang.Exception) {
                Log.e(reactContext.packageName, e.toString())
                result( mapError(e))

            }


    }

    private fun mapError(e: Exception): WritableMap {
        val params = Arguments.createMap()
        params.putString("errorCode", "1")
        params.putString("errorMsg", e.message)
        params.putBoolean("send", false)
        params.putBoolean("save", false)
        return params

    }
}