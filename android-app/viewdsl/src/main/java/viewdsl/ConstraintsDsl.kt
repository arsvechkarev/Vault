package viewdsl

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP

fun View.constraints(block: ConstraintsDsl.() -> Unit) {
  require(layoutParams is ConstraintLayout.LayoutParams) { "Parent should be ConstraintLayout" }
  val constraintsDsl = ConstraintsDsl(id, parent as ConstraintLayout)
  constraintsDsl.apply(block).applyToLayout()
}

class ConstraintsDsl(
  private val viewId: Int,
  private val constraintLayout: ConstraintLayout
) {
  
  val constraintSet = ConstraintSet().apply { clone(constraintLayout) }
  
  val parent get() = ConstraintSet.PARENT_ID
  
  fun startToStartOf(id: Int) {
    constraintSet.connect(viewId, START, id, START)
  }
  
  fun startToEndOf(id: Int) {
    constraintSet.connect(viewId, START, id, END)
  }
  
  fun topToTopOf(id: Int) {
    constraintSet.connect(viewId, TOP, id, TOP)
  }
  
  fun endToEndOf(id: Int) {
    constraintSet.connect(viewId, END, id, END)
  }
  
  fun endToStartOf(id: Int) {
    constraintSet.connect(viewId, END, id, START)
  }
  
  fun bottomToBottomOf(id: Int) {
    constraintSet.connect(viewId, BOTTOM, id, BOTTOM)
  }
  
  fun topToBottomOf(id: Int) {
    constraintSet.connect(viewId, TOP, id, BOTTOM)
  }
  
  fun bottomToTopOf(id: Int) {
    constraintSet.connect(viewId, BOTTOM, id, TOP)
  }
  
  fun centeredWithin(id: Int) {
    topToTopOf(id)
    startToStartOf(id)
    endToEndOf(id)
    bottomToBottomOf(id)
  }
  
  fun setVerticalBias(bias: Float) {
    constraintSet.setVerticalBias(viewId, bias)
  }
  
  fun applyToLayout() {
    constraintSet.applyTo(constraintLayout)
  }
}
