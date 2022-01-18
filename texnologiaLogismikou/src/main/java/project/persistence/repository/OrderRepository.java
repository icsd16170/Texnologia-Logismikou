package project.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.persistence.entity.OrderEntity;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerUserName(String customerUserName);

    @Query("SELECT oder FROM OrderEntity oder "
            + "WHERE (oder.customerUserName = :customerUserName OR :customerUserName = '')"
            + "AND (lower(oder.status) = lower(:status) OR :status = '')"
            + "AND oder.createdDate >=  CAST(:createdDate AS date) "
            + "AND (lower(oder.address) LIKE lower(:address) OR :address = '')")
    List<OrderEntity> search(@Param("customerUserName") String customerUserName,
            @Param("status") String status,
            @Param("createdDate") LocalDateTime createdDate,
            @Param("address") String address);

    List<OrderEntity> findByStatusAndCustomerUserName(String status, String customerUserName);

    List<OrderEntity> findByStatus(String status);

}
