package com.example.filestore.repositories;

import com.example.filestore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<User, Long> {

}
