package com.example.seagowhere.Service;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Booking;
import com.example.seagowhere.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }


    public Booking updateBooking(Integer bookingId, Booking bookingDetails) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setTitle(bookingDetails.getTitle());
            booking.setNationality(bookingDetails.getNationality());
            booking.setGender(bookingDetails.getGender());
            booking.setDob(bookingDetails.getDob());
            booking.setPassportNo(bookingDetails.getPassportNo());
            booking.setPassportExp(bookingDetails.getPassportExp());
            booking.setAPackage(bookingDetails.getAPackage());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }


    public void deleteBookingById(Integer bookingId) {
        bookingRepository.deleteById(bookingId);
    }

}
