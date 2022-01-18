package project.service.statistics;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import project.dto.DVDDTO;
import project.dto.DVDStatisticsDTO;
import project.dto.ShoppingCardDTO;
import project.service.dvd.DVDService;
import project.service.shoppingCard.ShoppingCardService;

@Service
public class DVDStatisticsServiceImpl implements DVDStatisticsService {
    private final ShoppingCardService shoppingCardService;

    private final DVDService dvdService;

    public DVDStatisticsServiceImpl(ShoppingCardService shoppingCardService, DVDService dvdService) {
        this.shoppingCardService = shoppingCardService;
        this.dvdService = dvdService;
    }

    @Override
    public DVDStatisticsDTO getDvdStatistics(Long id) {
        DVDDTO dvd = dvdService.findById(id);
        List<ShoppingCardDTO> completedShoppingCards = shoppingCardService.findCompletedShoppingCards();

        DVDStatisticsDTO dvdStatisticsDTO = new DVDStatisticsDTO();
        for (ShoppingCardDTO completedShoppingCard : completedShoppingCards) {
            completedShoppingCard.getDvd()
                                 .forEach((dvdId, quantity) -> {
                                     if (dvdId.equals(id)) {
                                         dvdStatisticsDTO.setTimesPurchased(dvdStatisticsDTO.getTimesPurchased() + 1);
                                         dvdStatisticsDTO.setQuantitiesPurchased(dvdStatisticsDTO.getQuantitiesPurchased() + quantity);
                                     }
                                 });
        }
        dvdStatisticsDTO.setTitle(dvd.getTitle());
        dvdStatisticsDTO.setId(id);
        return dvdStatisticsDTO;
    }

    @Override
    public List<DVDStatisticsDTO> getAllDvdStatistics() {
        List<DVDStatisticsDTO> dvdStatisticsDTOs = new ArrayList<>();
        List<DVDDTO> dvds = dvdService.findAll();
        dvds.forEach(dvd -> {
            DVDStatisticsDTO dvdStatistics = getDvdStatistics(dvd.getId());
            dvdStatisticsDTOs.add(dvdStatistics);
        });
        return dvdStatisticsDTOs;
    }
}