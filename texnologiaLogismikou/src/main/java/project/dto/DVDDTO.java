package project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;


public class DVDDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "must not be null")
    private String title;

    @NotNull(message = "must not be null")
    private List<String> actors = new ArrayList<>();

    @NotNull(message = "must not be null")
    private String stageDirector;

    @NotNull(message = "must not be null")
    private LocalDate productionDate;

    @NotNull(message = "must not be null")
    private List<String> supportedSpokenLanguages = new ArrayList<>();

    @NotNull(message = "must not be null")
    private List<String> supportedSubtitleLanguages = new ArrayList<>();

    @NotNull(message = "must not be null")
    private int duration;

    @NotNull(message = "must not be null")
    private String type;

    @NotNull(message = "must not be null")
    private int quantityAvailable;

    @NotNull(message = "must not be null")
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DVDDTO dvddto = (DVDDTO) o;
        return duration == dvddto.duration && quantityAvailable == dvddto.quantityAvailable && Double.compare(dvddto.cost, cost) == 0
                && Objects.equals(id, dvddto.id) && Objects.equals(title, dvddto.title) && Objects.equals(actors, dvddto.actors)
                && Objects.equals(stageDirector, dvddto.stageDirector) && Objects.equals(productionDate, dvddto.productionDate)
                && Objects.equals(supportedSpokenLanguages, dvddto.supportedSpokenLanguages) && Objects.equals(supportedSubtitleLanguages,
                dvddto.supportedSubtitleLanguages) && Objects.equals(type, dvddto.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, actors, stageDirector, productionDate, supportedSpokenLanguages, supportedSubtitleLanguages, duration, type,
                quantityAvailable, cost);
    }
}
