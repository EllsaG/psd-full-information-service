package com.project.psdfullinformationservice.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "power-transformers-service")
public interface PowerTransformerSelectionServiceClient {

    @RequestMapping(value = "powerTransformers/create/selectionInformation", method = RequestMethod.PUT)
    void savePowerTransformerSelectionInformation(@RequestBody PowerTransformerSelectionInformationRequestDTO powerTransformerSelectionInformationRequestDTO);

    @RequestMapping(value = "powerTransformers/delete/selectionInformation/{powerTransformersSelectionId}", method = RequestMethod.DELETE)
    void deletePowerTransformerSelectionInformationById(@PathVariable("powerTransformersSelectionId") short powerTransformersSelectionId);

    @RequestMapping(value = "powerTransformers/check/{powerTransformersSelectionId}", method = RequestMethod.GET)
    Boolean checkAvailability(@PathVariable("powerTransformersSelectionId") short powerTransformersSelectionId);

}
