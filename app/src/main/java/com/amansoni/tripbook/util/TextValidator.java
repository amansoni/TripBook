package com.amansoni.tripbook.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * Created by Aman on 19/02/2015.
 *
 * Usage:
 * editText.addTextChangedListener(new TextValidator(editText) {
 *   @Override public void validate(TextView textView, String text) {
 *   //Validation code here
 *  }
 * });
 *
 * Abstract class
 * http://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input/11838715#11838715
 * Error response
 * http://www.donnfelker.com/android-validation-with-edittext/
 */
public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = textView.getText().toString();
        validate(textView, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}