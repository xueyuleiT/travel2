package com.jtcxw.glcxw.localbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MessageBean {

    @Id(autoincrement = true)
    public Long id;

    @Property
    public String title;

    @Property
    public String phone;

    @Property
    public String BusinessId;

    @Property
    public String MessageType;

    @Property
    public String PushType;

    @Property
    public int read = 0;

    @Property
    public String  time;
    @Property
    public String  type;
    @Property
    public String  detail;
    @Generated(hash = 176915582)
    public MessageBean(Long id, String title, String phone, String BusinessId,
            String MessageType, String PushType, int read, String time, String type,
            String detail) {
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.BusinessId = BusinessId;
        this.MessageType = MessageType;
        this.PushType = PushType;
        this.read = read;
        this.time = time;
        this.type = type;
        this.detail = detail;
    }
    @Generated(hash = 1588632019)
    public MessageBean() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getRead() {
        return this.read;
    }
    public void setRead(int read) {
        this.read = read;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBusinessId() {
        return this.BusinessId;
    }
    public void setBusinessId(String BusinessId) {
        this.BusinessId = BusinessId;
    }
    public String getMessageType() {
        return this.MessageType;
    }
    public void setMessageType(String MessageType) {
        this.MessageType = MessageType;
    }
    public String getPushType() {
        return this.PushType;
    }
    public void setPushType(String PushType) {
        this.PushType = PushType;
    }
}
