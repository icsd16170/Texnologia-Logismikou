package project.service.order;

import java.time.LocalDate;
import java.util.List;
import project.dto.OrderDTO;

public interface OrderService {

    OrderDTO create(OrderDTO orderDTO);

    OrderDTO findById(Long id);

    List<OrderDTO> search(String customerUserName, String status, LocalDate createdDate, String address);

    void delete(Long id, String cancelReason);

    OrderDTO update(OrderDTO orderDTO, Long orderId);

    List<OrderDTO> findByUserName(String customerUsername);

    List<OrderDTO> findByStatusAndUserName(String status, String customerUsername);

    List<OrderDTO> findByStatus(String status);
}