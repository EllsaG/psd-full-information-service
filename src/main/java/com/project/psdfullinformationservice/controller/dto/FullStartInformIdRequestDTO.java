package com.project.psdfullinformationservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullStartInformIdRequestDTO {

    @JsonProperty("numberOfBusbar")
    private short numberOfBusbar;
    @JsonProperty("numbersOfEquipment")
    private short numberOfEquipment;
    @JsonProperty("amountOfEquipments")
    private short amountOfEquipment;


}
