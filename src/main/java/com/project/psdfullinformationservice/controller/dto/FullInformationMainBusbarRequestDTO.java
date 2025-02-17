package com.project.psdfullinformationservice.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullInformationMainBusbarRequestDTO {

    @JsonProperty("fullInformationId")
    private short fullInformationId;
    @JsonProperty("busbarName")
    private String busbarName;
    @JsonProperty("numbersBusbarsIncludedInMain")
    private List<Short> numbersBusbarsIncludedInMain;

}
