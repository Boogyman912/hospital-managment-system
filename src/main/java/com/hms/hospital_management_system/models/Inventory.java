package com.hms.hospital_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate; 

@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "idx_inventory_item_brand", columnList = "itemName, brandName"),
    @Index(name = "idx_inventory_quantity", columnList = "quantity")
})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;

    private String brandName;

    private Integer quantity;

    private Double unitPrice;

    private LocalDate lastRestocked;

    public Inventory() {
    }
    public Inventory(String itemName,String brandName, Integer quantity, Double unitPrice, LocalDate lastRestocked) {
        this.itemName = itemName;
        this.brandName = brandName;
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
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
    public LocalDate getLastRestocked() {
        return lastRestocked;
    }
    public void setLastRestocked(LocalDate lastRestocked) {
        this.lastRestocked = lastRestocked;
    }
    
}
