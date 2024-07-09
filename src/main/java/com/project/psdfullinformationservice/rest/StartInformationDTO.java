package com.project.psdfullinformationservice.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartInformationDTO {

    private short startInformationId;
    private String name;
    private float activePower;
    private short amount;
    private float ki;
    private float cosf;
    private float tgf;
    private float avgDailyActivePower;
    private float avgDailyReactivePower;
}
