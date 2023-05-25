/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.northwind.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author luuqu
 */
@Entity
public class Teritories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teritoryID;
    @Column
    private String teritoryDescription;
    @Column
    private int regionID;

    public Teritories() {
    }

    public Teritories(int teritoryID, String teritoryDescription, int regionID) {
        this.teritoryID = teritoryID;
        this.teritoryDescription = teritoryDescription;
        this.regionID = regionID;
    }

    public int getTeritoryID() {
        return teritoryID;
    }

    public void setTeritoryID(int teritoryID) {
        this.teritoryID = teritoryID;
    }

    public String getTeritoryDescription() {
        return teritoryDescription;
    }

    public void setTeritoryDescription(String teritoryDescription) {
        this.teritoryDescription = teritoryDescription;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }
    
    
}
