package project.service.order;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.dto.DVDDTO;
import project.dto.OrderDTO;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.MissingFieldException;
import project.errorhandling.exception.OrderNotFoundException;
import project.persistence.entity.OrderEntity;
import project.persistence.entity.ShoppingCardEntity;
import project.persistence.repository.OrderRepository;
import project.persistence.repository.ShoppingCardRepository;
import project.service.dvd.DVDService;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, 1, 1, 0, 0);

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DVDService dvdService;

    @Mock
    private ShoppingCardRepository shoppingCardRepository;

    @InjectMocks
    private OrderServiceImpl testee;

    @Test
    void create_returns_savedEntity() {
        OrderDTO expected = getOrderDTO();
        Mockito.when(orderRepository.save(any())).thenReturn(getOrderEntity());
        OrderDTO actual = testee.create(getOrderDTO());
        makeCommonAssertions(expected, actual);
    }

    @Test
    void findById_orderNotExists_throwsOrderNotFoundException() {
        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(OrderNotFoundException.class, () -> testee.findById(1L));
    }


    @Test
    void findById_orderExists_returnsEntity() {
        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(getOrderEntity()));
        OrderDTO actual = testee.findById(1L);
        OrderDTO expected = getOrderDTO();
        makeCommonAssertions(expected, actual);
    }


    @Test
    void findByUserName_noRecords_returnsEmptyList() {
        Mockito.when(orderRepository.findByCustomerUserName(any())).thenReturn(Collections.emptyList());
        List<OrderDTO> actual = testee.findByUserName("username");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByUserName_ordersFound_returnsOrderEntities() {
        Mockito.when(orderRepository.findByCustomerUserName(any())).thenReturn(List.of(getOrderEntity()));
        List<OrderDTO> actual = testee.findByUserName("username");
        Assertions.assertEquals(1, actual.size());
        makeCommonAssertions(getOrderDTO(), actual.get(0));
    }

    @Test
    void search_noParameters_returnsEntities() {
        Mockito.when(orderRepository.search("", "", LocalDate.of(1800, 1, 1).atStartOfDay(), "")).thenReturn(List.of(getOrderEntity()));
        List<OrderDTO> actual = testee.search(null, null, null, null);

        Mockito.verify(orderRepository).search("", "", LocalDate.of(1800, 1, 1).atStartOfDay(), "");
        Assertions.assertEquals(1, actual.size());
        makeCommonAssertions(getOrderDTO(), actual.get(0));
    }

    @Test
    void search_parameters_returnsEntities() {
        Mockito.when(orderRepository.search("user", "status", LocalDate.of(2021, 1, 1).atStartOfDay(), "%address%")).thenReturn(List.of(getOrderEntity()));
        List<OrderDTO> actual = testee.search("user", "status", LocalDate.of(2021, 1, 1), "address");

        Mockito.verify(orderRepository).search("user", "status", LocalDate.of(2021, 1, 1).atStartOfDay(), "%address%");
        Assertions.assertEquals(1, actual.size());
        makeCommonAssertions(getOrderDTO(), actual.get(0));
    }

    @Test
    void delete_orderDoesNotExists_throwsOrderNotFoundException() {
        Mockito.when(orderRepository.existsById(any())).thenReturn(false);
        Assertions.assertThrows(OrderNotFoundException.class, () -> testee.delete(1L, "cancelReason"));
    }

    @Test
    void delete_statusNotCreated_throwsInvalidStatusException() {
        Mockito.when(orderRepository.existsById(any())).thenReturn(true);
        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(getOrderEntity()));

        Assertions.assertThrows(InvalidStatusException.class, () -> testee.delete(1L, "cancelReason"));
    }

    @Test
    void delete_validCase_orderAndShoppingCardCanceled() {
        Mockito.when(orderRepository.existsById(any())).thenReturn(true);
        OrderEntity canceledOrderEntity = getOrderEntity();
        canceledOrderEntity.setStatus("CREATED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(canceledOrderEntity));
        Mockito.when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(new ShoppingCardEntity()));

        testee.delete(1L, "cancelReason");

        Mockito.verify(orderRepository).save(Mockito.any());
        Mockito.verify(shoppingCardRepository).save(Mockito.any());
    }

    @Test
    void update_beforeUpdateNoCreatedStatusWhenCharged_throwsInvalidStatusException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("CHARGED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        InvalidStatusException invalidStatusException = Assertions.assertThrows(InvalidStatusException.class, () -> testee.update(orderDTO, 1L));
        String message = invalidStatusException.getMessage();
        Assertions.assertEquals("Invalid Status. Only Order with status CREATED can change to CHARGED ", message);
    }

    @Test
    void update_beforeUpdateNoCreatedStatusWhenCancelled_throwsInvalidStatusException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("CANCELLED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        InvalidStatusException invalidStatusException = Assertions.assertThrows(InvalidStatusException.class, () -> testee.update(orderDTO, 1L));
        String message = invalidStatusException.getMessage();
        Assertions.assertEquals("Invalid Status. Only Order with status CREATED can change to CANCELLED ", message);
    }

    @Test
    void update_beforeUpdateNoChargedStatusWhenPending_throwsInvalidStatusException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("PENDING");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        InvalidStatusException invalidStatusException = Assertions.assertThrows(InvalidStatusException.class, () -> testee.update(orderDTO, 1L));
        String message = invalidStatusException.getMessage();
        Assertions.assertEquals("Invalid Status. Only Order with status CHARGED can change to PENDING ", message);
    }

    @Test
    void update_beforeUpdateNoChargedStatusWhenCompleted_throwsInvalidStatusException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("COMPLETED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        InvalidStatusException invalidStatusException = Assertions.assertThrows(InvalidStatusException.class, () -> testee.update(orderDTO, 1L));
        String message = invalidStatusException.getMessage();
        Assertions.assertEquals("Invalid Status. Only Order with status CHARGED can change to COMPLETED ", message);
    }

    @Test
    void update_updateToCancelMissingCancelReason_throwsMissingFieldException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        orderBeforeUpdate.setStatus("CREATED");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("CANCELLED");
        orderDTO.setCancelledReason(null);

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        MissingFieldException missingFieldException = Assertions.assertThrows(MissingFieldException.class, () -> testee.update(orderDTO, 1L));

        Assertions.assertEquals("Provide cancelReason", missingFieldException.getMessage());
    }

    @Test
    void update_updateToPendingMissingPendingReason_throwsMissingFieldException() {
        OrderEntity orderBeforeUpdate = getOrderEntity();
        orderBeforeUpdate.setStatus("CHARGED");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus("PENDING");
        orderDTO.setCancelledReason(null);

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));

        MissingFieldException missingFieldException = Assertions.assertThrows(MissingFieldException.class, () -> testee.update(orderDTO, 1L));

        Assertions.assertEquals("Provide pendingReason", missingFieldException.getMessage());
    }

    @Test
    void update_chargeOrder_seizeDvds() {
        DVDDTO dvd = getDVDdto();

        DVDDTO seizedDVD = getDVDdto();
        seizedDVD.setQuantityAvailable(9);

        OrderEntity orderBeforeUpdate = getOrderEntity();
        orderBeforeUpdate.setStatus("CREATED");

        OrderDTO orderDTO = getOrderDTO();
        orderDTO.setStatus("CHARGED");

        OrderDTO expected = getOrderDTO();
        expected.setStatus("CHARGED");

        OrderEntity orderEntity = getOrderEntity();
        orderEntity.setStatus("CHARGED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));
        Mockito.when(orderRepository.save(any())).thenReturn(orderEntity);
        Mockito.when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(getShoppingCardEntity()));
        Mockito.when(dvdService.findById(any())).thenReturn(dvd);

        OrderDTO actual = testee.update(orderDTO, 1L);

        makeCommonAssertions(expected, actual);
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Mockito.verify(dvdService).findById(1L);
        Mockito.verify(dvdService).update(any(), any());

    }

    @Test
    void update_completedOrder_completesShoppingCard() {

        OrderEntity orderBeforeUpdate = getOrderEntity();
        orderBeforeUpdate.setStatus("CHARGED");

        OrderDTO orderDTO = getOrderDTO();
        orderDTO.setStatus("COMPLETED");

        OrderDTO expected = getOrderDTO();
        expected.setStatus("COMPLETED");

        OrderEntity orderEntity = getOrderEntity();
        orderEntity.setStatus("COMPLETED");

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(orderBeforeUpdate));
        Mockito.when(orderRepository.save(any())).thenReturn(orderEntity);
        Mockito.when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(getShoppingCardEntity()));

        OrderDTO actual = testee.update(orderDTO, 1L);

        makeCommonAssertions(expected, actual);
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Mockito.verify(shoppingCardRepository).save(any());

    }


    @Test
    void findByStatusAndUserName() {
    }

    @Test
    void findByStatus() {
    }

    private OrderDTO getOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus("status");
        orderDTO.setCancelledReason("cancelReason");
        orderDTO.setAddress("address");
        orderDTO.setCompletionDate(LOCAL_DATE_TIME);
        orderDTO.setCustomerUserName("customerUserName");
        orderDTO.setShoppingCardId(1L);
        orderDTO.setPendingReason("pendingReason");
        orderDTO.setCreatedDate(LOCAL_DATE_TIME);
        return orderDTO;
    }

    private OrderEntity getOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setStatus("status");
        orderEntity.setCancelledReason("cancelReason");
        orderEntity.setAddress("address");
        orderEntity.setCompletionDate(LOCAL_DATE_TIME);
        orderEntity.setCustomerUserName("customerUserName");
        orderEntity.setShoppingCardId(1L);
        orderEntity.setPendingReason("pendingReason");
        orderEntity.setCreatedDate(LOCAL_DATE_TIME);
        return orderEntity;
    }

    private void makeCommonAssertions(OrderDTO expected, OrderDTO actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCancelledReason(), actual.getCancelledReason());
        Assertions.assertEquals(expected.getAddress(), actual.getAddress());
        Assertions.assertEquals(expected.getCompletionDate(), actual.getCompletionDate());
        Assertions.assertEquals(expected.getCustomerUserName(), actual.getCustomerUserName());
        Assertions.assertEquals(expected.getShoppingCardId(), actual.getShoppingCardId());
        Assertions.assertEquals(expected.getPendingReason(), actual.getPendingReason());
        Assertions.assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
    }

    private ShoppingCardEntity getShoppingCardEntity() {
        ShoppingCardEntity shoppingCardEntity = new ShoppingCardEntity();

        Map<Long, Integer> dvds = new HashMap<>();
        dvds.put(1L, 1);
        shoppingCardEntity.setDvd(dvds);

        return shoppingCardEntity;
    }

    private DVDDTO getDVDdto() {
        DVDDTO dvddto = new DVDDTO();
        dvddto.setQuantityAvailable(10);
        return dvddto;
    }
}