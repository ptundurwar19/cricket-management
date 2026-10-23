package com.cricket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fall_of_wickets")
public class FallOfWicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int wicketNumber;
    private int scoreAtFall;
    private String overAtFall;
    private String batsmanName;

    public FallOfWicket() {}

    public FallOfWicket(int wicketNumber, int scoreAtFall, String overAtFall, String batsmanName) {
        this.wicketNumber = wicketNumber;
        this.scoreAtFall = scoreAtFall;
        this.overAtFall = overAtFall;
        this.batsmanName = batsmanName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getWicketNumber() { return wicketNumber; }
    public void setWicketNumber(int wicketNumber) { this.wicketNumber = wicketNumber; }

    public int getScoreAtFall() { return scoreAtFall; }
    public void setScoreAtFall(int scoreAtFall) { this.scoreAtFall = scoreAtFall; }

    public String getOverAtFall() { return overAtFall; }
    public void setOverAtFall(String overAtFall) { this.overAtFall = overAtFall; }

    public String getBatsmanName() { return batsmanName; }
    public void setBatsmanName(String batsmanName) { this.batsmanName = batsmanName; }
}
