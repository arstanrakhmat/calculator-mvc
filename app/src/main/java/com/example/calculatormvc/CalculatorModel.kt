package com.example.calculatormvc

import java.util.*

/*
 * A CalculatorModel holds data for a calculator.
*/
class CalculatorModel : Observable() {
    private var mIntermediateExpression: String = ""
    private var mFinalResult: String = ""

    // Returns a String representing the expression that has been typed into
    // the calculator so far.
    fun getIntermediateResult(): String {
        return mIntermediateExpression
    }

    fun setIntermediateResult(new_intermediate_result: String) {
        mIntermediateExpression = new_intermediate_result

        /**
         * TODO: Hide the final res while '=' is not being pressed
         */

        notifyChange()
    }

    // Returns a String representing the result of the entered expression.
    fun getFinalResult(): String {
        return mFinalResult
    }

    fun setFinalResult(new_final_result: String) {
        mFinalResult = new_final_result
        notifyChange()
    }

    fun reset() {
        mIntermediateExpression = ""
        mFinalResult = "0.0"
        notifyChange()
    }

    private fun notifyChange() {
        setChanged() // Marks Observable object as having been changed
        notifyObservers() // notify all observers and then set to "not changed"
    }
}