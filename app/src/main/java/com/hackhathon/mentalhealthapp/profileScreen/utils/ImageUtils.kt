import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    // Убрали композейбл функцию, будем работать через ресурсы напрямую
    fun saveImageToInternalStorage(context: Context, uri: Uri, filename: String): Uri? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val outputDir = File(context.filesDir, "profile_images")
            if (!outputDir.exists()) outputDir.mkdirs()

            val outputFile = File(outputDir, filename)
            val outputStream = FileOutputStream(outputFile)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            Uri.fromFile(outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadImageFromInternalStorage(context: Context, filename: String): Uri? {
        return try {
            val file = File(context.filesDir, "profile_images/$filename")
            if (file.exists()) Uri.fromFile(file) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteProfileImage(context: Context, filename: String): Boolean {
        return try {
            val file = File(context.filesDir, "profile_images/$filename")
            if (file.exists()) {
                file.delete()
            } else {
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}