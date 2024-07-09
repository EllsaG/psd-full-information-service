package com.project.psdfullinformationservice.repository;

import com.project.psdfullinformationservice.entity.FullStartInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullStartInformationRepository extends JpaRepository<FullStartInformation,Short> {

     void deleteAllByFullInformationId(short fullInformationId);
     List<FullStartInformation> findByFullInformationIdIn(List<Short> fullInformationId);

}
