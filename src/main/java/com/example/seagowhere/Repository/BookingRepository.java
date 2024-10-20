package com.example.seagowhere.Repository;

import com.example.seagowhere.Model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings, Long> {
    List<Bookings> findBookingById(Long bookingId);
    List<Bookings> findBookingByUserId(Long userId);
}
