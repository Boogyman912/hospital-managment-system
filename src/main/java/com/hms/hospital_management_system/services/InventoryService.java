package com.hms.hospital_management_system.services;
import org.springframework.stereotype.Service;
import java.util.List;
import com.hms.hospital_management_system.models.Inventory;
import com.hms.hospital_management_system.jpaRepository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
@Service    
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }
    
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    public Inventory updateInventory(Long id, Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
    
    public List<Inventory> getInventoryByItemName(String itemName) {
        return inventoryRepository.findByItemName(itemName);
    }
    
    public List<Inventory> getInventoryByBrandName(String brandName) {
        return inventoryRepository.findByBrandName(brandName);
    }
    

    public List<Inventory> getInventoryByQuantityZero() {
        return inventoryRepository.findByQuantityZero();
    }
    
    public List<Inventory> getInventoryByQuantityLessThan(Integer quantity) {
        return inventoryRepository.findByQuantityLessThan(quantity);
    }
    
    public void increaseQuantity(Long id, Integer quantity) {
        inventoryRepository.increaseQuantity(id, quantity);
    }
    
    public void decreaseQuantity(Long id, Integer quantity) {
        inventoryRepository.decreaseQuantity(id, quantity);
    }
    
    public void updateUnitPrice(Long id, Double unitPrice) {
        inventoryRepository.updateUnitPrice(id, unitPrice);
    }
    
    public void updateLastRestocked(Long id) {
        inventoryRepository.updateLastRestocked(id);
    }

    public Inventory getInventoryByItemNameAndBrandName(String itemName, String brandName) {
        System.out.println("Fetching inventory for Item Name: " + itemName + ", Brand Name: " + brandName);
        return inventoryRepository.findByItemNameAndBrandName(itemName, brandName);
    }

    // add method to get inventory by lastRestocked after a date
    public List<Inventory> getInventoryByLastRestockedAfterDate(LocalDate date) {
        return inventoryRepository.findByLastRestockedAfterDate(date);
    }

    // add method to get inventory by lastRestocked before a date
    public List<Inventory> getInventoryByLastRestockedBeforeDate(LocalDate date) {
        return inventoryRepository.findByLastRestockedBeforeDate(date);
    }
    
    
    
}
