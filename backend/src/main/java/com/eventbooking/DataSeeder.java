package com.eventbooking;

import com.eventbooking.model.*;
import com.eventbooking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public DataSeeder(AdminRepository adminRepository,
                      UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      VenueRepository venueRepository,
                      EventRepository eventRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        try {
            // Seed Admin
            if (adminRepository.count() == 0) {
                adminRepository.save(new Admin("Admin", "admin@gmail.com", "admin123"));
            }

            // Seed User
            if (userRepository.count() == 0) {
                userRepository.save(new User("User", "user@gmail.com", "user123"));
            }

            // Seed Category
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("General", "Default category"));
            }

            // Seed Venue
            if (venueRepository.count() == 0) {
                venueRepository.save(new Venue("Main Hall", "City Center", 500));
            }

            // Seed Events
            if (eventRepository.count() == 0) {
                Venue venue = venueRepository.findAll().get(0);
                Category category = categoryRepository.findAll().get(0);
                Admin admin = adminRepository.findAll().get(0);

                Event event1 = new Event();
                event1.setTitle("Music Concert");
                event1.setDescription("Live music");
                event1.setEventDate(LocalDate.of(2026, 4, 10));
                event1.setVenue(venue);
                event1.setPrice(new BigDecimal("500"));
                event1.setTotalSeats(100);
                event1.setAvailableSeats(100);
                event1.setCategory(category);
                event1.setAdmin(admin);
                eventRepository.save(event1);

                Event event2 = new Event();
                event2.setTitle("Tech Workshop");
                event2.setDescription("Workshop");
                event2.setEventDate(LocalDate.of(2026, 4, 15));
                event2.setVenue(venue);
                event2.setPrice(new BigDecimal("300"));
                event2.setTotalSeats(50);
                event2.setAvailableSeats(50);
                event2.setCategory(category);
                event2.setAdmin(admin);
                eventRepository.save(event2);
            }

            System.out.println("✅ Seed complete");
        } catch (Exception ex) {
            System.err.println("❌ Seed error: " + ex.getMessage());
        }
    }
}
