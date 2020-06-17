package com.example.mymmal.dataobject;

import java.util.Date;

public class ShippingDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.user_id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_name
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_phone
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverPhone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_mobile
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverMobile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_province
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverProvince;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_city
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverCity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_district
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverDistrict;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_address
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.receiver_zip
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private String receiverZip;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.create_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shipping.update_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.id
     *
     * @return the value of shipping.id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.id
     *
     * @param id the value for shipping.id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.user_id
     *
     * @return the value of shipping.user_id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.user_id
     *
     * @param userId the value for shipping.user_id
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_name
     *
     * @return the value of shipping.receiver_name
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_name
     *
     * @param receiverName the value for shipping.receiver_name
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_phone
     *
     * @return the value of shipping.receiver_phone
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_phone
     *
     * @param receiverPhone the value for shipping.receiver_phone
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_mobile
     *
     * @return the value of shipping.receiver_mobile
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_mobile
     *
     * @param receiverMobile the value for shipping.receiver_mobile
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile == null ? null : receiverMobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_province
     *
     * @return the value of shipping.receiver_province
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverProvince() {
        return receiverProvince;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_province
     *
     * @param receiverProvince the value for shipping.receiver_province
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince == null ? null : receiverProvince.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_city
     *
     * @return the value of shipping.receiver_city
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverCity() {
        return receiverCity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_city
     *
     * @param receiverCity the value for shipping.receiver_city
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity == null ? null : receiverCity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_district
     *
     * @return the value of shipping.receiver_district
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_district
     *
     * @param receiverDistrict the value for shipping.receiver_district
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict == null ? null : receiverDistrict.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_address
     *
     * @return the value of shipping.receiver_address
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_address
     *
     * @param receiverAddress the value for shipping.receiver_address
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.receiver_zip
     *
     * @return the value of shipping.receiver_zip
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public String getReceiverZip() {
        return receiverZip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.receiver_zip
     *
     * @param receiverZip the value for shipping.receiver_zip
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip == null ? null : receiverZip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.create_time
     *
     * @return the value of shipping.create_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.create_time
     *
     * @param createTime the value for shipping.create_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shipping.update_time
     *
     * @return the value of shipping.update_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shipping.update_time
     *
     * @param updateTime the value for shipping.update_time
     *
     * @mbg.generated Wed Jun 17 02:31:47 CST 2020
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}