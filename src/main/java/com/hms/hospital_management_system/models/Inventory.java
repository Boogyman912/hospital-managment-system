package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime; 

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private LocalDateTime lastRestocked;

    public Inventory() {
    }
    public Inventory(String itemName, Integer quantity, Double unitPrice, LocalDateTime lastRestocked) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lastRestocked = lastRestocked;
    }
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Double getUnitPrice() {
        return unitPrice;}
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public LocalDateTime getLastRestocked() {
        return lastRestocked;
    }
    public void setLastRestocked(LocalDateTime lastRestocked) {
        this.lastRestocked = lastRestocked;
    }
    
}
