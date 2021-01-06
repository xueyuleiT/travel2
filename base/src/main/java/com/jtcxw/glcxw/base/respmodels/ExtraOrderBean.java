package com.jtcxw.glcxw.base.respmodels;

public class ExtraOrderBean {
    private int NeedPayCount;
    private int WaitingToUseCount;
    private int TicketreFundedCount;


    public int getNeedPayCount() {
        return NeedPayCount;
    }

    public void setNeedPayCount(int needPayCount) {
        NeedPayCount = needPayCount;
    }

    public int getWaitingToUseCount() {
        return WaitingToUseCount;
    }

    public void setWaitingToUseCount(int waitingToUseCount) {
        WaitingToUseCount = waitingToUseCount;
    }

    public int getTicketreFundedCount() {
        return TicketreFundedCount;
    }

    public void setTicketreFundedCount(int ticketreFundedCount) {
        TicketreFundedCount = ticketreFundedCount;
    }
}
