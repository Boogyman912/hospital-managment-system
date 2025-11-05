package com.hms.hospital_management_system.jpaRepository;
import org.springframework.stereotype.Repository;
//import repo dependecies , room 
import com.hms.hospital_management_system.models.Room;
import com.hms.hospital_management_system.models.Room.RoomStatus;
import com.hms.hospital_management_system.models.Room.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // Custom query to find rooms by type
    @Query("SELECT r FROM Room r WHERE r.type = :type")
    List<Room> findByType(@Param("type") RoomType type);

    // Custom query to find rooms by status
    @Query("SELECT r FROM Room r WHERE r.status = :status")
    List<Room> findByStatus(@Param("status") RoomStatus status);

    //query to find by ttype and status
    @Query("SELECT r FROM Room r WHERE r.type = :type AND r.status = :status")
    List<Room> findByTypeAndStatus(@Param("type") RoomType type, @Param("status") RoomStatus status);

}
