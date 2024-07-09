package com.project.psdfullinformationservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullInformationRequestDTO {
    @JsonProperty("fullInformationId")
    private short fullInformationId;
    @JsonProperty("nameOfBusbar")
    private String nameOfBusbar;
    @JsonProperty("numbersAndAmountOfEquipments")
    private List <FullStartInformIdRequestDTO> fullStartInformId;

}
