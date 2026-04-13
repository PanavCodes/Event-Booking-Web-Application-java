package com.eventbooking.controller;

import com.eventbooking.model.Booking;
import com.eventbooking.model.Event;
import com.eventbooking.model.Payment;
import com.eventbooking.model.User;
import com.eventbooking.repository.BookingRepository;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.PaymentRepository;
import com.eventbooking.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public BookingController(BookingRepository bookingRepository,
                             EventRepository eventRepository,
                             UserRepository userRepository,
                             PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    // ===== POST /api/bookings =====
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> body) {
        try {
            Object userIdObj = body.get("userId");
            Object eventIdObj = body.get("eventId");
            Object ticketsObj = body.get("tickets");

            if (userIdObj == null || eventIdObj == null || ticketsObj == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId, eventId, and tickets are required"));
            }

            Long userId = Long.parseLong(userIdObj.toString());
            Long eventId = Long.parseLong(eventIdObj.toString());
            int tickets = Integer.parseInt(ticketsObj.toString());

            // Get event details
            Optional<Event> eventOpt = eventRepository.findById(eventId);
            if (eventOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Event not found"));
            }

            Event event = eventOpt.get();

            if (tickets > event.getAvailableSeats()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Not enough seats available"));
            }

            // Get user
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            BigDecimal totalAmount = event.getPrice().multiply(BigDecimal.valueOf(tickets));

            // Create booking
            Booking booking = new Booking();
            booking.setUser(userOpt.get());
            booking.setEvent(event);
            booking.setQuantity(tickets);
            booking.setTotalAmount(totalAmount);
            booking.setStatus("CONFIRMED");
            Booking saved = bookingRepository.save(booking);

            // Decrement available seats
            event.setAvailableSeats(event.getAvailableSeats() - tickets);
            eventRepository.save(event);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Booking created");
            response.put("bookingId", saved.getId());
            response.put("eventId", event.getId());
            response.put("eventName", event.getTitle());
            response.put("eventDate", event.getEventDate().toString());
            response.put("tickets", tickets);
            response.put("pricePerTicket", event.getPrice());
            response.put("totalPrice", totalAmount);
            return ResponseEntity.status(201).body(response);

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    // ===== GET /api/bookings/{userId} =====
    @GetMapping("/bookings/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Long userId) {
        try {
            List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(userId);

            List<Map<String, Object>> result = bookings.stream().map(b -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("bookingId", b.getId());
                map.put("tickets", b.getQuantity());
                map.put("totalPrice", b.getTotalAmount());
                map.put("status", b.getStatus());
                map.put("bookedAt", b.getBookingDate().toString());
                map.put("eventName", b.getEvent().getTitle());
                map.put("eventDate", b.getEvent().getEventDate().toString());
                map.put("pricePerTicket", b.getEvent().getPrice());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    // ===== POST /api/payments =====
    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> body) {
        try {
            Object bookingIdObj = body.get("bookingId");
            String paymentMethod = (String) body.get("paymentMethod");

            if (bookingIdObj == null || paymentMethod == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "bookingId and paymentMethod are required"));
            }

            Long bookingId = Long.parseLong(bookingIdObj.toString());

            // Check that booking exists
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Booking not found"));
            }

            // Insert payment
            Payment payment = new Payment();
            payment.setBooking(bookingOpt.get());
            payment.setPaymentStatus("SUCCESS");
            payment.setPaymentMethod(paymentMethod);
            Payment saved = paymentRepository.save(payment);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Payment recorded");
            response.put("paymentId", saved.getId());
            response.put("bookingId", bookingId);
            response.put("paymentMethod", paymentMethod);
            response.put("status", "SUCCESS");
            return ResponseEntity.status(201).body(response);

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }
}
