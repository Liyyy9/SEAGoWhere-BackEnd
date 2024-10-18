package com.example.seagowhere.Controller;


import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Bookings;
import com.example.seagowhere.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/api/booking")
@CrossOrigin("*")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping("/{id}")
    public ResponseEntity<Object> createBooking(@PathVariable("id") Long packageId, @Valid @RequestBody Bookings booking) {
        return new ResponseEntity<>(bookingService.createBooking(packageId, booking), HttpStatus.CREATED);
    }

    // Get a booking by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long id) {
        Bookings booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<Object> getAllBookings() {
        List<Bookings> bookingList = bookingService.getAllBookings();
        if (bookingList.isEmpty()) throw new ResourceNotFoundException("No bookings found");

        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    // Update a booking
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBooking(@PathVariable Long id, @RequestBody @Valid Bookings bookingDetails) {
        Bookings updatedBooking = bookingService.updateBooking(id, bookingDetails);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    // Delete a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBooking(@PathVariable Long id) {
        Bookings deletedBooking = bookingService.getBookingById(id).map(booking -> {
            bookingService.deleteBooking(id);
            return booking;
        }).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        String response = String.format("Booking '%s' has been successfully deleted.", deletedBooking.getTitle());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}