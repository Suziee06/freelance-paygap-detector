package com.freelancedetector.repository;

import com.freelancedetector.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByUserUserId(Long userId);
    List<Client> findByIndustry(String industry);
    List<Client> findByCountry(String country);
}
