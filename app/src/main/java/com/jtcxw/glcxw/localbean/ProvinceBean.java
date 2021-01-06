package com.jtcxw.glcxw.localbean;

/**
 * 作者：author jiahongyin
 * 时间：2018/8/14:14:54
 * 邮箱：15010921415@163.com
 * 说明：
 */
public class ProvinceBean {


    private String name;
    private int id;

    public ProvinceBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /*这个用来显示在PickerView上面的字符串,PickerView会通过反射获
        取getPickerViewText方法显示出来。*/
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return this.name;
    }

    public int getsId() {
        //这里还可以判断文字超长截断再提供显示
        return this.id;
    }


}
