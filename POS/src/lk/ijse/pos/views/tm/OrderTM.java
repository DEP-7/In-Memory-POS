package lk.ijse.pos.views.tm;

import lk.ijse.pos.model.ItemDetails;

import java.util.ArrayList;

public class OrderTM {
    private String orderId;
    private String orderDate;
    private String customerId;
    private double totalCost;

    public OrderTM() {
    }

    public OrderTM(String orderId, String orderDate, String customerId, double totalCost) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.totalCost = totalCost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
