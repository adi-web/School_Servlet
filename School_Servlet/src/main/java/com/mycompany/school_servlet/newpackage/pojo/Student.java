/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.school_servlet.newpackage.pojo;

/**
 *
 * @author admirportatile
 */
public class Student {

    public String id;
    public String name;
    public String surname;
    public String sidiCode;
    public String taxCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSidiCode() {
        return sidiCode;
    }

    public void setSidiCode(String sidiCode) {
        this.sidiCode = sidiCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

}
