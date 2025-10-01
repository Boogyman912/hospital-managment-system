package com.hms.hospital_management_system.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.hms.hospital_management_system.models.Inventory;
import com.hms.hospital_management_system.services.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/itemName/{itemName}")
    public ResponseEntity<List<Inventory>> getInventoryByItemName(@PathVariable String itemName) {
        List<Inventory> inventory = inventoryService.getInventoryByItemName(itemName);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/ItemBrand/{brandName}")
    public ResponseEntity<List<Inventory>> getInventoryByBrandName(@PathVariable String brandName) {
        List<Inventory> inventory = inventoryService.getInventoryByBrandName(brandName);
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/quantityZero")
    public ResponseEntity<List<Inventory>> getInventoryByQuantityZero() {
        List<Inventory> inventory = inventoryService.getInventoryByQuantityZero();
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/quantityLessThan/{quantity}")
    public ResponseEntity<List<Inventory>> getInventoryByQuantityLessThan(@PathVariable Integer quantity) {
        List<Inventory> inventory = inventoryService.getInventoryByQuantityLessThan(quantity);
        return ResponseEntity.ok(inventory);
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody Inventory inventory) {
        inventoryService.createInventory(inventory);
        return ResponseEntity.ok("Inventory created successfully");
    }
    
    @PatchMapping("/updateItem/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventoryService.updateInventory(id, inventory);
        return ResponseEntity.ok("Inventory updated successfully");
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok("Inventory deleted successfully");
    }

    @PatchMapping("/increaseQuantity/{id}")
    public ResponseEntity<String> increaseQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        inventoryService.increaseQuantity(id, quantity);
        inventoryService.updateLastRestocked(id);
        return ResponseEntity.ok("Item restocked successfully");
    }
    
    @PatchMapping("/decreaseQuantity/{id}")
    public ResponseEntity<String> decreaseQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        inventoryService.decreaseQuantity(id, quantity);
        return ResponseEntity.ok("Inventory quantity decreased successfully");
    }
    
    @PatchMapping("/updatePrice/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable Long id, @RequestBody Double add){
        inventoryService.updateUnitPrice(id, add);
        return ResponseEntity.ok("Price updated successfully");
    }
    
    
}
