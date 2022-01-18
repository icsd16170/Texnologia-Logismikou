package project.dto;

public class DVDStatisticsDTO {

    private String title;

    private Long id;

    private int timesPurchased;

    private int quantitiesPurchased;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTimesPurchased() {
        return timesPurchased;
    }

    public void setTimesPurchased(int timesPurchased) {
        this.timesPurchased = timesPurchased;
    }

    public int getQuantitiesPurchased() {
        return quantitiesPurchased;
    }

    public void setQuantitiesPurchased(int quantitiesPurchased) {
        this.quantitiesPurchased = quantitiesPurchased;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
