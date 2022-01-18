package project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import project.errorhandling.validation.OnUpdate;
import project.errorhandling.validation.order.customername.ValidCustomerUserName;
import project.errorhandling.validation.order.shoppingcard.ValidShoppingCardId;
import project.errorhandling.validation.order.status.ValidOrderStatus;


public class OrderDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    @ValidShoppingCardId
    private Long shoppingCardId;

    @NotNull(message = "must not be null")
    @ValidCustomerUserName
    private String customerUserName;

    @NotNull(message = "must not be null")
    @ValidOrderStatus(groups = OnUpdate.class)
    private String status;

    @NotNull(message = "must not be null")
    private String address;

    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdDate;

    private LocalDateTime completionDate;

    private String pendingReason;

    private String cancelledReason;

    public Long getShoppingCardId() {
        return shoppingCardId;
    }

    public void setShoppingCardId(Long shoppingCardId) {
        this.shoppingCardId = shoppingCardId;
    }

    public String getCustomerUserName() {
        return customerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        this.customerUserName = customerUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public String getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
