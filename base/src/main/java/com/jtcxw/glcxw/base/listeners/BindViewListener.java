package com.jtcxw.glcxw.base.listeners;

import android.view.View;

public interface BindViewListener<T> {
    void onBindView(T data, View itemView, int pos);
}
