package org.example.foodanddrinkproject.repository;

import org.example.foodanddrinkproject.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSuggestionRepository extends JpaRepository<Suggestion, Long> {
    Page<Suggestion> findAllByOrderByCreatedAtDesc(Pageable pageable);
}