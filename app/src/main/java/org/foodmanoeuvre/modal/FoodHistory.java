package org.foodmanoeuvre.modal;

import com.google.gson.annotations.SerializedName;

public class FoodHistory {
    @SerializedName("Id")
    private int Id;
    @SerializedName("Name")
    private String Name;
    @SerializedName("ngoname")
    private String Ngoname;
    @SerializedName("Qty")
    private int Qty;
    @SerializedName("Image")
    private String Image;
    @SerializedName("Status")
    private int status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNgoname() {
        return Ngoname;
    }

    public void setNgoname(String ngoname) {
        Ngoname = ngoname;
    }
}
