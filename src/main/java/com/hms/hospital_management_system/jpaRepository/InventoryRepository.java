package com.hms.hospital_management_system.jpaRepository;

import com.hms.hospital_management_system.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    //Item name and brand name together constitutes a unique key
    
    @Query("SELECT i FROM Inventory i WHERE i.lastRestocked > :date")
    List<Inventory> findByLastRestockedAfterDate(LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.lastRestocked < :date")
    List<Inventory> findByLastRestockedBeforeDate(LocalDate date);

    @Query("SELECT i FROM Inventory i WHERE i.brandName = :brandName")
    List<Inventory> findByBrandName(String brandName);

    @Query("SELECT i FROM Inventory i WHERE i.itemName = :itemName")
    List<Inventory> findByItemName(String itemName);

    @Query("SELECT i FROM Inventory i WHERE i.quantity = 0")
    List<Inventory> findByQuantityZero();

    @Query("SELECT i FROM Inventory i WHERE i.quantity < :quantity")
    List<Inventory> findByQuantityLessThan(Integer quantity);

    //increase quantity
    @Modifying
    @Query("Update Inventory set quantity = quantity + :quantity where itemId = :itemId")
    void increaseQuantity(Long itemId, Integer quantity);

    //decrease quantity
    @Modifying
    @Query("Update Inventory set quantity = quantity - :quantity where itemId = :itemId")
    void decreaseQuantity(Long itemId, Integer quantity);

    //increase  unit price
    @Modifying
    @Query("Update Inventory set unitPrice = unitPrice + :unitPrice where itemId = :itemId")
    void updateUnitPrice(Long itemId, Double unitPrice);

    //update last restocked by current date
    @Modifying
    @Query("Update Inventory set lastRestocked = current_date where itemId = :itemId")
    void updateLastRestocked(Long itemId);
   
    @Query("Select i from Inventory i where i.itemName = :itemName and i.brandName = :brandName ")
    Inventory findByItemNameAndBrandName(String itemName, String brandName);
    
    
}
