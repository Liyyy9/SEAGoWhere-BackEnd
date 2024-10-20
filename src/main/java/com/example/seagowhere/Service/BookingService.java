package com.example.seagowhere.Service;


import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Bookings;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Model.Users;
import com.example.seagowhere.Repository.BookingRepository;
import com.example.seagowhere.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PackagesService packagesService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Bookings> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Optional<Bookings> getBookingById(Long bookingId){
        return bookingRepository.findById(bookingId);
    }

    public Bookings createBooking(Long packageId, Bookings bookings){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var users = new Users();
        users = userRepository.findByEmail(authentication.getName()).orElseThrow();

        Packages packages = packagesService.findById(packageId).orElseThrow(() -> new ResourceNotFoundException());
        bookings.setPackages(packages);
        bookings.setUser(users);
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

    public List<Bookings> getBookingsByUserId(Integer userId){
        return bookingRepository.findBookingByUserId(Long.valueOf(userId));
    }
}
