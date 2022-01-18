package project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;


public class ShoppingCardDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    private String customerUserName;

    @NotNull(message = "must not be null")
    private Map<Long, Integer> dvd = new HashMap<>();

    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdDate;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private double totalCost;

    @Schema(accessMode = AccessMode.READ_ONLY)
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

    public Map<Long, Integer> getDvd() {
        return dvd;
    }

    public void setDvd(Map<Long, Integer> dvd) {
        this.dvd = dvd;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
        ShoppingCardDTO that = (ShoppingCardDTO) o;
        return Double.compare(that.totalCost, totalCost) == 0 && Objects.equals(id, that.id) && Objects.equals(customerUserName,
                that.customerUserName) && Objects.equals(dvd, that.dvd) && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerUserName, dvd, createdDate, totalCost, status);
    }
}
