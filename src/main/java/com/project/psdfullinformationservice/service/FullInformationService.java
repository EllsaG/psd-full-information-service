package com.project.psdfullinformationservice.service;



import com.project.psdfullinformationservice.calculation.FullInformationCalculation;
import com.project.psdfullinformationservice.controller.dto.FullInformationByIdResponseDTO;
import com.project.psdfullinformationservice.controller.dto.FullInformationResponseDTO;
import com.project.psdfullinformationservice.entity.FullInformation;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import com.project.psdfullinformationservice.exceptions.InformationAlreadyExistsException;
import com.project.psdfullinformationservice.exceptions.InformationNotFoundException;
import com.project.psdfullinformationservice.repository.FullInformationRepository;
import com.project.psdfullinformationservice.repository.FullStartInformationRepository;
import com.project.psdfullinformationservice.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class FullInformationService {

    private final FullInformationRepository fullInformationRepository;
    private final StartInformationServiceClient startInformationServiceClient;
    private final CompensationDeviceServiceClient compensationDeviceServiceClient;
    private final PowerTransformerSelectionServiceClient powerTransformerSelectionServiceClient;
    private final FullStartInformationRepository fullStartInformationRepository;

    @Autowired
    public FullInformationService(FullInformationRepository fullInformationRepository,
                                  StartInformationServiceClient startInformationServiceClient,
                                  CompensationDeviceServiceClient compensationDeviceServiceClient,
                                  PowerTransformerSelectionServiceClient powerTransformerSelectionServiceClient,
                                  FullStartInformationRepository fullStartInformationRepository) {
        this.fullInformationRepository = fullInformationRepository;
        this.startInformationServiceClient = startInformationServiceClient;
        this.compensationDeviceServiceClient = compensationDeviceServiceClient;
        this.powerTransformerSelectionServiceClient = powerTransformerSelectionServiceClient;
        this.fullStartInformationRepository = fullStartInformationRepository;
    }

    public FullInformationResponseDTO saveNewBusbar(short fullInformationId, String nameOfBusbar,
                                                    List<FullStartInformation> numbersAndAmountOfEquipments) {

        if (fullInformationRepository.existsById(fullInformationId)) {
            throw new InformationAlreadyExistsException("Information about busbar with id № " + fullInformationId + " is already exists");
        }

        FullInformationCalculation fullInformationCalculation = new FullInformationCalculation();

        List<FullStartInformation> informationAboutBusbarIncludedEquipment = fullInformationCalculation.
                getInformationAboutBusbarIncludedEquipment( numbersAndAmountOfEquipments, startInformationServiceClient);

        FullInformation fullInformation = fullInformationCalculation.calculationNewBusbar(fullInformationId,
                nameOfBusbar, informationAboutBusbarIncludedEquipment);

        fullInformationRepository.save(fullInformation);
        fullStartInformationRepository.saveAll(informationAboutBusbarIncludedEquipment);

        return getAllFullInformation();
    }

    public FullInformationResponseDTO saveMainBusbar(short fullInformationId, String nameOfBusbar,
                                                     List<Short> numbersBusbarsIncludedInMain) {

        if (fullInformationRepository.existsById(fullInformationId)) {
            throw new InformationAlreadyExistsException("Information about busbar with id № " + fullInformationId + " is already exists");
        }
        List<FullStartInformation> allEquipmentsActivePowerList = fullStartInformationRepository
                .findByFullInformationIdIn(numbersBusbarsIncludedInMain);

        List<FullInformation> fullInformationList = fullInformationRepository.findAllById(numbersBusbarsIncludedInMain);

        FullInformationCalculation fullInformationCalculation = new FullInformationCalculation();
        FullInformation fullInformation = fullInformationCalculation.calculationMainBusbar(fullInformationList,
                fullInformationId, nameOfBusbar, allEquipmentsActivePowerList);

        compensationDeviceServiceClient.saveCompensationDeviceSelectionInformation(
                new CompensationDeviceSelectionInformationRequestDTO(fullInformationId, fullInformation.getAvgDailyActivePower(), fullInformation.getTgF()));
        powerTransformerSelectionServiceClient.savePowerTransformerSelectionInformation(
                new PowerTransformerSelectionInformationRequestDTO(fullInformationId, fullInformation.getMaxFullPower()));
        fullInformationRepository.save(fullInformation);

        return getAllFullInformation();
    }

    public FullInformationResponseDTO updateBusbar(short fullInformationId, String nameOfBusbar,
                                                   List<FullStartInformation> numbersAndAmountOfEquipments) {

        deleteFullInformationById(fullInformationId);
        saveNewBusbar(fullInformationId, nameOfBusbar, numbersAndAmountOfEquipments);
        return getAllFullInformation();
    }

    public FullInformationResponseDTO updateMainBusbar(short fullInformationId, String nameOfBusbar,
                                                       List<Short> numbersBusbarsIncludedInMain) {
        deleteMainBusbarById(fullInformationId);
        saveMainBusbar(fullInformationId, nameOfBusbar, numbersBusbarsIncludedInMain);
        return getAllFullInformation();
    }
    public FullInformationByIdResponseDTO getInformationById(short fullInformationId) {
        FullInformation fullInformation = fullInformationRepository.findById(fullInformationId)
                .orElseThrow(() -> new InformationNotFoundException("Unable to find information about busbar with id № " + fullInformationId));

        return new FullInformationByIdResponseDTO(fullInformation);
    }

    public FullInformationResponseDTO getAllFullInformation() {
        return new FullInformationResponseDTO(fullInformationRepository.findAll(),
                fullStartInformationRepository.findAll());
    }



    public FullInformationResponseDTO deleteMainBusbarById(short fullInformationId) {

        deleteFullInformationById(fullInformationId);

        if (compensationDeviceServiceClient.checkAvailability(fullInformationId)) {
            compensationDeviceServiceClient.deleteCompensationDeviceSelectionInformationById(fullInformationId);
        }
        if (powerTransformerSelectionServiceClient.checkAvailability(fullInformationId)) {
            powerTransformerSelectionServiceClient.deletePowerTransformerSelectionInformationById(fullInformationId);
        }
        return getAllFullInformation();
    }

    public void deleteFullInformationById(short fullInformationId){
        fullInformationRepository.deleteById(fullInformationId);
        fullStartInformationRepository.deleteAllByFullInformationId(fullInformationId);
    }
    public Boolean isAvailable(short fullInformationId) {
        return fullInformationRepository.existsById(fullInformationId);
    }

}
