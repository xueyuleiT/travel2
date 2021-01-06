package com.jtcxw.glcxw.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class DecimalInputTextWatcher implements TextWatcher {
    /**
     * 需要设置该 DecimalInputTextWatcher 的 EditText
     */
    private EditText editText = null;

    /**
     * 默认  小数的位数   2 位
     */
    private static final int DEFAULT_DECIMAL_DIGITS = 2;

    private int decimalDigits;// 小数的位数
    private int integerDigits;// 整数的位数


    public DecimalInputTextWatcher(EditText editText) {
        this.editText = editText;
        this.decimalDigits = DEFAULT_DECIMAL_DIGITS;
    }

    /**
     * @param editText      editText
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcher(EditText editText, int decimalDigits) {
        this.editText = editText;
        if (decimalDigits <= 0){
            decimalDigits = 2;
        }
//            throw new RuntimeException("decimalDigits must > 0");
        this.decimalDigits = decimalDigits;
    }

    /**
     * @param editText      editText
     * @param integerDigits 整数的位数
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcher(EditText editText, int integerDigits, int decimalDigits) {
        this.editText = editText;
        if (integerDigits <= 0)
            throw new RuntimeException("integerDigits must > 0");
        if (decimalDigits <= 0)
            throw new RuntimeException("decimalDigits must > 0");
        this.integerDigits = integerDigits;
        this.decimalDigits = decimalDigits;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       Editable editable = editText.getText();
        String s = editable.toString().trim();
        if (TextUtils.isEmpty(s)){
            return;
        }
//        if () {
//            int index = editText.getSelectionStart();
//            editable.delete(index - 1, index);
//        }

        if (findRepeat(s,".")){
            int index = s.indexOf(".");
            s = (s.substring(0,index+1) + s.substring(index+1).replaceAll("\\.",""));
            editable.replace(0, editable.length(), s.trim());
        }
        editText.removeTextChangedListener(this);

        if (s.contains(".")) {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigits + 1)});
            }
            if (s.length() - 1 - s.indexOf(".") > decimalDigits) {
                s = s.substring(0,
                        s.indexOf(".") + decimalDigits + 1);
                editable.replace(0, editable.length(), s.trim());
            }
        } else {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
                if (s.length() > integerDigits) {
                    s = s.substring(0, integerDigits);
                    editable.replace(0, editable.length(), s.trim());
                }
            }

        }
        if (s.trim().startsWith(".")) {
            s = "0" + s;
            editable.replace(0, editable.length(), s.trim());
        }
        while (s.startsWith("0")
                && s.trim().length() > 1
                && !s.substring(1, 2).equals(".")){
            editable.replace(0, 1, "");
            s = editable.toString().trim();
        }
        editText.addTextChangedListener(this);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    boolean findRepeat(String str,String s){
        int index = str.indexOf(s);
        if (index == -1) {
            return false;
        }
        str = str.substring(index + s.length());
        if (str != null && str.indexOf(s) != -1) {
            return true;
        }
        return false;
    }

}

