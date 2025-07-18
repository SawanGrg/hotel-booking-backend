package com.fyp.hotel.dto.room;

import com.fyp.hotel.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RoomHistoryDTO {

    private String userName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long totalAmount; // Change to Long
    private String paymentMethod;
    private LocalDate bookingDate;
    private Status status;

    public RoomHistoryDTO() {
    }

    public RoomHistoryDTO(String userName, LocalDate checkInDate, LocalDate checkOutDate, Long totalAmount, String paymentMethod, LocalDate bookingDate, Status status) { // Change totalAmount to Long
        this.userName = userName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.bookingDate = bookingDate;
        this.status = status;
    }
}
