package com.simplilearn.project.onlinebankapp.controllers;

import com.simplilearn.project.onlinebankapp.entities.Roles;
import com.simplilearn.project.onlinebankapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleRepository roleRepository;
    @PostMapping
    public Roles create(@RequestBody Roles roles){
        return roleRepository.save(roles);
    }
    @PutMapping
    public Roles update(@RequestBody Roles roles){
        return roleRepository.save(roles);
    }
    @GetMapping("/{id}")
    public Roles get(@PathVariable("id") int id){
        Optional<Roles> optionalRoles = roleRepository.findById(id);
        if(optionalRoles.isPresent()) {
            return optionalRoles.get();
        }
        return null;
    }
    @GetMapping
    public List<Roles> index(){
        return roleRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String idInString){
        int id = Integer.parseInt(idInString);

        if(roleRepository.existsById(id)){
            Roles roles = roleRepository.getById(id);
            roleRepository.delete(roles);
            return "Roles with id [" + id + "] deleted Successfully";
        }
        return "Roles with Id " + id + " Not found";
    }
}
