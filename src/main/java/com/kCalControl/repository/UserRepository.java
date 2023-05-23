package com.kCalControl.repository;

import com.kCalControl.model.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Integer> {
    public Optional<UserDB> findByUsername(String name);
    public Optional<UserDB> findByFirstName(String name);
}
