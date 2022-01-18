package project.service.order;



import static project.service.Constants.CANCELLED;
import static project.service.Constants.CHARGED;
import static project.service.Constants.COMPLETED;
import static project.service.Constants.CREATED;
import static project.service.Constants.PENDING;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.DVDDTO;
import project.dto.OrderDTO;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.MissingFieldException;
import project.errorhandling.exception.OrderNotFoundException;
import project.mapper.OrderMapper;
import project.persistence.entity.OrderEntity;
import project.persistence.entity.ShoppingCardEntity;
import project.persistence.repository.OrderRepository;
import project.persistence.repository.ShoppingCardRepository;
import project.service.dvd.DVDService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final DVDService dvdService;

    private final ShoppingCardRepository shoppingCardRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, DVDService dvdService, ShoppingCardRepository shoppingCardRepository) {
        this.orderRepository = orderRepository;
        this.dvdService = dvdService;
        this.shoppingCardRepository = shoppingCardRepository;
    }

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        OrderEntity orderEntity = OrderMapper.mapDTOToEntity(orderDTO);
        orderEntity.setStatus(CREATED);
        return OrderMapper.mapEntityToDTO(orderRepository.save(orderEntity));
    }

    @Override
    public OrderDTO findById(Long id) {
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(id);
        if (orderEntityOptional.isPresent()) {
            return OrderMapper.mapEntityToDTO(orderEntityOptional.get());
        }
        throw new OrderNotFoundException();
    }

    @Override
    public List<OrderDTO> findByUserName(String customerUserName) {
        List<OrderEntity> orderEntityList = orderRepository.findByCustomerUserName(customerUserName);
        return OrderMapper.mapEntityListToDTOList(orderEntityList);
    }

    @Override
    public List<OrderDTO> search(String customerUserName, String status, LocalDate createdDate, String address) {
        LocalDateTime createdLocalDateTime = null;
        LocalDateTime completionDateTime = null;

        if (customerUserName == null) {
            customerUserName = "";
        }
        if (address != null) {
            address = "%" + address + "%";
        } else {
            address = "";
        }
        if (status == null) {
            status = "";
        }
        if (createdDate != null) {
            createdLocalDateTime = createdDate.atStartOfDay();
        } else {
            createdLocalDateTime = LocalDate.of(1800, 1, 1).atStartOfDay();
        }

        List<OrderEntity> orderEntities = orderRepository.search(customerUserName, status, createdLocalDateTime, address);
        return OrderMapper.mapEntityListToDTOList(orderEntities);
    }

    @Override
    @Transactional
    public void delete(Long id, String cancelReason) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
        OrderDTO order = findById(id);
        if (!CREATED.equals(order.getStatus())) {
            throw new InvalidStatusException(order.getStatus(), CREATED, "Order");
        }

        order.setStatus(CANCELLED);
        order.setCancelledReason(cancelReason);

        OrderEntity orderEntity = OrderMapper.mapDTOToEntity(order);
        orderEntity.setId(id);
        orderRepository.save(orderEntity);

        cancelShoppingCard(orderEntity.getShoppingCardId());
    }



    @Override
    @Transactional
    public OrderDTO update(OrderDTO orderDTO, Long orderId) {
        OrderDTO orderBeforeUpdate = findById(orderId);

        if ((CHARGED.equals(orderDTO.getStatus()) || CANCELLED.equals(orderDTO.getStatus())) && !CREATED.equals(orderBeforeUpdate.getStatus())) {
            throw new InvalidStatusException(orderDTO.getStatus(), CREATED, "Order");
        }
        if ((PENDING.equals(orderDTO.getStatus()) || COMPLETED.equals(orderDTO.getStatus())) && !CHARGED.equals(orderBeforeUpdate.getStatus())) {
            throw new InvalidStatusException(orderDTO.getStatus(), CHARGED, "Order");
        }

        if (CANCELLED.equals(orderDTO.getStatus()) && orderDTO.getCancelledReason() == null || "".equals(orderDTO.getCancelledReason())) {
            throw new MissingFieldException("cancelReason");
        }
        if (PENDING.equals(orderDTO.getStatus()) && orderDTO.getPendingReason() == null || "".equals(orderDTO.getPendingReason())) {
            throw new MissingFieldException("pendingReason");
        }

        orderDTO.setId(orderBeforeUpdate.getId());
        OrderEntity updatedEntity = orderRepository.save(OrderMapper.mapDTOToEntity(orderDTO));
        if (CHARGED.equals(updatedEntity.getStatus())) {
            seizeDvds(updatedEntity);
        }
        if (COMPLETED.equals(updatedEntity.getStatus())) {
            completeShoppingCard(updatedEntity.getShoppingCardId());
        }

        return OrderMapper.mapEntityToDTO(updatedEntity);
    }

    @Override
    public List<OrderDTO> findByStatusAndUserName(String status, String customerUsername) {
        return OrderMapper.mapEntityListToDTOList(orderRepository.findByStatusAndCustomerUserName(status, customerUsername));
    }

    @Override
    public List<OrderDTO> findByStatus(String status) {
        return OrderMapper.mapEntityListToDTOList(orderRepository.findByStatus(status));
    }

    private void seizeDvds(OrderEntity updatedEntity) {
        Optional<ShoppingCardEntity> shoppingCard = shoppingCardRepository.findById(updatedEntity.getShoppingCardId());

        shoppingCard.ifPresent(shoppingCardEntity -> shoppingCardEntity.getDvd().forEach((dvdId, quantity) -> {
            DVDDTO dvd = dvdService.findById(dvdId);
            int quantityAvailable = dvd.getQuantityAvailable() - quantity;
            dvd.setQuantityAvailable(quantityAvailable);
            dvdService.update(dvdId, dvd);
        }));

    }

    private void cancelShoppingCard(Long shoppingCardId) {
        Optional<ShoppingCardEntity> shoppingCardOptional = shoppingCardRepository.findById(shoppingCardId);
        if (shoppingCardOptional.isPresent()) {
            shoppingCardOptional.get().setStatus(CANCELLED);
            shoppingCardRepository.save(shoppingCardOptional.get());
        }
    }

    private void completeShoppingCard(Long shoppingCardId) {
        Optional<ShoppingCardEntity> shoppingCardOptional = shoppingCardRepository.findById(shoppingCardId);
        if (shoppingCardOptional.isPresent()) {
            shoppingCardOptional.get().setStatus(COMPLETED);
            shoppingCardRepository.save(shoppingCardOptional.get());
        }
    }
}
