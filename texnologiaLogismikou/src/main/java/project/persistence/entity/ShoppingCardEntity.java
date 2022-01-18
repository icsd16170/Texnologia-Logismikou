package project.persistence.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import project.errorhandling.validation.order.customername.ValidCustomerUserName;

@Entity
public class ShoppingCardEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String customerUserName;

    @Column(nullable = false)
    @ElementCollection
    private Map<Long,Integer> dvd = new HashMap<>();

    @Column
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column
    private double totalCost;

    @Column
    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerUserName() {
        return customerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        this.customerUserName = customerUserName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Map<Long, Integer> getDvd() {
        return dvd;
    }

    public void setDvd(Map<Long, Integer> dvd) {
        this.dvd = dvd;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ShoppingCardEntity that = (ShoppingCardEntity) o;
        return Double.compare(that.totalCost, totalCost) == 0 && Objects.equals(id, that.id) && Objects.equals(customerUserName,
                that.customerUserName) && Objects.equals(dvd, that.dvd) && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerUserName, dvd, createdDate, totalCost, status);
    }
}
