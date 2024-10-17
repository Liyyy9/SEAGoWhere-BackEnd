package com.example.seagowhere.Repository;

import com.example.seagowhere.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
