package com.simplilearn.project.onlinebankapp.repository;

import com.simplilearn.project.onlinebankapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
