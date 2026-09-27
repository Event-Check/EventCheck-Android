package com.eventcheck.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

/**
 * Utility object responsible for generating QR code Bitmaps using the ZXing library.
 */
object QrCodeGenerator {

    /**
     * Generates a QR Code Bitmap for the specified content string.
     *
     * @param content The text or payload to encode into the QR code.
     * @param size The width and height in pixels for the generated Bitmap.
     * @return A [Bitmap] containing the generated QR code, or null if generation fails.
     */
    fun generateQrCode(content: String, size: Int = 512): Bitmap? {
        if (content.isBlank()) return null
        return try {
            val bitMatrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
            )
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = createBitmap(width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                }
            }
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
