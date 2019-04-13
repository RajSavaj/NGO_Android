package org.foodmanoeuvre.modal;

import com.google.gson.annotations.SerializedName;

public class Restorent {
    @SerializedName("uid")
    int uid;
    @SerializedName("Name")
    String name;
    @SerializedName("latt")
    String latt;
    @SerializedName("lang")
    String lang;
    @SerializedName("mno")
    String mno;
    @SerializedName("email")
    String email;
    @SerializedName("address")
    String address;
    @SerializedName("image")
    String image;
    @SerializedName("oname")
    String oname;
    @SerializedName("omno")
    String omno;
    @SerializedName("url")
    String url;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getOmno() {
        return omno;
    }

    public void setOmno(String omno) {
        this.omno = omno;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
