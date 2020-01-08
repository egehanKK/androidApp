package com.egehankarakose.sorudefterim.Models

import android.graphics.Bitmap

data class ImagesModel(val image: Bitmap, val setId: String = "", var isChecked: Boolean = false)