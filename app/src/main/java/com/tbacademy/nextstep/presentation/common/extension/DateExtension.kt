package com.tbacademy.nextstep.presentation.common.extension

import android.text.format.DateUtils
import java.util.Date

fun Date.toTimeAgo(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.time,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}