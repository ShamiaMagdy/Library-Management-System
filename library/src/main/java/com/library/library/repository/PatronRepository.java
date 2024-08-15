package com.library.library.repository;

import com.library.library.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatronRepository extends JpaRepository<Patron, Long> {
    Optional<Patron> findByName(String name);
    Optional<Patron> findByContactInformation(String contactInformation);
    Optional<Patron> findByPhoneNumber(String phoneNumber);
}
