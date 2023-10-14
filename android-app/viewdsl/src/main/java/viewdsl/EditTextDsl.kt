package viewdsl

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.EditText

fun interface BaseTextWatcher : TextWatcher {
  
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
  override fun afterTextChanged(s: Editable) = onTextChange(s.toString())
  
  fun onTextChange(text: String)
}

fun EditText.setMaxLength(max: Int) {
  filters += arrayOf(InputFilter.LengthFilter(max))
}

fun EditText.onTextChanged(block: (text: String) -> Unit) {
  addTextChangedListener(BaseTextWatcher {
    block(it)
  })
}

fun EditText.onSubmit(block: (Editable) -> Unit) {
  imeOptions = IME_ACTION_DONE
  setOnEditorActionListener { _, actionId, _ ->
    if (actionId == IME_ACTION_DONE) {
      block(text)
      return@setOnEditorActionListener true
    }
    return@setOnEditorActionListener false
  }
}

fun EditText.setTextSilently(newText: String, textWatcher: BaseTextWatcher) {
  removeTextChangedListener(textWatcher)
  if (text.toString() != newText) {
    setText(newText)
  }
  addTextChangedListener(textWatcher)
}
