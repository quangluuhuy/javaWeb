/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

/**
 *
 * @author ADMIN
 */
public class Account {
    private String phone;
    private String pass;

    public Account() {
    }

    public Account(String phone, String pass) {
        this.phone = phone;
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Account{" + "phone=" + phone + ", pass=" + pass + '}';
    }
    
    
}
