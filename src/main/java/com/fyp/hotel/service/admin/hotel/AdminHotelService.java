package com.fyp.hotel.service.admin.hotel;

import com.fyp.hotel.dto.hotel.HotelDto;

import java.util.List;

public interface AdminHotelService {
    List<HotelDto> getAllHotels(int page, int size);
    HotelDto getHotelProfile(long userId);
}

