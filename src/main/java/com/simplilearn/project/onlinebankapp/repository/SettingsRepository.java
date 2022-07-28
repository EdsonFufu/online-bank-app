package com.simplilearn.project.onlinebankapp.repository;

import com.simplilearn.project.onlinebankapp.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}