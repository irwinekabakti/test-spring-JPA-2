package com.example.test_spring_JPA_2.repository;
import com.example.test_spring_JPA_2.model.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PocketRepository extends JpaRepository<Pocket, Long> {

}
