package com.jtcxw.glcxw.base.respmodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderConfirmBean implements Parcelable {
    /**
     * order_id : 1000211341
     * busi_type : 32
     * trip_type : 4
     * order_price : 0.01
     * order_time : 2020-10-12 16:08:47
     * order_state : 0
     * coupon_fee : 0
     * tikmodel_id : 4053
     * tik_price : 0.01
     * from_city : 桂林
     * to_city : 桂林
     * from_station_id : 1501
     * from_station_name : 磨盘山码头
     * to_station_id : 1502
     * to_station_name : 鼎富大厦中心
     * ride_time : 2020-10-14 18:00:00
     * start_station_name : 磨盘山码头
     * end_station_name : 鼎富大厦中心
     * vehicle_type_name : 旅游大巴47座
     * ctrip_order_id : 153301
     * seat_no : 1
     * ticket_no : 1040536400184
     * tikcet_state : N
     * check_time :
     * car_no :
     * passenger_info : [{"passenger_card_id":"","passenger_name":"","seat_no":"1","ticket_no":"1040536400184","tikcet_state":"N","check_time":""}]
     * refund_remark : 测试请勿购买
     */

    private long order_id;
    private int busi_type;
    private int trip_type;
    private double order_price;
    private String order_time;
    private int order_state;
    private int coupon_fee;
    private long tikmodel_id;
    private double tik_price;
    private String from_city;
    private String to_city;
    private long from_station_id;
    private String from_station_name;
    private long to_station_id;
    private String to_station_name;
    private String ride_time;
    private String start_station_name;
    private String end_station_name;
    private String vehicle_type_name;
    private long ctrip_order_id;
    private String seat_no;
    private String ticket_no;
    private String tikcet_state;
    private String check_time;
    private String car_no;
    private String refund_remark;
    private List<PassengerInfoBean> passenger_info;


    protected OrderConfirmBean(Parcel in) {
        order_id = in.readLong();
        busi_type = in.readInt();
        trip_type = in.readInt();
        order_price = in.readDouble();
        order_time = in.readString();
        order_state = in.readInt();
        coupon_fee = in.readInt();
        tikmodel_id = in.readLong();
        tik_price = in.readDouble();
        from_city = in.readString();
        to_city = in.readString();
        from_station_id = in.readLong();
        from_station_name = in.readString();
        to_station_id = in.readLong();
        to_station_name = in.readString();
        ride_time = in.readString();
        start_station_name = in.readString();
        end_station_name = in.readString();
        vehicle_type_name = in.readString();
        ctrip_order_id = in.readLong();
        seat_no = in.readString();
        ticket_no = in.readString();
        tikcet_state = in.readString();
        check_time = in.readString();
        car_no = in.readString();
        refund_remark = in.readString();
        passenger_info = in.createTypedArrayList(PassengerInfoBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(order_id);
        dest.writeInt(busi_type);
        dest.writeInt(trip_type);
        dest.writeDouble(order_price);
        dest.writeString(order_time);
        dest.writeInt(order_state);
        dest.writeInt(coupon_fee);
        dest.writeLong(tikmodel_id);
        dest.writeDouble(tik_price);
        dest.writeString(from_city);
        dest.writeString(to_city);
        dest.writeLong(from_station_id);
        dest.writeString(from_station_name);
        dest.writeLong(to_station_id);
        dest.writeString(to_station_name);
        dest.writeString(ride_time);
        dest.writeString(start_station_name);
        dest.writeString(end_station_name);
        dest.writeString(vehicle_type_name);
        dest.writeLong(ctrip_order_id);
        dest.writeString(seat_no);
        dest.writeString(ticket_no);
        dest.writeString(tikcet_state);
        dest.writeString(check_time);
        dest.writeString(car_no);
        dest.writeString(refund_remark);
        dest.writeTypedList(passenger_info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderConfirmBean> CREATOR = new Creator<OrderConfirmBean>() {
        @Override
        public OrderConfirmBean createFromParcel(Parcel in) {
            return new OrderConfirmBean(in);
        }

        @Override
        public OrderConfirmBean[] newArray(int size) {
            return new OrderConfirmBean[size];
        }
    };

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getBusi_type() {
        return busi_type;
    }

    public void setBusi_type(int busi_type) {
        this.busi_type = busi_type;
    }

    public int getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(int trip_type) {
        this.trip_type = trip_type;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public int getOrder_state() {
        return order_state;
    }

    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }

    public int getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(int coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public long getTikmodel_id() {
        return tikmodel_id;
    }

    public void setTikmodel_id(long tikmodel_id) {
        this.tikmodel_id = tikmodel_id;
    }

    public double getTik_price() {
        return tik_price;
    }

    public void setTik_price(double tik_price) {
        this.tik_price = tik_price;
    }

    public String getFrom_city() {
        return from_city;
    }

    public void setFrom_city(String from_city) {
        this.from_city = from_city;
    }

    public String getTo_city() {
        return to_city;
    }

    public void setTo_city(String to_city) {
        this.to_city = to_city;
    }

    public long getFrom_station_id() {
        return from_station_id;
    }

    public void setFrom_station_id(long from_station_id) {
        this.from_station_id = from_station_id;
    }

    public String getFrom_station_name() {
        return from_station_name;
    }

    public void setFrom_station_name(String from_station_name) {
        this.from_station_name = from_station_name;
    }

    public long getTo_station_id() {
        return to_station_id;
    }

    public void setTo_station_id(long to_station_id) {
        this.to_station_id = to_station_id;
    }

    public String getTo_station_name() {
        return to_station_name;
    }

    public void setTo_station_name(String to_station_name) {
        this.to_station_name = to_station_name;
    }

    public String getRide_time() {
        return ride_time;
    }

    public void setRide_time(String ride_time) {
        this.ride_time = ride_time;
    }

    public String getStart_station_name() {
        return start_station_name;
    }

    public void setStart_station_name(String start_station_name) {
        this.start_station_name = start_station_name;
    }

    public String getEnd_station_name() {
        return end_station_name;
    }

    public void setEnd_station_name(String end_station_name) {
        this.end_station_name = end_station_name;
    }

    public String getVehicle_type_name() {
        return vehicle_type_name;
    }

    public void setVehicle_type_name(String vehicle_type_name) {
        this.vehicle_type_name = vehicle_type_name;
    }

    public long getCtrip_order_id() {
        return ctrip_order_id;
    }

    public void setCtrip_order_id(long ctrip_order_id) {
        this.ctrip_order_id = ctrip_order_id;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
    }

    public String getTikcet_state() {
        return tikcet_state;
    }

    public void setTikcet_state(String tikcet_state) {
        this.tikcet_state = tikcet_state;
    }

    public String getCheck_time() {
        return check_time;
    }

    public void setCheck_time(String check_time) {
        this.check_time = check_time;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getRefund_remark() {
        return refund_remark;
    }

    public void setRefund_remark(String refund_remark) {
        this.refund_remark = refund_remark;
    }

    public List<PassengerInfoBean> getPassenger_info() {
        return passenger_info;
    }

    public void setPassenger_info(List<PassengerInfoBean> passenger_info) {
        this.passenger_info = passenger_info;
    }



    public static class PassengerInfoBean implements Parcelable {
        /**
         * passenger_card_id :
         * passenger_name :
         * seat_no : 1
         * ticket_no : 1040536400184
         * tikcet_state : N
         * check_time :
         */

        private String passenger_card_id;
        private String passenger_name;
        private String seat_no;
        private String ticket_no;
        private String tikcet_state;
        private String check_time;
        private String free_ticket;

        protected PassengerInfoBean(Parcel in) {
            passenger_card_id = in.readString();
            passenger_name = in.readString();
            seat_no = in.readString();
            ticket_no = in.readString();
            tikcet_state = in.readString();
            check_time = in.readString();
            free_ticket = in.readString();
        }

        public static final Creator<PassengerInfoBean> CREATOR = new Creator<PassengerInfoBean>() {
            @Override
            public PassengerInfoBean createFromParcel(Parcel in) {
                return new PassengerInfoBean(in);
            }

            @Override
            public PassengerInfoBean[] newArray(int size) {
                return new PassengerInfoBean[size];
            }
        };

        public String getPassenger_card_id() {
            return passenger_card_id;
        }

        public void setPassenger_card_id(String passenger_card_id) {
            this.passenger_card_id = passenger_card_id;
        }

        public String getPassenger_name() {
            return passenger_name;
        }

        public void setPassenger_name(String passenger_name) {
            this.passenger_name = passenger_name;
        }

        public String getSeat_no() {
            return seat_no;
        }

        public void setSeat_no(String seat_no) {
            this.seat_no = seat_no;
        }

        public String getTicket_no() {
            return ticket_no;
        }

        public void setTicket_no(String ticket_no) {
            this.ticket_no = ticket_no;
        }

        public String getTikcet_state() {
            return tikcet_state;
        }

        public void setTikcet_state(String tikcet_state) {
            this.tikcet_state = tikcet_state;
        }

        public String getCheck_time() {
            return check_time;
        }

        public void setCheck_time(String check_time) {
            this.check_time = check_time;
        }

        public String getFree_ticket() {
            return free_ticket;
        }

        public void setFree_ticket(String free_ticket) {
            this.free_ticket = free_ticket;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(passenger_card_id);
            parcel.writeString(passenger_name);
            parcel.writeString(seat_no);
            parcel.writeString(ticket_no);
            parcel.writeString(tikcet_state);
            parcel.writeString(check_time);
            parcel.writeString(free_ticket);
        }
    }
}
