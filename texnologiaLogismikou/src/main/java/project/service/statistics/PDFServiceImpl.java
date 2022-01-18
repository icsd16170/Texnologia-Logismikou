package project.service.statistics;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import project.dto.DVDStatisticsDTO;
import project.dto.SystemStatisticsDTO;
import project.dto.UserDTO;
import project.dto.UserStatisticsDTO;
import project.service.user.UserService;

@Service
public class PDFServiceImpl implements PDFService {

    private static final Font BASE_FONT = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.COURIER_BOLD, 16, BaseColor.BLACK);

    private static final Font CELLS_FONT = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);

    private static final Paragraph NEW_LINE = new Paragraph("\n");

    private final OrderStatisticsService orderStatisticsService;

    private final UserService userService;

    private final DVDStatisticsService dvdStatisticsService;

    public PDFServiceImpl(OrderStatisticsService orderStatisticsService, UserService userService,
            DVDStatisticsService dvdStatisticsService) {
        this.orderStatisticsService = orderStatisticsService;
        this.userService = userService;
        this.dvdStatisticsService = dvdStatisticsService;
    }

    @Override
    public byte[] getStatisticsPDF() {

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        createSystemStatistics(document);
        createUsersStatistics(document);
        Paragraph dvdStatistics = new Paragraph("DVD Statistics", TITLE_FONT);
        PdfPTable dvdStatisticsTable = new PdfPTable(3);
        addDvDStatisticsTableHeader(dvdStatisticsTable);
        List<DVDStatisticsDTO> allDvdStatistics = dvdStatisticsService.getAllDvdStatistics();

        allDvdStatistics.forEach(dvdStatisticsDTO -> {
            dvdStatisticsTable.addCell(new PdfPCell(new Phrase(dvdStatisticsDTO.getTitle(), CELLS_FONT)));
            dvdStatisticsTable.addCell(new PdfPCell(new Phrase(String.valueOf(dvdStatisticsDTO.getTimesPurchased()), CELLS_FONT)));
            dvdStatisticsTable.addCell(new PdfPCell(new Phrase(String.valueOf(dvdStatisticsDTO.getQuantitiesPurchased()), CELLS_FONT)));
        });
        try {
            document.add(dvdStatistics);
            document.add(NEW_LINE);
            document.add(dvdStatisticsTable);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        document.close();
        return byteArrayOutputStream.toByteArray();
    }


    private void createSystemStatistics(Document document) {
        SystemStatisticsDTO systemStatistics = orderStatisticsService.findSystemStatistics();
        Paragraph totalStatisticsTitle = new Paragraph("System Statistics", TITLE_FONT);
        Paragraph completedOrders = new Paragraph("Completed Orders: " + systemStatistics.getOrdersCompleted(), BASE_FONT);
        Paragraph canceledOrders = new Paragraph("Canceled Orders: " + systemStatistics.getOrdersCanceled(), BASE_FONT);
        Paragraph userStatisticsTitle = new Paragraph("User Statistics", TITLE_FONT);

        PdfPTable totalStatisticsTable = new PdfPTable(2);
        addDvDPerCategoriesTableHeader(totalStatisticsTable);
        addRowsTodvdPerCategoriesTable(totalStatisticsTable, systemStatistics.getDvdsPerCategories());
        try {
            document.add(totalStatisticsTitle);
            document.add(completedOrders);
            document.add(canceledOrders);
            document.add(NEW_LINE);
            document.add(totalStatisticsTable);
            document.add(NEW_LINE);
            document.add(userStatisticsTitle);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void createUsersStatistics(Document document) {
        List<UserDTO> users = userService.findAllUsers();
        users.forEach(user -> {
            UserStatisticsDTO statisticsForUser = orderStatisticsService.findStatisticsForUser(user.getUserName());

            if (statisticsForUser.getOrdersCompleted() != 0) {

                Paragraph userName = new Paragraph("Username: " + statisticsForUser.getUserName(), BASE_FONT);
                Paragraph userCompletedOrders = new Paragraph("Completed Orders: " + statisticsForUser.getOrdersCompleted(), BASE_FONT);
                Paragraph userCanceledOrders = new Paragraph("Canceled Orders: " + statisticsForUser.getOrdersCanceled(), BASE_FONT);
                PdfPTable userDvdPerCategories = new PdfPTable(2);
                addDvDPerCategoriesTableHeader(userDvdPerCategories);
                addRowsTodvdPerCategoriesTable(userDvdPerCategories, statisticsForUser.getDvdsPerCategories());
                try {
                    document.add(userName);
                    document.add(userCompletedOrders);
                    document.add(userCanceledOrders);
                    document.add(NEW_LINE);
                    document.add(userDvdPerCategories);
                    document.add(NEW_LINE);

                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addDvDPerCategoriesTableHeader(PdfPTable dvdPerCategoriesTable) {
        Stream.of("DVD category", "Times Bought")
              .forEach(columnTitle -> {
                  PdfPCell header = new PdfPCell();
                  header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                  header.setBorderWidth(2);
                  header.setPhrase(new Phrase(columnTitle));
                  dvdPerCategoriesTable.addCell(header);
              });
    }

    private void addDvDStatisticsTableHeader(PdfPTable dvdStatisticsTable) {
        Stream.of("Title", "Times Purchased", "Quantity Purchased")
              .forEach(columnTitle -> {
                  PdfPCell header = new PdfPCell();
                  header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                  header.setBorderWidth(2);
                  header.setPhrase(new Phrase(columnTitle));
                  dvdStatisticsTable.addCell(header);
              });
    }

    private void addRowsTodvdPerCategoriesTable(PdfPTable dvdPerCategoriesTable, Map<String, Integer> dvdsPerCategories) {

        dvdsPerCategories.forEach((category, timesBought) -> {
            dvdPerCategoriesTable.addCell(new PdfPCell(new Phrase(category, CELLS_FONT)));
            dvdPerCategoriesTable.addCell(new PdfPCell(new Phrase(timesBought.toString(), CELLS_FONT)));
        });

    }
}
