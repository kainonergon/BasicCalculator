package org.hyperskill.calculator

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private const val PLUS = "+"
        private const val MINUS = "−"
        private const val HYPHEN = "-"
        private const val TIMES = "×"
        private const val DIVIDE = "÷"
        private const val EQUALS = "="
        private const val DOT = "."
        private const val ZERO = "0"
    }

    private lateinit var editText: EditText // calculator display as EditText widget
    private lateinit var displayText: Editable // calculator display as Editable text object
    private val wrongNumberFormat: Boolean
        get() = displayText.toString().toDoubleOrNull() == null
    private val displayNumber: Double // calculator display converted to number value
        get() = if (wrongNumberFormat) 0.0 else displayText.toString().toDouble()
    private var register = 0.0 // stores the first operand
    private var operation = EQUALS // stores operation to perform

    private fun doubleToText(double: Double): String =
        double.toString().removeSuffix("$DOT$ZERO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        displayText = editText.text

        listOf(R.id.clearButton to ::clearClick,
            R.id.backspaceButton to ::backspaceClick,
            R.id.dotButton to ::dotClick,
            R.id.equalButton to ::equalClick)
            .forEach { (id, listener) -> findViewById<Button>(id).setOnClickListener(listener) }

        listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
            .forEach { findViewById<Button>(it).setOnClickListener(::digitClick) }

        listOf(R.id.addButton, R.id.multiplyButton, R.id.divideButton, R.id.subtractButton)
            .forEach { findViewById<Button>(it).setOnClickListener(::operationClick) }
    }

    private fun clearClick(view: View?) {
        displayText.clear()
        displayText.append(ZERO)
        editText.hint = ZERO
        register = 0.0
        operation = EQUALS
    }

    private fun backspaceClick(view: View?) {
        if (displayText.isNotEmpty()) displayText.delete(displayText.lastIndex, displayText.length)
        if (displayText.isBlank()) displayText.append(ZERO)
    }

    private fun dotClick(view: View?) {
        if (!displayText.contains(DOT)) displayText.append(DOT)
        if (wrongNumberFormat) {
            displayText.clear()
            displayText.append(ZERO)
            displayText.append(DOT)
        }
    }

    private fun digitClick(view: View?) {
        val digit = (view as Button).text.toString()
        if (
            displayText.toString() == ZERO ||
            displayText.toString() == "$HYPHEN$ZERO"
        ) displayText.delete(displayText.lastIndex, displayText.lastIndex + 1)
        displayText.append(digit)
        if (wrongNumberFormat) {
            displayText.clear()
            displayText.append(digit)
        }
    }

    private fun operationClick(view: View?) {
        val thisOperation = (view as Button).text.toString()
        if (
            thisOperation == MINUS &&
            displayText.toString() == ZERO || displayText.isBlank()
        ) {
            displayText.clear()
            displayText.append(HYPHEN)
            displayText.append(ZERO)
        } else {
            operation = thisOperation
            register = displayNumber
            editText.hint = doubleToText(register)
            displayText.clear()
        }
    }

    private fun equalClick(view: View?) {
        val current = displayNumber
        val result = when (operation) {
            PLUS -> register + current
            MINUS -> register - current
            TIMES -> register * current
            DIVIDE -> register / current
            else -> current
        }
        displayText.clear()
        displayText.append(doubleToText(result))
        register = 0.0
        operation = EQUALS
    }
}
