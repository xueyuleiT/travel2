package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class GoodsInfoBean {
    private int ChangeType;
    private double OrderAmount;
    private String Title;
    private String SubTitle;
    private List<String> Names;
    private List<String> Values;

    public int getChangeType() {
        return ChangeType;
    }

    public void setChangeType(int changeType) {
        ChangeType = changeType;
    }

    public double getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public List<String> getNames() {
        return Names;
    }

    public void setNames(List<String> names) {
        Names = names;
    }

    public List<String> getValues() {
        return Values;
    }

    public void setValues(List<String> values) {
        Values = values;
    }
}
