package project.service.statistics;

import project.dto.SystemStatisticsDTO;
import project.dto.UserStatisticsDTO;

public interface OrderStatisticsService {

    UserStatisticsDTO findStatisticsForUser(String userName);

    SystemStatisticsDTO findSystemStatistics();
}
