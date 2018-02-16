package com.example.filestore.repositories;


import com.example.filestore.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

   // @Query("SELECT p FROM Person p WHERE LOWER(p.lastName) = LOWER(:lastName)")

//    @Query("SELECT g FROM user_groups")
//    public List<Group> find(@Param("lastName") Long floorId);


}