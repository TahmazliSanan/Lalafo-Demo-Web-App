package org.pronet.lalafodemo.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String name;
    private String status;
    private String location;
    private String mobileNumber;
    private String imageName;
    private Double price;
    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteList> favoriteList;
    @CreationTimestamp
    private LocalDateTime createdDateTime;

    public Product() {
    }

    public Product(
            Long id, String name, String status, String location, String mobileNumber, String imageName,
            Double price, Category category, User user, List<FavoriteList> favoriteList) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.location = location;
        this.mobileNumber = mobileNumber;
        this.imageName = imageName;
        this.price = price;
        this.category = category;
        this.user = user;
        this.favoriteList = favoriteList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<FavoriteList> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<FavoriteList> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
