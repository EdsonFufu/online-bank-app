package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.CheckBookRequest;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.CheckBookRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CheckBookRequestService {
    @Autowired private CheckBookRequestRepository checkBookRequestRepository;

    public Paged<CheckBookRequest> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<CheckBookRequest> checkBookRequests = checkBookRequestRepository.findAll(request);
        return new Paged<>(checkBookRequests, Paging.of(checkBookRequests.getTotalPages(), pageNumber, size));
    }

    public CheckBookRequest save(CheckBookRequest checkBookRequest) {
       return checkBookRequestRepository.save(checkBookRequest);
    }

    public List<CheckBookRequest> findAll(long userId){
        return checkBookRequestRepository.findAllById(Collections.singleton(userId));
    }
}
