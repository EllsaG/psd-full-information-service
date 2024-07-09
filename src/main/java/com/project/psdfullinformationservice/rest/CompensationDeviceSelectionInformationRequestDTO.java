package com.project.psdfullinformationservice.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CompensationDeviceSelectionInformationRequestDTO {

    private short compensationDeviceSelectionId;
    private float avgDailyActivePower;
    private float tgfBeforeCompensation;
}
