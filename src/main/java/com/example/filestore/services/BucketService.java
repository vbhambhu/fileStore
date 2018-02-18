package com.example.filestore.services;

import com.example.filestore.entities.Bucket;
import com.example.filestore.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BucketService {

    @Autowired
    BucketRepository bucketRepository;


    public void createFolder(Bucket bucket) {


        bucket.setType("folder");

        bucketRepository.save(bucket);

    }
}
