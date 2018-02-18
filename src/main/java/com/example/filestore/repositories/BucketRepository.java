package com.example.filestore.repositories;

import com.example.filestore.entities.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BucketRepository extends JpaRepository<Bucket, Long> {


}