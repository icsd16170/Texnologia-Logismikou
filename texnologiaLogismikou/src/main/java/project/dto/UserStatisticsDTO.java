package project.dto;

import java.util.HashMap;
import java.util.Map;


public class UserStatisticsDTO {

    private String userName;

    private int ordersCompleted;

    private int ordersCanceled;

    private Map<String, Integer> dvdsPerCategories = new HashMap<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOrdersCompleted() {
        return ordersCompleted;
    }

    public void setOrdersCompleted(int ordersCompleted) {
        this.ordersCompleted = ordersCompleted;
    }

    public int getOrdersCanceled() {
        return ordersCanceled;
    }

    public void setOrdersCanceled(int ordersCanceled) {
        this.ordersCanceled = ordersCanceled;
    }

    public Map<String, Integer> getDvdsPerCategories() {
        return dvdsPerCategories;
    }

    public void setDvdsPerCategories(Map<String, Integer> dvdsPerCategories) {
        this.dvdsPerCategories = dvdsPerCategories;
    }
}
