package com.simplilearn.project.onlinebankapp.service;

import com.simplilearn.project.onlinebankapp.entities.Account;
import com.simplilearn.project.onlinebankapp.entities.paging.Paged;
import com.simplilearn.project.onlinebankapp.entities.paging.Paging;
import com.simplilearn.project.onlinebankapp.repository.AccountRepository;
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
public class AccountService {
    private AccountRepository accountRepository;

    public Account save(Account account){
        return accountRepository.save(account);
    }
    public Account getById(int id) {
        return accountRepository.getById(id);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public boolean deleteById(int id) {
        try {
            accountRepository.deleteById(id);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public Paged<Account> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Account> accountPage = accountRepository.findAll(request);
        return new Paged<>(accountPage, Paging.of(accountPage.getTotalPages(), pageNumber, size));
    }
}
