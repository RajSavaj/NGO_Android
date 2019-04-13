package org.foodmanoeuvre.modal;

import com.google.gson.annotations.SerializedName;

public class FoodItem {
    @SerializedName("Id")
    private int Id;
    @SerializedName("R_id")
    private int R_id;
    @SerializedName("Time")
    private String Time;
    @SerializedName("Qty")
    private float Qty;
    @SerializedName("Image")
    private String Image;
    @SerializedName("Name")
    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getR_id() {
        return R_id;
    }

    public void setR_id(int r_id) {
        R_id = r_id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public float getQty() {
        return Qty;
    }

    public void setQty(float qty) {
        Qty = qty;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
