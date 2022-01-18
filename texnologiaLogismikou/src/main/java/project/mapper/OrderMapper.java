package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.OrderDTO;
import project.persistence.entity.OrderEntity;

public class OrderMapper {


    public static OrderDTO mapEntityToDTO(OrderEntity orderEntity) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setShoppingCardId(orderEntity.getShoppingCardId());
        orderDTO.setCustomerUserName(orderEntity.getCustomerUserName());
        orderDTO.setStatus(orderEntity.getStatus());
        orderDTO.setCreatedDate(orderEntity.getCreatedDate());
        orderDTO.setCancelledReason(orderEntity.getCancelledReason());
        orderDTO.setAddress(orderEntity.getAddress());
        orderDTO.setPendingReason(orderEntity.getPendingReason());
        orderDTO.setCancelledReason(orderEntity.getCancelledReason());
        orderDTO.setCompletionDate(orderEntity.getCompletionDate());
        orderDTO.setId(orderEntity.getId());

        return orderDTO;
    }

    public static OrderEntity mapDTOToEntity(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setShoppingCardId(orderDTO.getShoppingCardId());
        orderEntity.setCustomerUserName(orderDTO.getCustomerUserName());
        orderEntity.setStatus(orderDTO.getStatus());
        orderEntity.setCreatedDate(orderDTO.getCreatedDate());
        orderEntity.setCancelledReason(orderDTO.getCancelledReason());
        orderEntity.setAddress(orderDTO.getAddress());
        orderEntity.setPendingReason(orderDTO.getPendingReason());
        orderEntity.setCancelledReason(orderDTO.getCancelledReason());
        orderEntity.setCompletionDate(orderDTO.getCompletionDate());
        orderEntity.setId(orderDTO.getId());

        return orderEntity;
    }

    public static List<OrderDTO> mapEntityListToDTOList(List<OrderEntity> orderEntities) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        orderEntities.forEach(orderEntity -> orderDTOs.add(mapEntityToDTO(orderEntity)));
        return orderDTOs;
    }
}
