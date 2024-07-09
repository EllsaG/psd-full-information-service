package com.project.psdfullinformationservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "start-information-service")
public interface StartInformationServiceClient {

    @RequestMapping(value = "startInformation/getInformationByIdList", method = RequestMethod.GET)
    StartInformationResponseDTO getStartInformationByIdList(@RequestParam(value = "eq") List<Short> startInformationIds);

}
