package com.project.psdfullinformationservice.controller;


import com.project.psdfullinformationservice.controller.dto.FullInformationByIdResponseDTO;
import com.project.psdfullinformationservice.controller.dto.FullInformationMainBusbarRequestDTO;
import com.project.psdfullinformationservice.controller.dto.FullInformationRequestDTO;
import com.project.psdfullinformationservice.controller.dto.FullInformationResponseDTO;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import com.project.psdfullinformationservice.service.FullInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController

public class FullInformationController {

    private final FullInformationService fullInformationService;

    @Autowired
    public FullInformationController(FullInformationService fullInformationService) {
        this.fullInformationService = fullInformationService;
    }

    @GetMapping("/getAllInformation")
    public FullInformationResponseDTO getAllFullInformation(){
        return fullInformationService.getAllFullInformation();
    }
    @GetMapping("/{fullInformationId}")
    public FullInformationByIdResponseDTO getAFullInformationById(@PathVariable  short fullInformationId){
        return fullInformationService.getInformationById(fullInformationId);
    }
    @PutMapping("/create")
    public FullInformationResponseDTO createNewBusbar(@RequestBody FullInformationRequestDTO fullInformationRequestDTO) {
        return fullInformationService.saveNewBusbar(fullInformationRequestDTO.getFullInformationId(),
                fullInformationRequestDTO.getNameOfBusbar(),
                fullInformationRequestDTO.getFullStartInformId().stream()
                        .map(e -> new FullStartInformation(e.getNumberOfBusbar(), e.getNumberOfEquipment(), e.getAmountOfEquipment()))
                        .collect(Collectors.toList()));
    }

    @PutMapping("/create/main")
    public FullInformationResponseDTO createMainBusbar(@RequestBody FullInformationMainBusbarRequestDTO fullInformationMainBusbarRequestDTO) {
        return fullInformationService.saveMainBusbar(fullInformationMainBusbarRequestDTO.getFullInformationId(),
                fullInformationMainBusbarRequestDTO.getBusbarName(),
                fullInformationMainBusbarRequestDTO.getNumbersBusbarsIncludedInMain());
    }

    @PutMapping("/update")
    public FullInformationResponseDTO update(@RequestBody FullInformationRequestDTO fullInformationRequestDTO) {
        return fullInformationService.updateBusbar(fullInformationRequestDTO.getFullInformationId(),
                fullInformationRequestDTO.getNameOfBusbar(),
                fullInformationRequestDTO.getFullStartInformId().stream()
                        .map(e -> new FullStartInformation(e.getNumberOfBusbar(), e.getNumberOfEquipment(), e.getAmountOfEquipment()))
                        .collect(Collectors.toList()));
    }

    @PutMapping("/update/main")
    public FullInformationResponseDTO updateMainBusbar(@RequestBody FullInformationMainBusbarRequestDTO fullInformationMainBusbarRequestDTO) {
        return fullInformationService.updateMainBusbar(fullInformationMainBusbarRequestDTO.getFullInformationId(),
                fullInformationMainBusbarRequestDTO.getBusbarName(),
                fullInformationMainBusbarRequestDTO.getNumbersBusbarsIncludedInMain());
    }

    @DeleteMapping("/delete/{fullInformationId}")
    public FullInformationResponseDTO deleteBusbar(@PathVariable  short fullInformationId){
        return fullInformationService.deleteMainBusbarById(fullInformationId);
    }

    @GetMapping("/check/{fullInformationId}")
    public Boolean checkAvailability(@PathVariable short fullInformationId){
        return fullInformationService.isAvailable(fullInformationId);
    }

}
