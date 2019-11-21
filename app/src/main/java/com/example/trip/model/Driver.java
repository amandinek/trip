
package com.example.trip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("permit")
    @Expose
    private String permit;
    @SerializedName("tel")
    @Expose
    private Integer tel;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("platenbr")
    @Expose
    private Object platenbr;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Driver() {
    }

    /**
     * 
     * @param platenbr
     * @param name
     * @param permit
     * @param tel
     * @param userId
     */
    public Driver(String name, String permit, Integer tel, Object userId, Object platenbr) {
        super();
        this.name = name;
        this.permit = permit;
        this.tel = tel;
        this.userId = userId;
        this.platenbr = platenbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getPlatenbr() {
        return platenbr;
    }

    public void setPlatenbr(Object platenbr) {
        this.platenbr = platenbr;
    }

}
