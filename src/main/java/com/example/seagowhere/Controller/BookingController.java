package com.example.seagowhere.Controller;

import com.example.seagowhere.Exception.ResourceNotFoundException;
import com.example.seagowhere.Model.Booking;
import com.example.seagowhere.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController{

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable(value = "id") Integer bookingId) {
        Booking booking = bookingService.findBookingById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException());
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking newBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable(value = "id") Integer bookingId,
                                                 @RequestBody Booking bookingDetails) {
        Booking booking = bookingService.findBookingById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException());

        // Update fields
        booking.setTitle(bookingDetails.getTitle());
        booking.setNationality(bookingDetails.getNationality());
        booking.setGender(bookingDetails.getGender());
        booking.setDob(bookingDetails.getDob());
        booking.setPassportNo(bookingDetails.getPassportNo());
        booking.setPassportExp(bookingDetails.getPassportExp());
        booking.setAPackage(bookingDetails.getAPackage());
        booking.setUser(bookingDetails.getUser());

        Booking updatedBooking = bookingService.updateBooking(bookingId, booking);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBooking(@PathVariable(value = "id") Integer bookingId) {
        Booking checkBooking = bookingService.findBookingById(bookingId).map(booking -> {
            bookingService.deleteBookingById(booking.getBookingId());
            return booking;
        }).orElseThrow(() -> new ResourceNotFoundException());

        String response = String.format("Booking %s deleted successfully", checkBooking.getTitle());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
