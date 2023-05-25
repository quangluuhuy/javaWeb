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
public class EmployeeTerritories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeID;
    @Column
    private int tereitoryID;

    public EmployeeTerritories() {
    }

    public EmployeeTerritories(int employeeID, int tereitoryID) {
        this.employeeID = employeeID;
        this.tereitoryID = tereitoryID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getTereitoryID() {
        return tereitoryID;
    }

    public void setTereitoryID(int tereitoryID) {
        this.tereitoryID = tereitoryID;
    }

}
