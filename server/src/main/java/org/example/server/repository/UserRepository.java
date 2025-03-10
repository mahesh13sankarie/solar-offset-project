package org.example.server.repository;

import org.example.server.dto.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository implements CrudRepository {
    private PasswordEncoder passwordEncoder;

    public UserDto findByEmail(String email) {
        return new UserDto();
    }

    private boolean isAccountExists(String email) {
        return false; //TODO: adjust logic
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public Iterable saveAll(Iterable entities) {
        return null;
    }

    @Override
    public Optional findById(Object o) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Object o) {
        return false;
    }

    @Override
    public Iterable findAll() {
        return null;
    }

    @Override
    public Iterable findAllById(Iterable iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Object o) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void deleteAllById(Iterable iterable) {

    }

    @Override
    public void deleteAll(Iterable entities) {

    }

    @Override
    public void deleteAll() {

    }
}
