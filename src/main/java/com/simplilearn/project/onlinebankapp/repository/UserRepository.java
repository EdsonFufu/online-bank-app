package com.simplilearn.project.onlinebankapp.repository;

import com.simplilearn.project.onlinebankapp.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByUsername(String username);

    @Override
    <S extends User> boolean exists(Example<S> example);

    Optional<User> findUserByEmail(String email);
}
