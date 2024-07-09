package com.project.psdfullinformationservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "compensation-device-service")
public interface CompensationDeviceServiceClient {

    @RequestMapping(value = "compensationDevice/create/selectionInformation", method = RequestMethod.PUT)
    void saveCompensationDeviceSelectionInformation(@RequestBody CompensationDeviceSelectionInformationRequestDTO compensationDeviceSelectionInformationRequestDTO);

    @RequestMapping(value = "compensationDevice/delete/selectionInformation/{compensationDeviceSelectionId}", method = RequestMethod.DELETE)
    void deleteCompensationDeviceSelectionInformationById(@PathVariable("compensationDeviceSelectionId") short compensationDeviceSelectionId);

    @RequestMapping(value = "compensationDevice/check/{compensationDeviceSelectionId}", method = RequestMethod.GET)
    Boolean checkAvailability(@PathVariable("compensationDeviceSelectionId") short compensationDeviceSelectionId);



}
