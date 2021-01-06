package com.jtcxw.glcxw.base.respmodels;

import java.util.List;

public class TurnBackBean {
    /**
     * OrderId : C202011110011195828236
     * RefundFees : 0.02
     * TicketNos : ["1040739000272","1040735200180"]
     * Msg : 退款成功
     */

    private String OrderId;
    private double RefundFees;
    private String Msg;
    private List<String> TicketNos;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public double getRefundFees() {
        return RefundFees;
    }

    public void setRefundFees(double RefundFees) {
        this.RefundFees = RefundFees;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<String> getTicketNos() {
        return TicketNos;
    }

    public void setTicketNos(List<String> TicketNos) {
        this.TicketNos = TicketNos;
    }
}
