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
public class App_user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String created_by;
    @Column
    private String created_date;
    @Column
    private String last_modified_by;
    @Column
    private String last_modified_date;
    @Column
    private int activated;
    @Column
    private String activation_key;
    @Column
    private String email;
    @Column
    private String first_name;
    @Column
    private String image_url;
    @Column
    private String lang_key;
    @Column
    private String login;
    @Column
    private String password_hash;
    @Column
    private String reser_date;
    @Column
    private String reser_key;

    public App_user() {
    }

    public App_user(int id, String created_by, String created_date, String last_modified_by, String last_modified_date, int activated, String activation_key, String email, String first_name, String image_url, String lang_key, String login, String password_hash, String reser_date, String reser_key) {
        this.id = id;
        this.created_by = created_by;
        this.created_date = created_date;
        this.last_modified_by = last_modified_by;
        this.last_modified_date = last_modified_date;
        this.activated = activated;
        this.activation_key = activation_key;
        this.email = email;
        this.first_name = first_name;
        this.image_url = image_url;
        this.lang_key = lang_key;
        this.login = login;
        this.password_hash = password_hash;
        this.reser_date = reser_date;
        this.reser_key = reser_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public String getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public String getActivation_key() {
        return activation_key;
    }

    public void setActivation_key(String activation_key) {
        this.activation_key = activation_key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLang_key() {
        return lang_key;
    }

    public void setLang_key(String lang_key) {
        this.lang_key = lang_key;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getReser_date() {
        return reser_date;
    }

    public void setReser_date(String reser_date) {
        this.reser_date = reser_date;
    }

    public String getReser_key() {
        return reser_key;
    }

    public void setReser_key(String reser_key) {
        this.reser_key = reser_key;
    }
    
            
    
    
    
    
    
    
}
