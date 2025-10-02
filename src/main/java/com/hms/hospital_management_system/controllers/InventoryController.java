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
        try {
            List<Inventory> inventory = inventoryService.getAllInventory();
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        try {
            Inventory inventory = inventoryService.getInventoryById(id);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/itemName/{itemName}")
    public ResponseEntity<List<Inventory>> getInventoryByItemName(@PathVariable String itemName) {
        try {
            List<Inventory> inventory = inventoryService.getInventoryByItemName(itemName);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/ItemBrand/{brandName}")
    public ResponseEntity<List<Inventory>> getInventoryByBrandName(@PathVariable String brandName) {
        try {
            List<Inventory> inventory = inventoryService.getInventoryByBrandName(brandName);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/item/{itemName}/{brandName}")
    public ResponseEntity<Inventory> getInventoryByItemNameAndBrandName(@PathVariable String itemName, @PathVariable String brandName) {
        try {
            Inventory inventory = inventoryService.getInventoryByItemNameAndBrandName(itemName, brandName);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/quantityZero")
    public ResponseEntity<List<Inventory>> getInventoryByQuantityZero() {
        try {
            List<Inventory> inventory = inventoryService.getInventoryByQuantityZero();
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/quantityLessThan/{quantity}")
    public ResponseEntity<List<Inventory>> getInventoryByQuantityLessThan(@PathVariable Integer quantity) {
        try {
            List<Inventory> inventory = inventoryService.getInventoryByQuantityLessThan(quantity);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createInventory(@RequestBody Inventory inventory) {
        try {
            inventoryService.createInventory(inventory);
            return ResponseEntity.ok("Inventory created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating inventory");
        }
    }
    
    @PatchMapping("/updateItem/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        try {
            inventoryService.updateInventory(id, inventory);
            return ResponseEntity.ok("Inventory updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating inventory");
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.ok("Inventory deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting inventory");
        }
    }

    @PatchMapping("/increaseQuantity/{id}")
    public ResponseEntity<String> increaseQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        try {
            inventoryService.increaseQuantity(id, quantity);
            inventoryService.updateLastRestocked(id);
            return ResponseEntity.ok("Item restocked successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error restocking item");
        }
    }
    
    @PatchMapping("/decreaseQuantity/{id}")
    public ResponseEntity<String> decreaseQuantity(@PathVariable Long id, @RequestBody Integer quantity) {
        try {
            inventoryService.decreaseQuantity(id, quantity);
            return ResponseEntity.ok("Inventory quantity decreased successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error decreasing quantity");
        }
    }
    
    @PatchMapping("/updatePrice/{id}")
    public ResponseEntity<String> updatePrice(@PathVariable Long id, @RequestBody Double add){
        try {
            inventoryService.updateUnitPrice(id, add);
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating price");
        }
    }
    
    
}
