package project.service.shoppingCard;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.DVDDTO;
import project.dto.OrderDTO;
import project.dto.ShoppingCardDTO;
import project.dto.UserDTO;
import project.errorhandling.exception.InvalidQuantiyException;
import project.errorhandling.exception.InvalidStatusException;
import project.errorhandling.exception.ShoppingCardIsNotActiveException;
import project.errorhandling.exception.ShoppingCardNotFoundException;
import project.mapper.OrderMapper;
import project.mapper.ShoppingCardMapper;
import project.persistence.entity.ShoppingCardEntity;
import project.persistence.repository.OrderRepository;
import project.persistence.repository.ShoppingCardRepository;
import project.service.Constants;
import project.service.dvd.DVDService;
import project.service.user.UserService;

@Service
public class ShoppingCardServiceImpl implements ShoppingCardService {

    private final ShoppingCardRepository shoppingCardRepository;

    private final DVDService dvdService;

    private final OrderRepository orderRepository;

    private final UserService userService;


    @Autowired
    public ShoppingCardServiceImpl(ShoppingCardRepository shoppingCardRepository, DVDService dvdService, OrderRepository orderRepository,
            UserService userService) {
        this.shoppingCardRepository = shoppingCardRepository;
        this.dvdService = dvdService;
        this.orderRepository = orderRepository;
        this.userService = userService;
    }



    @Override
    public ShoppingCardDTO create(ShoppingCardDTO shoppingCardDTO) {
        checkDVDs(shoppingCardDTO);
        ShoppingCardEntity shoppingCardEntity = ShoppingCardMapper.mapDTOToEntity(shoppingCardDTO);
        shoppingCardEntity.setStatus(Constants.ACTIVE);
        shoppingCardEntity.setTotalCost(getTotalCost(shoppingCardDTO));
        return ShoppingCardMapper.mapEntityToDTO(shoppingCardRepository.save(shoppingCardEntity));
    }

    private int getTotalCost(ShoppingCardDTO shoppingCardDTO) {
        int cost = 0;
        for (Long key : shoppingCardDTO.getDvd().keySet()) {
            DVDDTO dvddto = dvdService.findById(key);
            cost += dvddto.getCost() * shoppingCardDTO.getDvd().get(key);

        }
        return cost;
    }

    private void checkDVDs(ShoppingCardDTO shoppingCardDTO) {
        shoppingCardDTO.getDvd().forEach((dvdId, quantity) -> {
            DVDDTO dvd = dvdService.findById(dvdId);
            if (dvd.getQuantityAvailable() < quantity) {
                throw new InvalidQuantiyException(dvdId);
            }
        });
    }

    @Override
    public void delete(Long id) {
        ShoppingCardDTO shoppingCardDTO = findById(id);

        if (Constants.ACTIVE.equals(shoppingCardDTO.getStatus())) {
            shoppingCardRepository.delete(ShoppingCardMapper.mapDTOToEntity(shoppingCardDTO));
        } else {
            throw new ShoppingCardIsNotActiveException(id);
        }


    }

    @Override
    public boolean existsById(Long id) {
        return shoppingCardRepository.existsById(id);
    }

    @Override
    public ShoppingCardDTO findById(Long id) {
        Optional<ShoppingCardEntity> shoppingCardEntity = shoppingCardRepository.findById(id);
        if (shoppingCardEntity.isPresent()) {
            return ShoppingCardMapper.mapEntityToDTO(shoppingCardEntity.get());
        }
        throw new ShoppingCardNotFoundException(id);
    }

    @Override
    public List<ShoppingCardDTO> findByUserName(String customerUserName) {
        List<ShoppingCardEntity> shoppingCardEntityList = shoppingCardRepository.findByCustomerUserName(customerUserName);
        List<ShoppingCardDTO> shoppingCardDTOList = ShoppingCardMapper.mapEntityListToDTOList(shoppingCardEntityList);
        return getOrderedShoppingCardDTOS(shoppingCardDTOList);
    }

    @Override
    @Transactional
    public void submit(Long id) {
        ShoppingCardDTO shoppingCardDTO = findById(id);
        shoppingCardDTO.setStatus(Constants.PENDING);
        ShoppingCardEntity shoppingCardEntity = ShoppingCardMapper.mapDTOToEntity(shoppingCardDTO);
        shoppingCardEntity.setId(id);
        shoppingCardRepository.save(shoppingCardEntity);
        OrderDTO orderDTO = getOrderDTO(id, shoppingCardDTO);
        orderRepository.save(OrderMapper.mapDTOToEntity(orderDTO));

    }

    @Override
    public ShoppingCardDTO update(ShoppingCardDTO shoppingCardDTO, Long id) {
        ShoppingCardDTO oldShoppingCard = findById(id);
        if (Constants.ACTIVE.equals(oldShoppingCard.getStatus())) {
            checkDVDs(shoppingCardDTO);
            ShoppingCardEntity shoppingCardEntity = ShoppingCardMapper.mapDTOToEntity(shoppingCardDTO);
            shoppingCardEntity.setStatus(Constants.ACTIVE);
            shoppingCardEntity.setCreatedDate(LocalDateTime.now());
            shoppingCardEntity.setTotalCost(getTotalCost(shoppingCardDTO));
            shoppingCardEntity.setId(id);
            return ShoppingCardMapper.mapEntityToDTO(shoppingCardRepository.save(shoppingCardEntity));
        }
        throw new InvalidStatusException();
    }

    @Override
    public List<ShoppingCardDTO> findCompletedShoppingCards() {
        return ShoppingCardMapper.mapEntityListToDTOList(shoppingCardRepository.findByStatus(Constants.COMPLETED));
    }

    private OrderDTO getOrderDTO(long id, ShoppingCardDTO shoppingCardDTO) {
        UserDTO userDTO = userService.findByUserName(shoppingCardDTO.getCustomerUserName());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setShoppingCardId(id);
        orderDTO.setCustomerUserName(shoppingCardDTO.getCustomerUserName());
        orderDTO.setAddress(userDTO.getAddress());
        orderDTO.setStatus(Constants.CREATED);
        return orderDTO;
    }

    private List<ShoppingCardDTO> getOrderedShoppingCardDTOS(List<ShoppingCardDTO> shoppingCardDTOList) {
        List<ShoppingCardDTO> activeShoppingCardList =
                shoppingCardDTOList.stream().filter(shoppingCardDTO -> Constants.ACTIVE.equals(shoppingCardDTO.getStatus())).collect(
                        Collectors.toList());
        List<ShoppingCardDTO> pendingShoppingCardList =
                shoppingCardDTOList.stream().filter(shoppingCardDTO -> Constants.PENDING.equals(shoppingCardDTO.getStatus())).collect(
                        Collectors.toList());
        List<ShoppingCardDTO> cancelledShoppingCardList =
                shoppingCardDTOList.stream().filter(shoppingCardDTO -> Constants.CANCELLED.equals(shoppingCardDTO.getStatus())).collect(
                        Collectors.toList());
        List<ShoppingCardDTO> completedShoppingCardList =
                shoppingCardDTOList.stream().filter(shoppingCardDTO -> Constants.COMPLETED.equals(shoppingCardDTO.getStatus())).collect(
                        Collectors.toList());
        List<ShoppingCardDTO> orderedShoppingCardList = new ArrayList<>();
        orderedShoppingCardList.addAll(activeShoppingCardList);
        orderedShoppingCardList.addAll(pendingShoppingCardList);
        orderedShoppingCardList.addAll(cancelledShoppingCardList);
        orderedShoppingCardList.addAll(completedShoppingCardList);
        return orderedShoppingCardList;
    }


}
