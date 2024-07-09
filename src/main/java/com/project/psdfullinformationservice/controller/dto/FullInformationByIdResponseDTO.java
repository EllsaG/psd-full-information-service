package com.project.psdfullinformationservice.controller.dto;


import com.project.psdfullinformationservice.entity.FullInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullInformationByIdResponseDTO {

    FullInformation fullInformation;
}
