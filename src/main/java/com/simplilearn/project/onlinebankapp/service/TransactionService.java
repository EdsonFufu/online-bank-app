package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.Transaction;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction findById(long id) {
        return transactionRepository.getById(id);
    }

    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    public boolean exists(long id){
        return transactionRepository.existsById(id);
    }

    public boolean deleteById(long id) {
        try {
            transactionRepository.deleteById(id);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public Paged<Transaction> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Transaction> accountPage = transactionRepository.findAll(request);
        return new Paged<>(accountPage, Paging.of(accountPage.getTotalPages(), pageNumber, size));
    }

}
