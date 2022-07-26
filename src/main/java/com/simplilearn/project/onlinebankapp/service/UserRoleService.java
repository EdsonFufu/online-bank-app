package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.UserRole;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
import com.simplilearn.project.onlinebankapp.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRole save(UserRole userRole){
        return userRoleRepository.save(userRole);
    }
    public UserRole findById(long id) {
        return userRoleRepository.getById(id);
    }

    public List<UserRole> findAll(){
        return userRoleRepository.findAll();
    }

    public boolean deleteById(long id) {
        try {
            if (userRoleRepository.existsById(id)) {
                UserRole roles = userRoleRepository.getById(id);
                userRoleRepository.delete(roles);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
    public Paged<UserRole> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserRole> userRolePage = userRoleRepository.findAll(request);
        return new Paged<>(userRolePage, Paging.of(userRolePage.getTotalPages(), pageNumber, size));
    }
}
