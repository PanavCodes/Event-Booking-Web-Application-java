package com.eventbooking.controller;

import com.eventbooking.model.Admin;
import com.eventbooking.model.Category;
import com.eventbooking.model.Event;
import com.eventbooking.model.Venue;
import com.eventbooking.repository.AdminRepository;
import com.eventbooking.repository.CategoryRepository;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.VenueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;

    public EventController(EventRepository eventRepository,
                           VenueRepository venueRepository,
                           CategoryRepository categoryRepository,
                           AdminRepository adminRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.categoryRepository = categoryRepository;
        this.adminRepository = adminRepository;
    }

    // ===== GET /api/events =====
    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();

            List<Map<String, Object>> result = events.stream().map(e -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", e.getId());
                map.put("name", e.getTitle());
                map.put("description", e.getDescription());
                map.put("date", e.getEventDate().toString());
                map.put("price", e.getPrice());
                map.put("totalSeats", e.getTotalSeats());
                map.put("seats", e.getAvailableSeats());
                map.put("venueId", e.getVenue().getId());
                map.put("categoryId", e.getCategory().getId());
                map.put("adminId", e.getAdmin().getId());
                map.put("venueName", e.getVenue().getName());
                map.put("venueLocation", e.getVenue().getLocation());
                map.put("categoryName", e.getCategory().getName());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    // ===== POST /api/events =====
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            String date = (String) body.get("date");
            Object priceObj = body.get("price");
            Object seatsObj = body.get("seats");

            if (name == null || date == null || priceObj == null || seatsObj == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Name, date, price, and seats are required"));
            }

            BigDecimal price = new BigDecimal(priceObj.toString());
            int seats = Integer.parseInt(seatsObj.toString());

            Long venueId = body.get("venueId") != null ? Long.parseLong(body.get("venueId").toString()) : 1L;
            Long categoryId = body.get("categoryId") != null ? Long.parseLong(body.get("categoryId").toString()) : 1L;
            Long adminId = body.get("adminId") != null ? Long.parseLong(body.get("adminId").toString()) : 1L;

            Venue venue = venueRepository.findById(venueId).orElse(null);
            Category category = categoryRepository.findById(categoryId).orElse(null);
            Admin admin = adminRepository.findById(adminId).orElse(null);

            if (venue == null || category == null || admin == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid venue, category, or admin ID"));
            }

            Event event = new Event();
            event.setTitle(name);
            event.setDescription("");
            event.setEventDate(LocalDate.parse(date));
            event.setVenue(venue);
            event.setPrice(price);
            event.setTotalSeats(seats);
            event.setAvailableSeats(seats);
            event.setCategory(category);
            event.setAdmin(admin);

            Event saved = eventRepository.save(event);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Event created");
            response.put("id", saved.getId());
            response.put("name", name);
            response.put("date", date);
            response.put("price", price);
            response.put("seats", seats);
            return ResponseEntity.status(201).body(response);

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    // ===== PUT /api/events/{id} =====
    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            String date = (String) body.get("date");
            Object priceObj = body.get("price");
            Object seatsObj = body.get("seats");

            if (name == null || date == null || priceObj == null || seatsObj == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Name, date, price, and seats are required"));
            }

            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Event not found"));
            }

            Event event = eventOpt.get();
            event.setTitle(name);
            event.setEventDate(LocalDate.parse(date));
            event.setPrice(new BigDecimal(priceObj.toString()));
            event.setAvailableSeats(Integer.parseInt(seatsObj.toString()));
            eventRepository.save(event);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Event updated");
            response.put("id", id);
            response.put("name", name);
            response.put("date", date);
            response.put("price", priceObj);
            response.put("seats", seatsObj);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    // ===== DELETE /api/events/{id} =====
    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            if (!eventRepository.existsById(id)) {
                return ResponseEntity.status(404).body(Map.of("error", "Event not found"));
            }

            eventRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Event deleted"));

        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }
}
