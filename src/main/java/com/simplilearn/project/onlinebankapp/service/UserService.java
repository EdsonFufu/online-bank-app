package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.User;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.UserRepository;
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
public class UserService {
    @Autowired private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }
    public User findById(long id) {
        return userRepository.getById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public boolean deleteById(long id) {
        try {
            userRepository.deleteById(id);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean findUserByUsername(String username){
        try {
            return userRepository.findUserByUsername(username).isPresent();

        }catch (NullPointerException nullPointerException){
            return false;
        }
    }

    public boolean exists(long id){
        return userRepository.existsById(id);
    }
    public Paged<User> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> userPage = userRepository.findAll(request);
        return new Paged<>(userPage, Paging.of(userPage.getTotalPages(), pageNumber, size));
    }
}
