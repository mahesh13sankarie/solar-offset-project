package org.example.server.repository;

import jakarta.transaction.Transactional;
import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("select u from User u where u.accountType = 1 or u.accountType = 2")
    List<User> fetchUsers();

    @Modifying
    @Query("update User u set u.accountType = :#{#userRequest.accountType().orElse(1)} where u.id = :#{#userRequest.userId()}")
    @Transactional
    void updateRole(UserRequest userRequest);

    @Modifying
    @Query("update User u set u.password = :password where u.email = :email")
    @Transactional
    void updatePassword(String email, String password);
}