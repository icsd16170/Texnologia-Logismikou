package project.service.shoppingCard;

import static org.mockito.ArgumentMatchers.any;

import java.util.HashMap;
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
import project.dto.ShoppingCardDTO;
import project.errorhandling.exception.ShoppingCardIsNotActiveException;
import project.persistence.entity.ShoppingCardEntity;
import project.persistence.repository.OrderRepository;
import project.persistence.repository.ShoppingCardRepository;
import project.service.Constants;
import project.service.dvd.DVDService;
import project.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class ShoppingCardServiceImplTest {

    @Mock
    private ShoppingCardRepository shoppingCardRepository;

    @Mock
    private DVDService dvdService;

    @InjectMocks
    private ShoppingCardServiceImpl testee;

    @Test
    void create_returns_savedEntity() {
        ShoppingCardDTO expected = getShoppingcardDTO();
        expected.setTotalCost(120);

        ShoppingCardDTO input = getShoppingcardDTO();

        Mockito.when(dvdService.findById(any())).thenReturn(getDVDdto());
        Mockito.when(shoppingCardRepository.save(getShoppingCardEntity())).thenReturn(getShoppingCardEntity());

        ShoppingCardDTO actual = testee.create(input);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_shoppingcardExists_deletesShoppingCard() {
        Mockito.when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(getShoppingCardEntity()));
        testee.delete(1L);
        Mockito.verify(shoppingCardRepository).delete(getShoppingCardEntity());
    }

    @Test
    void delete_statusNotActive_deletesShoppingCard() {
        ShoppingCardEntity shoppingCardEntity = getShoppingCardEntity();
        shoppingCardEntity.setStatus("NOTACTIVE");
        Mockito.when(shoppingCardRepository.findById(any())).thenReturn(Optional.of(shoppingCardEntity));
        Assertions.assertThrows(ShoppingCardIsNotActiveException.class,()->testee.delete(1L));
    }

    @Test
    void existsById_entityExists_returnsTrue(){
        Mockito.when(shoppingCardRepository.existsById(any())).thenReturn(true);
        Assertions.assertTrue(testee.existsById(1L));
    }
    @Test
    void existsById_entityNotExists_returnsFalse(){
        Mockito.when(shoppingCardRepository.existsById(any())).thenReturn(false);
        Assertions.assertFalse(testee.existsById(1L));
    }


    private DVDDTO getDVDdto() {
        DVDDTO dvddto = new DVDDTO();
        dvddto.setQuantityAvailable(10);
        dvddto.setCost(12);
        return dvddto;
    }

    private ShoppingCardDTO getShoppingcardDTO() {
        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();
        shoppingCardDTO.setStatus(Constants.ACTIVE);
        shoppingCardDTO.setCustomerUserName("username");

        Map<Long, Integer> dvd = new HashMap<>();
        dvd.put(1L, 10);

        shoppingCardDTO.setDvd(dvd);
        return shoppingCardDTO;
    }

    private ShoppingCardEntity getShoppingCardEntity() {
        ShoppingCardEntity shoppingCardEntity = new ShoppingCardEntity();
        shoppingCardEntity.setStatus(Constants.ACTIVE);
        shoppingCardEntity.setCustomerUserName("username");
        shoppingCardEntity.setTotalCost(120);

        Map<Long, Integer> dvd = new HashMap<>();
        dvd.put(1L, 10);

        shoppingCardEntity.setDvd(dvd);
        return shoppingCardEntity;
    }
}