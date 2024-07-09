package com.project.psdfullinformationservice.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PowerTransformerSelectionInformationRequestDTO {

    private short powerTransformerSelectionId;
    private float ratedPowerForTransformerSelection;
}
