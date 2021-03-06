/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotredesign.features.media

import android.media.ExifInterface
import android.widget.ImageView
import im.vector.matrix.android.api.Matrix
import im.vector.matrix.android.api.session.content.ContentUrlResolver
import im.vector.riotredesign.core.glide.GlideApp

object MediaContentRenderer {

    data class Data(
            val url: String?,
            val height: Int?,
            val maxHeight: Int,
            val width: Int?,
            val maxWidth: Int,
            val orientation: Int?,
            val rotation: Int?
    )

    enum class Mode {
        FULL_SIZE,
        THUMBNAIL
    }

    fun render(data: Data, mode: Mode, imageView: ImageView) {
        val (width, height) = processSize(data, mode)
        imageView.layoutParams.height = height
        imageView.layoutParams.width = width

        val contentUrlResolver = Matrix.getInstance().currentSession.contentUrlResolver()
        val resolvedUrl = when (mode) {
                              Mode.FULL_SIZE -> contentUrlResolver.resolveFullSize(data.url)
                              Mode.THUMBNAIL -> contentUrlResolver.resolveThumbnail(data.url, width, height, ContentUrlResolver.ThumbnailMethod.SCALE)
                          }
                          ?: return

        GlideApp
                .with(imageView)
                .load(resolvedUrl)
                .thumbnail(0.3f)
                .into(imageView)
    }

    private fun processSize(data: Data, mode: Mode): Pair<Int, Int> {
        val maxImageWidth = data.maxWidth
        val maxImageHeight = data.maxHeight
        val rotationAngle = data.rotation ?: 0
        val orientation = data.orientation ?: ExifInterface.ORIENTATION_NORMAL
        var width = data.width ?: maxImageWidth
        var height = data.height ?: maxImageHeight
        var finalHeight = -1
        var finalWidth = -1

        // if the image size is known
        // compute the expected height
        if (width > 0 && height > 0) {
            // swap width and height if the image is side oriented
            if (rotationAngle == 90 || rotationAngle == 270) {
                val tmp = width
                width = height
                height = tmp
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                val tmp = width
                width = height
                height = tmp
            }
            if (mode == Mode.FULL_SIZE) {
                finalHeight = height
                finalWidth = width
            } else {
                finalHeight = Math.min(maxImageWidth * height / width, maxImageHeight)
                finalWidth = finalHeight * width / height
            }
        }
        // ensure that some values are properly initialized
        if (finalHeight < 0) {
            finalHeight = maxImageHeight
        }
        if (finalWidth < 0) {
            finalWidth = maxImageWidth
        }
        return Pair(finalWidth, finalHeight)
    }
}