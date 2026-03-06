package medicare.back.dto;

public class RiskDto {

    private int pollutionScore;  
    private int meteoScore;      
    private int industryScore;   
    private int totalScore;      
    private String level;        

    public int getPollutionScore() {
        return pollutionScore;
    }

    public void setPollutionScore(int pollutionScore) {
        this.pollutionScore = pollutionScore;
    }
    
    public int getMeteoScore() {
        return meteoScore;
    }

    public void setMeteoScore(int meteoScore) {
        this.meteoScore = meteoScore;
    }

    public int getIndustryScore() {
        return industryScore;
    }

    public void setIndustryScore(int industryScore) {
        this.industryScore = industryScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}