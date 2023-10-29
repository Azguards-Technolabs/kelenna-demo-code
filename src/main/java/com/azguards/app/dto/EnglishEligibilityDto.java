package com.azguards.app.dto;

public class EnglishEligibilityDto {
    
    private String id;
    private String englishType;
    private Double reading;
    private Double writing;
    private Double speaking;
    private Double listening;
    private Double overall;
    private Boolean isActive;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEnglishType() {
        return englishType;
    }
    public void setEnglishType(String englishType) {
        this.englishType = englishType;
    }
    public Double getReading() {
        return reading;
    }
    public void setReading(Double reading) {
        this.reading = reading;
    }
    public Double getWriting() {
        return writing;
    }
    public void setWriting(Double writing) {
        this.writing = writing;
    }
    public Double getSpeaking() {
        return speaking;
    }
    public void setSpeaking(Double speaking) {
        this.speaking = speaking;
    }
    public Double getListening() {
        return listening;
    }
    public void setListening(Double listening) {
        this.listening = listening;
    }
    public Double getOverall() {
        return overall;
    }
    public void setOverall(Double overall) {
        this.overall = overall;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
