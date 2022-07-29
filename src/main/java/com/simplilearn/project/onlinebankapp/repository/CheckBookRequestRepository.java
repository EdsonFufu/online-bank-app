package com.simplilearn.project.onlinebankapp.repository;

import com.simplilearn.project.onlinebankapp.entities.CheckBookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckBookRequestRepository extends JpaRepository<CheckBookRequest, Long> {
}