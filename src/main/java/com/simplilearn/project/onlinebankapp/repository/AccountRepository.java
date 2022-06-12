package com.simplilearn.project.onlinebankapp.repository;

import com.simplilearn.project.onlinebankapp.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
}
