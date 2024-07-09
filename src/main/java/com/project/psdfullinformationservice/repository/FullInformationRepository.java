package com.project.psdfullinformationservice.repository;


import com.project.psdfullinformationservice.entity.FullInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullInformationRepository extends JpaRepository<FullInformation,Short> {

}
