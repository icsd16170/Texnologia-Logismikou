package project.service.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import project.dto.DVDDTO;
import project.dto.OrderDTO;
import project.dto.ShoppingCardDTO;
import project.dto.SystemStatisticsDTO;
import project.dto.UserDTO;
import project.dto.UserStatisticsDTO;
import project.service.Constants;
import project.service.dvd.DVDService;
import project.service.order.OrderService;
import project.service.shoppingCard.ShoppingCardService;
import project.service.user.UserService;

@Service
public class OrderStatisticsServiceImpl implements OrderStatisticsService {

    private final UserService userService;

    private final OrderService orderService;

    private final DVDService dvdService;

    private final ShoppingCardService shoppingCardService;

    public OrderStatisticsServiceImpl(UserService userService, OrderService orderService, DVDService dvdService,
            ShoppingCardService shoppingCardService) {
        this.userService = userService;
        this.orderService = orderService;
        this.dvdService = dvdService;
        this.shoppingCardService = shoppingCardService;
    }

    @Override
    public UserStatisticsDTO findStatisticsForUser(String userName) {
        UserDTO user = userService.findByUserName(userName);
        List<OrderDTO> completedOrders = orderService.findByStatusAndUserName(Constants.COMPLETED, userName);
        List<OrderDTO> cancelledOrders = orderService.findByStatusAndUserName(Constants.CANCELLED, userName);

        Map<String, Integer> dvdsPerCategory = new HashMap<>();

        List<ShoppingCardDTO> userCompletedShoppingCards =
                shoppingCardService.findByUserName(userName).stream()
                                   .filter(shoppingCard -> Constants.COMPLETED.equals(shoppingCard.getStatus()))
                                   .collect(Collectors.toList());
        for (ShoppingCardDTO userCompletedShoppingCard : userCompletedShoppingCards) {
            userCompletedShoppingCard.getDvd().forEach((dvdId, quantity) -> {
                DVDDTO dvd = dvdService.findById(dvdId);
                if (dvdsPerCategory.containsKey(dvd.getType())) {
                    dvdsPerCategory.put(dvd.getType(), dvdsPerCategory.get(dvd.getType()) + quantity);
                } else {
                    dvdsPerCategory.put(dvd.getType(), quantity);
                }
            });
        }

        UserStatisticsDTO userStatisticsDTO = new UserStatisticsDTO();
        userStatisticsDTO.setUserName(userName);
        userStatisticsDTO.setOrdersCanceled(cancelledOrders.size());
        userStatisticsDTO.setOrdersCompleted(completedOrders.size());
        userStatisticsDTO.setDvdsPerCategories(dvdsPerCategory);
        return userStatisticsDTO;
    }

    @Override
    public SystemStatisticsDTO findSystemStatistics() {
        List<OrderDTO> completedOrders = orderService.findByStatus(Constants.COMPLETED);
        List<OrderDTO> cancelledOrders = orderService.findByStatus(Constants.CANCELLED);

        Map<String, Integer> dvdsPerCategory = new HashMap<>();

        List<ShoppingCardDTO> userCompletedShoppingCards = shoppingCardService.findCompletedShoppingCards();

        for (ShoppingCardDTO userCompletedShoppingCard : userCompletedShoppingCards) {
            userCompletedShoppingCard.getDvd().forEach((dvdId, quantity) -> {
                DVDDTO dvd = dvdService.findById(dvdId);
                if (dvdsPerCategory.containsKey(dvd.getType())) {
                    dvdsPerCategory.put(dvd.getType(), dvdsPerCategory.get(dvd.getType()) + quantity);
                } else {
                    dvdsPerCategory.put(dvd.getType(), quantity);
                }
            });
        }
        SystemStatisticsDTO systemStatisticsDTO = new SystemStatisticsDTO();
        systemStatisticsDTO.setOrdersCanceled(cancelledOrders.size());
        systemStatisticsDTO.setOrdersCompleted(completedOrders.size());
        systemStatisticsDTO.setDvdsPerCategories(dvdsPerCategory);
        return systemStatisticsDTO;
    }
}
