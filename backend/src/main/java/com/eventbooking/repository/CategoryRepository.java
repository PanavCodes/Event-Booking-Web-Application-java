package com.eventbooking.repository;

import com.eventbooking.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    long count();
}
