package com.fyp.hotel.dao.hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.fyp.hotel.model.HotelRoom;

import java.util.List;

import org.hibernate.query.NativeQuery;

@Repository
public class HotelRoomDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public List<HotelRoom> getHotelRooms(
            Long hotelId,
            String roomType,
            String roomCategory,
            String roomBed,
            String minRoomPrice,
            String maxRoomPrice,
            Boolean hasAC,
            Boolean hasBalcony,
            Boolean hasRefridge,
            int page,
            int size
    ) {
        try {
            // Initialize the dynamic query with the base SELECT statement
            StringBuilder dynamicQuery = new StringBuilder("SELECT * FROM hotel_room hr WHERE hr.hotel_id = :hotelId ");

            // Add conditions to the dynamic query based on input parameters
            if (roomType != null && !roomType.isEmpty()) {
                dynamicQuery.append(" AND hr.room_type = :roomType ");
            }

            if (roomCategory != null && !roomCategory.isEmpty()) {
                dynamicQuery.append(" AND hr.bed_category = :roomCategory ");
            }

            if (roomBed != null && !roomBed.isEmpty()) {
                dynamicQuery.append(" AND hr.bed_type = :roomBed ");
            }
            if (minRoomPrice != null && !minRoomPrice.isEmpty()) {
                dynamicQuery.append(" AND hr.room_price >= :minRoomPrice ");
            }
            if (maxRoomPrice != null && !maxRoomPrice.isEmpty()) {
                dynamicQuery.append(" AND hr.room_price <= :maxRoomPrice ");
            }
            if (hasAC != null) {
                dynamicQuery.append(" AND hr.has_ac = :hasAC ");
            }
            if (hasBalcony != null) {
                dynamicQuery.append(" AND hr.has_balcony = :hasBalcony ");
            }
            if (hasRefridge != null) {
                dynamicQuery.append(" AND hr.has_refridge = :hasRefridge ");
            }

            // Add ORDER BY clause for room_price
//            dynamicQuery.append(" ORDER BY hr.room_price ASC ");

            // Open a session and create a native query based on the dynamic query string
            Session session = sessionFactory.openSession();
            NativeQuery<HotelRoom> query = session.createNativeQuery(dynamicQuery.toString(), HotelRoom.class);

            // Set parameters based on input values
            query.setParameter("hotelId", hotelId);

            if (roomType != null && !roomType.isEmpty()) {
                query.setParameter("roomType", roomType);
            }

            if (roomCategory != null && !roomCategory.isEmpty()) {
                query.setParameter("roomCategory", roomCategory);
            }

            if (roomBed != null && !roomBed.isEmpty()) {
                query.setParameter("roomBed", roomBed);
            }

            if (minRoomPrice != null && !minRoomPrice.isEmpty()) {
                query.setParameter("minRoomPrice", Double.parseDouble(minRoomPrice));
            }

            if (maxRoomPrice != null && !maxRoomPrice.isEmpty()) {
                query.setParameter("maxRoomPrice", Double.parseDouble(maxRoomPrice));
            }

            if (hasAC != null) {
                query.setParameter("hasAC", hasAC);
            }

            if (hasBalcony != null) {
                query.setParameter("hasBalcony", hasBalcony);
            }

            if (hasRefridge != null) {
                query.setParameter("hasRefridge", hasRefridge);
            }

            // Execute the query and retrieve the result list
            List<HotelRoom> hotelRoomList = query.getResultList();

            // Return the result list
            return hotelRoomList;
        } catch (Exception e) {
            // Handle exceptions and log error messages
            System.out.println("Error in getHotelRooms: this is from repo class " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void updateRoomStatusAsAvailable(Long bookingId) {
        entityManager.createQuery(
                        "UPDATE HotelRoom hr SET hr.roomStatus = 'AVAILABLE' " +
                                "WHERE hr.roomId = (SELECT b.hotelRoom.roomId FROM Booking b WHERE b.bookingId = :bookingId)")
                .setParameter("bookingId", bookingId)
                .executeUpdate();
    }
}
