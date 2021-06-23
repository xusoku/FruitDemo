package com.sunmi.fruit.util

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * author :  chensen
 * data  :  2018/3/15
 * desc :
 */
object FileUtil {
    public val rootFolderPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "CameraDemo"

    fun createImageFile(isCrop: Boolean = false): File? {
        return try {
            var rootFile = File(rootFolderPath + File.separator + "capture")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (isCrop) "IMG_${timeStamp}_CROP.jpg" else "IMG_$timeStamp.jpg"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createCameraFile(folderName: String = "camera1"): File? {
        return try {
            val rootFile = File(rootFolderPath + File.separator + "$folderName")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "IMG_$timeStamp.jpg"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createVideoFile(): File? {
        return try {
            var rootFile = File(rootFolderPath + File.separator + "video")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "VIDEO_$timeStamp.mp4"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Context.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun log(msg: String) {
        LogSunmi.e("tag", msg)
    }
}