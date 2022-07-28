package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.Settings;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
import com.simplilearn.project.onlinebankapp.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettingService {
    private final SettingsRepository settingsRepository;

    public Settings save(Settings settings){
        return settingsRepository.save(settings);
    }
    public Settings findById(long id) {
        return settingsRepository.getReferenceById(id);
    }

    public List<Settings> findAll(){
        return settingsRepository.findAll();
    }

    public boolean deleteById(long id) {
        try {
            settingsRepository.deleteById(id);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public Settings getSettings() {
       Example<Settings> example = Example.of(Settings.builder().build());
       return settingsRepository.findOne(example).orElse(null);
    }
}
