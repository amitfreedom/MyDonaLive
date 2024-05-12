package com.stream.prettylive.ui.home.ui.profile.models;

public class HostModal {
    private String realName ="";
    private String phoneNumber ="";
    private String agencyCode ="";
    private String emailAddress ="";
    private String docType ="";
    private String idCardNumber ="";
    private String idCardImage ="";
    private String status ="";
    private String userId ="";
    private String uid ="";
    private String photo ="";
    private String joiningDate ="";


    public HostModal() {
    }

    public HostModal(String realName, String phoneNumber, String agencyCode, String emailAddress, String docType, String idCardNumber, String idCardImage, String status, String userId, String uid, String photo, String joiningDate) {
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.agencyCode = agencyCode;
        this.emailAddress = emailAddress;
        this.docType = docType;
        this.idCardNumber = idCardNumber;
        this.idCardImage = idCardImage;
        this.status = status;
        this.userId = userId;
        this.uid = uid;
        this.photo = photo;
        this.joiningDate=joiningDate;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardImage() {
        return idCardImage;
    }

    public void setIdCardImage(String idCardImage) {
        this.idCardImage = idCardImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


