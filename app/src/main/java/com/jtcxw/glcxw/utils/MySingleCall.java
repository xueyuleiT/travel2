package com.jtcxw.glcxw.utils;

import com.toptechs.libaction.action.Action;
import com.toptechs.libaction.action.CallUnit;
import com.toptechs.libaction.action.Valid;

//添加登录后的执行操作
public class MySingleCall {

    CallUnit callUnit = new CallUnit();

    public MySingleCall addAction(Action action){
        clear();
        callUnit.setAction(action);
        return this;
    }


    public MySingleCall addValid(Valid valid){
        //只添加无效的，验证不通过的。
        if(valid.check()){
            return this;
        }
        callUnit.addValid(valid);
        return this;
    }

    public void doCall(){

        //如果上一条valid难没有通过，是不允许再发起call的
        if(callUnit.getLastValid() != null && !callUnit.getLastValid().check() ){
            return;
        }

        //执行action
        if(callUnit.getValidQueue().size() == 0 && callUnit.getAction() != null){
            callUnit.getAction().call();
            //清空
            clear();
        }else{
            //执行验证。
            Valid valid = callUnit.getValidQueue().poll();
            if (valid != null) {
                callUnit.setLastValid(valid);
                valid.doValid();
            }
        }

    }

    public void clear(){
        callUnit.getValidQueue().clear();
        callUnit.setAction(null);
        callUnit.setLastValid(null);
    }


    // 单一全局访问点
    public static MySingleCall getInstance() {
        if (MySingleCall.SingletonHolder.mInstance == null) {
            MySingleCall.SingletonHolder.mInstance = new MySingleCall();
        }
        return MySingleCall.SingletonHolder.mInstance;
    }

    // 静态内部类，第一次加载Singleton类时不会初始化mInstance，
    // 当调用getInstance()时才会初始化
    private static class SingletonHolder {
        private static MySingleCall mInstance = new MySingleCall();
    }

}
