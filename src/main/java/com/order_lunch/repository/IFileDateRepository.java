package com.order_lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.FileData;


@Repository
public interface IFileDateRepository extends JpaRepository<FileData,Integer> {

    @Override
    FileData getOne(Integer fileDataId);
}
