package com.example.filestore.repositories;


import com.example.filestore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByLoginToken(String token);

    User findByEmailAndIdNot(String email, Long id);

    User findByUsernameAndIdNot(String username, Long id);

}