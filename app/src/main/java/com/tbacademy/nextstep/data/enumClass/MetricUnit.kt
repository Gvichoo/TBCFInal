package com.tbacademy.nextstep.data.enumClass

enum class MetricUnit(val unit: String) {
    KILOGRAM("kg"),
    KILOMETER("km"),
    METER("m");

    companion object {
        fun from(unit: String): MetricUnit? =
            entries.find { it.unit == unit.lowercase() }
    }
}