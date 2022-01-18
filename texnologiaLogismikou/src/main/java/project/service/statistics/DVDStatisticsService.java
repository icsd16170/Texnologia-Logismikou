package project.service.statistics;

import java.util.List;
import project.dto.DVDStatisticsDTO;

public interface DVDStatisticsService {

    DVDStatisticsDTO getDvdStatistics(Long id);

    List<DVDStatisticsDTO> getAllDvdStatistics();
}
