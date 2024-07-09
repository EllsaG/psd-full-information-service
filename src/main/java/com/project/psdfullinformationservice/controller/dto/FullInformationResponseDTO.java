package com.project.psdfullinformationservice.controller.dto;


import com.project.psdfullinformationservice.entity.FullInformation;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullInformationResponseDTO {

    private List<FullInformation> fullInformationList;
    private List<FullStartInformation> fullStartInformationList;

}
