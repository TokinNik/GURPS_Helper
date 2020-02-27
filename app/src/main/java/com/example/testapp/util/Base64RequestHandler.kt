package com.example.testapp.util

import android.graphics.BitmapFactory
import android.util.Base64
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler

class Base64RequestHandler : RequestHandler() {
    companion object {
        const val BASE_64_SCHEME = "base64://"
    }

    override fun canHandleRequest(data: Request?): Boolean {
        return BASE_64_SCHEME == "${data?.uri?.scheme}://"
    }

    override fun load(request: Request?, networkPolicy: Int): Result? {
        var imageCode = request?.uri?.toString() ?: ""
        if (imageCode.isNotBlank())
            imageCode = imageCode.substring(BASE_64_SCHEME.length, imageCode.length)
        val bytes = Base64.decode(imageCode, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return Result(image, Picasso.LoadedFrom.DISK)
    }
}