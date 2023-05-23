package com.kCalControl.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users", catalog = "kcal_control")
public class UserDB {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;
    @Column(name = "f_name", length = 20)
    private String firstName;

    @Column(name = "l_name", length = 40)
    private String lastName;

    @Column(name = "mobile", length = 12)
    private String mobile;

    @Column(name = "email", length = 80)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "password_date")
    private LocalDateTime passwordDate;

    @Embedded
    private Assets assets;

    public UserDB() {
    }

    public UserDB(String username, String firstName, String lastName, String mobile, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public UserDB getCreationPerson() {
        return getAssets().getCreationPerson();
    }

    public LocalDateTime getCreationDate() {
        return getAssets().getCreationDate();
    }

    public UserDB getModificationPerson() {
        return getAssets().getModificationPerson();
    }

    public LocalDateTime getModificationDate() {
        return getAssets().getModificationDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getPasswordDate() {
        return passwordDate;
    }

    public void setPasswordDate(LocalDateTime passwordDate) {
        this.passwordDate = passwordDate;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDB userDB)) return false;
        return Objects.equals(id, userDB.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDB{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordDate=" + passwordDate +
                ", assets=" + assets +
                '}';
    }
}
