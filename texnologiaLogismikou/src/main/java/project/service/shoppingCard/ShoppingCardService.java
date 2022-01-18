package project.service.shoppingCard;

import java.util.List;
import project.dto.ShoppingCardDTO;

public interface ShoppingCardService {

    ShoppingCardDTO create(ShoppingCardDTO shoppingCardDTO);

    void delete(Long id);

    boolean existsById(Long id);

    ShoppingCardDTO findById(Long id);

    List<ShoppingCardDTO> findByUserName(String customerUserName);

    void submit(Long id);

    ShoppingCardDTO update(ShoppingCardDTO shoppingCardDTO, Long id);


    List<ShoppingCardDTO> findCompletedShoppingCards();

}