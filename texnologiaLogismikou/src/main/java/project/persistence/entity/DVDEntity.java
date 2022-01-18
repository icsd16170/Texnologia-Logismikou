package project.persistence.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class DVDEntity {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> actors = new ArrayList<>();

    @Column(nullable = false)
    private String stageDirector;

    @Column(nullable = false)
    private LocalDate productionDate;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> supportedSpokenLanguages = new ArrayList<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> supportedSubtitleLanguages = new ArrayList<>();

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private double cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getStageDirector() {
        return stageDirector;
    }

    public void setStageDirector(String stageDirector) {
        this.stageDirector = stageDirector;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public List<String> getSupportedSpokenLanguages() {
        return supportedSpokenLanguages;
    }

    public void setSupportedSpokenLanguages(List<String> supportedSpokenLanguages) {
        this.supportedSpokenLanguages = supportedSpokenLanguages;
    }

    public List<String> getSupportedSubtitleLanguages() {
        return supportedSubtitleLanguages;
    }

    public void setSupportedSubtitleLanguages(List<String> supportedSubtitleLanguages) {
        this.supportedSubtitleLanguages = supportedSubtitleLanguages;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
