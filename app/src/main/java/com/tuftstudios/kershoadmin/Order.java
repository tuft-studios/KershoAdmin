package com.tuftstudios.kershoadmin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {


    @SerializedName("order_dishes")
    private ArrayList<Dish> dishesList;


    private int orderId, userId, addressId, status, eta;
    private double subtotal, delivery, total, discount;
    private String location, schedule, orderTime, creationTime, phone, fullAddress, addressName,
            dishes, count, options, sizes, sides1, sides2, comment, area, street, landMark, floor, apartmentNumber, buildingNumber;
    private float latitude, longitude;

    public Order(int orderId, ArrayList<Dish> dishesList, int status, double subtotal, double delivery, double total, double discount,
                 String phone, String orderTime, String creationTime, String fullAddress, String addressName,
                 String comment, int eta, String area, String street, String landMark, String floor, String apartmentNumber, String buildingNumber, float latitude, float longitude) {
        this.dishesList = dishesList;
        this.orderId = orderId;
        this.status = status;
        this.subtotal = subtotal;
        this.delivery = delivery;
        this.total = total;
        this.discount = discount;
        this.phone = phone;
        this.orderTime = orderTime;
        this.creationTime = creationTime;
        this.fullAddress = fullAddress;
        this.addressName = addressName;
        this.comment = comment;
        this.eta = eta;
        this.area = area;
        this.street = street;
        this.landMark = landMark;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.buildingNumber = buildingNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Order(String dishes, String count, String options, String sizes, String sides1, String sides2,
                 double subtotal, double delivery, double total, double discount, int userId, String location,
                 int addressId, String phone, String orderTime, String creationTime, int eta, String comment) {
        this.dishes = dishes;
        this.count = count;
        this.options = options;
        this.sizes = sizes;
        this.sides1 = sides1;
        this.sides2 = sides2;
        this.subtotal = subtotal;
        this.delivery = delivery;
        this.total = total;
        this.discount = discount;
        this.userId = userId;
        this.location = location;
        this.addressId = addressId;
        this.phone = phone;
        this.orderTime = orderTime;
        this.creationTime = creationTime;
        this.eta = eta;
        this.comment = comment;
    }

    public String getSchedule() {


        return schedule;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public String getDishes() {

        return dishes;
    }

    public String getCount() {
        return count;
    }

    public double getSubtotalPrice() {
        return subtotal;
    }

    public double getDelivery() {
        return delivery;
    }

    public double getTotalPrice() {
        return total;
    }

    public double getDiscount() {
        return discount;
    }

    public String getLocation() {
        return location;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {

        return userId;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setCreationTime(String creationTime) {
        creationTime = creationTime;
    }

    public String getCreationTime() {

        return creationTime;
    }

    public String getMobile() {
        return phone;
    }

    public ArrayList<Dish> getDishesList() {
        return dishesList;
    }

    public String getFullAddress() {
        return fullAddress;
    }


    public int getId() {
        return orderId;
    }


    public int getStatus() {
        return status;
    }


    public String getOptions() {
        return options;
    }

    public String getSizes() {
        return sizes;
    }

    public String getSides1() {
        return sides1;
    }

    public String getSides2() {
        return sides2;
    }

    public String getComment() {
        return comment;
    }

    public int getEta() {
        return eta;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getArea() {
        return area;
    }

    public String getStreet() {
        return street;
    }

    public String getLandMark() {
        return landMark;
    }

    public String getFloor() {
        return floor;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}


