package com.alperenbabagil.simpleanalogclockviewlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import java.util.*
import kotlin.math.min

class SimpleAnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs) {


    @ColorRes
    var defaultColor: Int = R.color.dark

    @ColorRes
    var minuteHandColorResId: Int = defaultColor
    @ColorRes
    var hourHandColorResId: Int = defaultColor
    @ColorRes
    var borderColorResId: Int = defaultColor

    var min = 0
        set(value) {
            field = value
            arrangeTime()
        }
    var hour = 0
        set(value) {
            field = value
            arrangeTime()
        }

    private var borderThickness = DEFAULT_THICKNESS_IN_DP
    private var hourHandThickness = DEFAULT_THICKNESS_IN_DP
    private var minuteHandThickness = DEFAULT_THICKNESS_IN_DP

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var centerCoords = Pair<Float, Float>(0f, 0f) // x,y

    private var r = 0F

    var hourAngle = 0.0
    var minuteAngle = 0.0

    var hourLengthRatio = 0.5F
    var minuteLengthRatio = 0.75F

    var hourLength = 0F
    var minuteLength = 0F

    var valuesUpdated = false

    fun updateTime(date: Date) {
        val cl = Calendar.getInstance().apply {
            timeInMillis = date.time
        }
        min = cl.get(Calendar.MINUTE)
        hour = cl.get(Calendar.HOUR)

        arrangeTime()
    }

    private fun arrangeTime() {
        val hourDegree = (hour % 12) * 30 +
             min * 0.5 // adding extra angle because of the minute

        val minuteDegree = min * 6F

        hourAngle = hourDegree - 90 // because of the start point and direction of android system
        minuteAngle = minuteDegree - 90.0 // because of the start point and direction of android system

        invalidate()
    }

    // making view always circle
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.simple_analog_clock_view_attributes, 0, 0
            )

            val allItemsColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resources.getColor(
                    typedArray
                        .getResourceId(
                            R.styleable
                                .simple_analog_clock_view_attributes_all_items_color,
                            defaultColor
                        ), context.theme
                )
            } else {
                resources.getColor(
                    typedArray
                        .getResourceId(
                            R.styleable
                                .simple_analog_clock_view_attributes_all_items_color,
                            defaultColor
                        )
                )
            }

            if (allItemsColor == defaultColor) {
                minuteHandColorResId = allItemsColor
                hourHandColorResId = allItemsColor
                borderColorResId = allItemsColor
            } else {
                minuteHandColorResId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_minute_hand_color,
                                R.color.dark
                            ), context.theme
                    )
                } else {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_minute_hand_color,
                                R.color.dark
                            )
                    )
                }

                hourHandColorResId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_hour_hand_color,
                                R.color.dark
                            ), context.theme
                    )
                } else {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_minute_hand_color,
                                R.color.dark
                            )
                    )
                }

                borderColorResId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_border_color,
                                R.color.dark
                            ), context.theme
                    )
                } else {
                    resources.getColor(
                        typedArray
                            .getResourceId(
                                R.styleable
                                    .simple_analog_clock_view_attributes_border_color,
                                R.color.dark
                            )
                    )
                }
            }

            val allItemsThickness =
                typedArray.getDimensionPixelSize(
                    R.styleable.simple_analog_clock_view_attributes_all_items_thickness,
                    NO_VALUE
                ).toFloat()

            if (allItemsThickness != NO_VALUE.toFloat()) {
                borderThickness = allItemsThickness
                hourHandThickness = allItemsThickness
                minuteHandThickness = allItemsThickness
            } else {
                borderThickness =
                    typedArray.getDimensionPixelSize(
                        R.styleable.simple_analog_clock_view_attributes_border_thickness,
                        DEFAULT_THICKNESS_IN_DP.toInt()
                    ).toFloat()

                hourHandThickness =
                    typedArray.getDimensionPixelSize(
                        R.styleable.simple_analog_clock_view_attributes_hour_hand_thickness,
                        DEFAULT_THICKNESS_IN_DP.toInt()
                    ).toFloat()

                minuteHandThickness =
                    typedArray.getDimensionPixelSize(
                        R.styleable.simple_analog_clock_view_attributes_minute_hand_thickness,
                        DEFAULT_THICKNESS_IN_DP.toInt()
                    ).toFloat()
            }

            hour = typedArray.getInt(R.styleable.simple_analog_clock_view_attributes_hour, 0)
            min = typedArray.getInt(R.styleable.simple_analog_clock_view_attributes_minute, 0)

            hourLengthRatio = typedArray.getFloat(
                R.styleable.simple_analog_clock_view_attributes_hour_hand_r_ratio,
                hourLengthRatio
            )

            minuteLengthRatio = typedArray.getFloat(
                R.styleable.simple_analog_clock_view_attributes_minute_hand_r_ratio,
                minuteLengthRatio
            )

            typedArray.recycle()

            arrangeTime()
        }
    }

    private fun updateUIValues() {
        centerCoords = Pair(width / 2f, height / 2f)
        r = (width / 2).toFloat()
        hourLength = r * hourLengthRatio
        minuteLength = r * minuteLengthRatio
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!valuesUpdated) {
            updateUIValues()
            valuesUpdated = true
        }

        // drawing hour hand
        paint.color = hourHandColorResId
        paint.strokeWidth = hourHandThickness.toFloat()

        canvas.drawLine(
            centerCoords.first,
            centerCoords.second,
            centerCoords.first + (kotlin.math.cos(Math.toRadians(hourAngle))
                .toFloat() * hourLength),
            centerCoords.second + (kotlin.math.sin(Math.toRadians(hourAngle))
                .toFloat() * hourLength),
            paint
        )

        // drawing minute hand
        paint.color = minuteHandColorResId
        paint.strokeWidth = minuteHandThickness.toFloat()

        canvas.drawLine(
            centerCoords.first,
            centerCoords.second,
            centerCoords.first + (kotlin.math.cos(Math.toRadians(minuteAngle))
                .toFloat() * minuteLength),
            centerCoords.second + (kotlin.math.sin(Math.toRadians(minuteAngle))
                .toFloat() * minuteLength),
            paint
        )

        // drawing border
        paint.color = borderColorResId
        paint.strokeWidth = borderThickness.toFloat()
        paint.style = Paint.Style.STROKE

        canvas.drawCircle(centerCoords.first, centerCoords.second, r - borderThickness / 2, paint)
    }

    companion object {
        const val DEFAULT_THICKNESS_IN_DP = 5F
        const val NO_VALUE = -1
    }
}