package com.example.seagowhere.Service;


import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Bookings;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackagesService packagesService;

    public List<Bookings> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Optional<Bookings> getBookingById(Long bookingId){
        return bookingRepository.findById(bookingId);
    }

    public Bookings createBooking(Long packageId, Bookings bookings){
        Packages packages = packagesService.findById(packageId).orElseThrow(() -> new ResourceNotFoundException());
        bookings.setPackages(packages);
        return bookingRepository.save(bookings);
    }

    public Bookings updateBooking(Long bookingId, Bookings bookingDetails){
        return bookingRepository.findById(bookingId).map(existingBooking -> {
            existingBooking.setDob(bookingDetails.getDob());
            existingBooking.setGender(bookingDetails.getGender());
            existingBooking.setNationality(bookingDetails.getNationality());
            existingBooking.setPassportNo(bookingDetails.getPassportNo());
            existingBooking.setPassportExp(bookingDetails.getPassportExp());

            return bookingRepository.save(existingBooking);
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public void deleteBooking (Long bookingId){
        bookingRepository.deleteById(bookingId);
    }

}
