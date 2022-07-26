package com.example.calculatormvc

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

/*
 * A Calculator uses PEMDAS and parses input as a solvable equation.
 * Supports decimal calculations.
 * Displays an intermediate result on top in red small letters and the final result in black.
 * TODO: What other ideas for Observers (ex: some TableView also subscribes to changes in Model)
*/
class Calculator(_activity: Activity) : Observer { // primary constructor

    private val mActivity: Activity = _activity
    private val mCalculatorModel: CalculatorModel = CalculatorModel()

    // Contains the final result of calculation
    private val mResultView: TextView = mActivity.findViewById(R.id.tvInput)

    // Contains the intermediate result of input
    private val mIntermediateView: TextView = mActivity.findViewById(R.id.interInput)

    // Calculator buttons
    private val mButtonViews: ArrayList<Button> =
        (mActivity.findViewById<View>(R.id.parent_container)).touchables as ArrayList<Button>

    init { // initializes references to all views from activity

        mCalculatorModel.addObserver(this) // to listen for value changes

        for (btn in mButtonViews) { // handles click responses for each button
            btn.setOnClickListener {
                val intermediateExpr = mCalculatorModel.getIntermediateResult()
                if (btn.text.contains("=")) {
                    mCalculatorModel.setFinalResult(intermediateExpr)
                    calculate(intermediateExpr)
                } else {
                    //mResultView.text = ""
                    mCalculatorModel.setIntermediateResult(intermediateExpr + btn.text)
                }
            }
        }

        // Resets calculation
        mActivity.findViewById<Button>(R.id.cancel).setOnClickListener { reset() }

        // Handles Backspace functionality
        mActivity.findViewById<Button>(R.id.back).setOnClickListener {
            val intermediate = mCalculatorModel.getIntermediateResult()

            if (intermediate.isNotEmpty()) {
                mCalculatorModel.setIntermediateResult(
                    intermediate.substring(0, intermediate.lastIndex)
                )
            }
        }
    }

    // Calculates and outputs a result for a given equation
    private fun calculate(stringResult: String) {
        try {
            val e = ExpressionBuilder(stringResult).build()
            val result = e.evaluate()
            mCalculatorModel.setFinalResult(result.toString())
            //mCalculatorModel.setIntermediateResult(result.toString())
            mResultView.contentDescription =
                mActivity.getString(R.string.result) + mResultView.text
            mResultView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
        } catch (e: IllegalArgumentException) {
            handleError(stringResult) // bad user input
        } catch (e: EmptyStackException) {
            handleError(stringResult)
        }
    }

    // Resets state of calculator model
    private fun reset() {
        mResultView.announceForAccessibility(mActivity.getString(R.string.equation_cleared))
        mCalculatorModel.reset()
    }

    // Catches errors in expression parsing
    private fun handleError(causeOfError: String) {
        Log.i("DEBUG", "Invalid input: $causeOfError")
        mIntermediateView.announceForAccessibility(mActivity.getString(R.string.error_msg))
        reset()
    }

    // Automatically updates the View (appearance) after values in model have been modified
    // Overrides: https://developer.android.com/reference/kotlin/java/util/Observer#update
    override fun update(p0: Observable?, p1: Any?) {
        mResultView.text = mCalculatorModel.getFinalResult()
        mIntermediateView.text = mCalculatorModel.getIntermediateResult()
        mIntermediateView.announceForAccessibility(mIntermediateView.text)
    }
}