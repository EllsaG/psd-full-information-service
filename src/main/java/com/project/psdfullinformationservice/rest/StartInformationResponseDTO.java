package com.project.psdfullinformationservice.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartInformationResponseDTO {

    List<StartInformationDTO> startInformationList;
}
