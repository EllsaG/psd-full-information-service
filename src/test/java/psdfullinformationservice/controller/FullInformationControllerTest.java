package psdfullinformationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.psdfullinformationservice.controller.dto.*;
import com.project.psdfullinformationservice.entity.FullInformation;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import com.project.psdfullinformationservice.repository.FullInformationRepository;
import com.project.psdfullinformationservice.repository.FullStartInformationRepository;
import com.project.psdfullinformationservice.rest.*;
import com.project.psdfullinformationservice.PsdFullInformationServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psdfullinformationservice.config.SpringH2DatabaseConfig;


import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PsdFullInformationServiceApplication.class, SpringH2DatabaseConfig.class})
public class FullInformationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FullInformationRepository fullInformationRepository;
    @Autowired
    private FullStartInformationRepository fullStartInformationRepository;
    @MockBean
    private StartInformationServiceClient startInformationServiceClient;
    @MockBean
    private CompensationDeviceServiceClient compensationDeviceServiceClient;
    @MockBean
    private PowerTransformerSelectionServiceClient powerTransformerSelectionServiceClient;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/clearFullStartInformationDB.sql"})
    public void createNewBusbar() throws Exception {

        Mockito.when(startInformationServiceClient.getStartInformationByIdList(ArgumentMatchers.anyList()))
                .thenReturn(createStartInformationServiceClientResponseDTO());

        String REQUEST = createFullInformationRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationResponseDTO fullInformationResponseDTO = objectMapper.readValue(body, FullInformationResponseDTO.class);
        List<FullInformation> fullInformationResponseList = fullInformationResponseDTO.getFullInformationList();
        List<FullStartInformation> fullStartInformationResponseList = fullInformationResponseDTO.getFullStartInformationList();

        List<Short> shortList = new ArrayList<>();
        shortList.add((short) 1);
        shortList.add((short) 2);

        FullInformation fullInformation = fullInformationRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        List<FullStartInformation> byFullInformationIdsIn = fullStartInformationRepository.findByFullInformationIdIn(shortList);

        Assertions.assertEquals(fullInformationResponseList.get(0).getBusbarName(), fullInformation.getBusbarName());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAmount(), fullInformation.getAmount());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAvgDailyActivePower(), fullInformation.getAvgDailyActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAvgDailyReactivePower(), fullInformation.getAvgDailyReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getEffectiveAmountOfEquipment(), fullInformation.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(fullInformationResponseList.get(0).getCoefficientMax(), fullInformation.getCoefficientMax());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxActivePower(), fullInformation.getMaxActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxReactivePower(), fullInformation.getMaxReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxFullPower(), fullInformation.getMaxFullPower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxElectricCurrent(), fullInformation.getMaxElectricCurrent());
        Assertions.assertEquals(fullInformationResponseList.get(0).getActivePower(), fullInformation.getActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getCosF(), fullInformation.getCosF());
        Assertions.assertEquals(fullInformationResponseList.get(0).getTgF(), fullInformation.getTgF());
        Assertions.assertEquals(fullInformationResponseList.get(0).getKi(), fullInformation.getKi());
        Assertions.assertEquals(fullInformationResponseList.get(0).getModule(), fullInformation.getModule());


        Assertions.assertEquals(fullStartInformationResponseList.get(0).getFullInformationId(), byFullInformationIdsIn.get(0).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getStartInformationId(), byFullInformationIdsIn.get(0).getStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getName(), byFullInformationIdsIn.get(0).getName());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getActivePowerOfOne(), byFullInformationIdsIn.get(0).getActivePowerOfOne());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getActivePowerOfGroup(), byFullInformationIdsIn.get(0).getActivePowerOfGroup());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAmount(), byFullInformationIdsIn.get(0).getAmount());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getKi(), byFullInformationIdsIn.get(0).getKi());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getCosf(), byFullInformationIdsIn.get(0).getCosf());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getTgf(), byFullInformationIdsIn.get(0).getTgf());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAvgDailyActivePower(), byFullInformationIdsIn.get(0).getAvgDailyActivePower());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAvgDailyReactivePower(), byFullInformationIdsIn.get(0).getAvgDailyReactivePower());

        Assertions.assertEquals(fullStartInformationResponseList.get(1).getFullInformationId(), byFullInformationIdsIn.get(1).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getStartInformationId(), byFullInformationIdsIn.get(1).getStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getName(), byFullInformationIdsIn.get(1).getName());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getActivePowerOfOne(), byFullInformationIdsIn.get(1).getActivePowerOfOne());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getActivePowerOfGroup(), byFullInformationIdsIn.get(1).getActivePowerOfGroup());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAmount(), byFullInformationIdsIn.get(1).getAmount());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getKi(), byFullInformationIdsIn.get(1).getKi());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getCosf(), byFullInformationIdsIn.get(1).getCosf());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getTgf(), byFullInformationIdsIn.get(1).getTgf());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAvgDailyActivePower(), byFullInformationIdsIn.get(1).getAvgDailyActivePower());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAvgDailyReactivePower(), byFullInformationIdsIn.get(1).getAvgDailyReactivePower());
    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/clearFullStartInformationDB.sql",
            "/sql/addFullInformation.sql", "/sql/addFullStartInformation.sql"})
    public void createMainBusbar() throws Exception {

        String REQUEST = createFullInformationMainBusbarRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/create/main")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationResponseDTO fullInformationResponseDTO = objectMapper.readValue(body, FullInformationResponseDTO.class);
        List<FullInformation> fullInformationResponseList = fullInformationResponseDTO.getFullInformationList();

        FullInformation fullInformationRepositoryById = fullInformationRepository.findById((short) 3)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(fullInformationResponseList.get(2).getBusbarName(), fullInformationRepositoryById.getBusbarName());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAmount(), fullInformationRepositoryById.getAmount());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAvgDailyActivePower(), fullInformationRepositoryById.getAvgDailyActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAvgDailyReactivePower(), fullInformationRepositoryById.getAvgDailyReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getEffectiveAmountOfEquipment(), fullInformationRepositoryById.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(fullInformationResponseList.get(2).getCoefficientMax(), fullInformationRepositoryById.getCoefficientMax());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxActivePower(), fullInformationRepositoryById.getMaxActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxReactivePower(), fullInformationRepositoryById.getMaxReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxFullPower(), fullInformationRepositoryById.getMaxFullPower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxElectricCurrent(), fullInformationRepositoryById.getMaxElectricCurrent());
        Assertions.assertEquals(fullInformationResponseList.get(2).getActivePower(), fullInformationRepositoryById.getActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getCosF(), fullInformationRepositoryById.getCosF());
        Assertions.assertEquals(fullInformationResponseList.get(2).getTgF(), fullInformationRepositoryById.getTgF());
        Assertions.assertEquals(fullInformationResponseList.get(2).getKi(), fullInformationRepositoryById.getKi());
        Assertions.assertEquals(fullInformationResponseList.get(2).getModule(), fullInformationRepositoryById.getModule());

    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/clearFullStartInformationDB.sql",
            "/sql/addFullInformation.sql", "/sql/addFullStartInformation.sql"})
    public void update() throws Exception {

        Mockito.when(startInformationServiceClient.getStartInformationByIdList(ArgumentMatchers.anyList()))
                .thenReturn(createStartInformationServiceClientResponseDTO());

        String REQUEST = createFullInformationRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationResponseDTO fullInformationResponseDTO = objectMapper.readValue(body, FullInformationResponseDTO.class);
        List<FullInformation> fullInformationResponseList = fullInformationResponseDTO.getFullInformationList();
        List<FullStartInformation> fullStartInformationResponseList = fullInformationResponseDTO.getFullStartInformationList();

        List<Short> shortList = new ArrayList<>();
        shortList.add((short) 1);
        shortList.add((short) 2);

        FullInformation fullInformation = fullInformationRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        List<FullStartInformation> byFullInformationIdsIn = fullStartInformationRepository.findByFullInformationIdIn(shortList);

        Assertions.assertEquals(fullInformationResponseList.get(0).getBusbarName(), fullInformation.getBusbarName());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAmount(), fullInformation.getAmount());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAvgDailyActivePower(), fullInformation.getAvgDailyActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getAvgDailyReactivePower(), fullInformation.getAvgDailyReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getEffectiveAmountOfEquipment(), fullInformation.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(fullInformationResponseList.get(0).getCoefficientMax(), fullInformation.getCoefficientMax());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxActivePower(), fullInformation.getMaxActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxReactivePower(), fullInformation.getMaxReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxFullPower(), fullInformation.getMaxFullPower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getMaxElectricCurrent(), fullInformation.getMaxElectricCurrent());
        Assertions.assertEquals(fullInformationResponseList.get(0).getActivePower(), fullInformation.getActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(0).getCosF(), fullInformation.getCosF());
        Assertions.assertEquals(fullInformationResponseList.get(0).getTgF(), fullInformation.getTgF());
        Assertions.assertEquals(fullInformationResponseList.get(0).getKi(), fullInformation.getKi());
        Assertions.assertEquals(fullInformationResponseList.get(0).getModule(), fullInformation.getModule());


        Assertions.assertEquals(fullStartInformationResponseList.get(0).getFullStartInformationId(), byFullInformationIdsIn.get(0).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getStartInformationId(), byFullInformationIdsIn.get(0).getStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getName(), byFullInformationIdsIn.get(0).getName());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getActivePowerOfOne(), byFullInformationIdsIn.get(0).getActivePowerOfOne());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getActivePowerOfGroup(), byFullInformationIdsIn.get(0).getActivePowerOfGroup());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAmount(), byFullInformationIdsIn.get(0).getAmount());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getKi(), byFullInformationIdsIn.get(0).getKi());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getCosf(), byFullInformationIdsIn.get(0).getCosf());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getTgf(), byFullInformationIdsIn.get(0).getTgf());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAvgDailyActivePower(), byFullInformationIdsIn.get(0).getAvgDailyActivePower());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getAvgDailyReactivePower(), byFullInformationIdsIn.get(0).getAvgDailyReactivePower());

        Assertions.assertEquals(fullStartInformationResponseList.get(1).getFullInformationId(), byFullInformationIdsIn.get(1).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getStartInformationId(), byFullInformationIdsIn.get(1).getStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getName(), byFullInformationIdsIn.get(1).getName());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getActivePowerOfOne(), byFullInformationIdsIn.get(1).getActivePowerOfOne());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getActivePowerOfGroup(), byFullInformationIdsIn.get(1).getActivePowerOfGroup());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAmount(), byFullInformationIdsIn.get(1).getAmount());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getKi(), byFullInformationIdsIn.get(1).getKi());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getCosf(), byFullInformationIdsIn.get(1).getCosf());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getTgf(), byFullInformationIdsIn.get(1).getTgf());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAvgDailyActivePower(), byFullInformationIdsIn.get(1).getAvgDailyActivePower());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getAvgDailyReactivePower(), byFullInformationIdsIn.get(1).getAvgDailyReactivePower());
    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/clearFullStartInformationDB.sql",
            "/sql/addFullInformation.sql", "/sql/addFullStartInformation.sql"})
    public void updateMainBusbar() throws Exception {

        String REQUEST = createFullInformationMainBusbarUpdateRequestAsString();

        MvcResult mvcResult = mockMvc.perform(put("/update/main")
                        .content(REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationResponseDTO fullInformationResponseDTO = objectMapper.readValue(body, FullInformationResponseDTO.class);
        List<FullInformation> fullInformationResponseList = fullInformationResponseDTO.getFullInformationList();

        FullInformation fullInformationRepositoryById = fullInformationRepository.findById((short) 4)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(fullInformationResponseList.get(2).getBusbarName(), fullInformationRepositoryById.getBusbarName());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAmount(), fullInformationRepositoryById.getAmount());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAvgDailyActivePower(), fullInformationRepositoryById.getAvgDailyActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getAvgDailyReactivePower(), fullInformationRepositoryById.getAvgDailyReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getEffectiveAmountOfEquipment(), fullInformationRepositoryById.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(fullInformationResponseList.get(2).getCoefficientMax(), fullInformationRepositoryById.getCoefficientMax());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxActivePower(), fullInformationRepositoryById.getMaxActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxReactivePower(), fullInformationRepositoryById.getMaxReactivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxFullPower(), fullInformationRepositoryById.getMaxFullPower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getMaxElectricCurrent(), fullInformationRepositoryById.getMaxElectricCurrent());
        Assertions.assertEquals(fullInformationResponseList.get(2).getActivePower(), fullInformationRepositoryById.getActivePower());
        Assertions.assertEquals(fullInformationResponseList.get(2).getCosF(), fullInformationRepositoryById.getCosF());
        Assertions.assertEquals(fullInformationResponseList.get(2).getTgF(), fullInformationRepositoryById.getTgF());
        Assertions.assertEquals(fullInformationResponseList.get(2).getKi(), fullInformationRepositoryById.getKi());
        Assertions.assertEquals(fullInformationResponseList.get(2).getModule(), fullInformationRepositoryById.getModule());

    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/addFullInformation.sql"})
    public void getAFullInformationById() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/1"))
                .andExpect(status()
                        .isOk())
                .andReturn();


        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationByIdResponseDTO fullInformationByIdResponseDTO = objectMapper.readValue(body, FullInformationByIdResponseDTO.class);
        FullInformation fullInformationResponse = fullInformationByIdResponseDTO.getFullInformation();


        FullInformation fullInformationRepositoryById = fullInformationRepository.findById((short) 1)
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        Assertions.assertEquals(fullInformationResponse.getBusbarName(), fullInformationRepositoryById.getBusbarName());
        Assertions.assertEquals(fullInformationResponse.getAmount(), fullInformationRepositoryById.getAmount());
        Assertions.assertEquals(fullInformationResponse.getAvgDailyActivePower(), fullInformationRepositoryById.getAvgDailyActivePower());
        Assertions.assertEquals(fullInformationResponse.getAvgDailyReactivePower(), fullInformationRepositoryById.getAvgDailyReactivePower());
        Assertions.assertEquals(fullInformationResponse.getEffectiveAmountOfEquipment(), fullInformationRepositoryById.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(fullInformationResponse.getCoefficientMax(), fullInformationRepositoryById.getCoefficientMax());
        Assertions.assertEquals(fullInformationResponse.getMaxActivePower(), fullInformationRepositoryById.getMaxActivePower());
        Assertions.assertEquals(fullInformationResponse.getMaxReactivePower(), fullInformationRepositoryById.getMaxReactivePower());
        Assertions.assertEquals(fullInformationResponse.getMaxFullPower(), fullInformationRepositoryById.getMaxFullPower());
        Assertions.assertEquals(fullInformationResponse.getMaxElectricCurrent(), fullInformationRepositoryById.getMaxElectricCurrent());
        Assertions.assertEquals(fullInformationResponse.getActivePower(), fullInformationRepositoryById.getActivePower());
        Assertions.assertEquals(fullInformationResponse.getCosF(), fullInformationRepositoryById.getCosF());
        Assertions.assertEquals(fullInformationResponse.getTgF(), fullInformationRepositoryById.getTgF());
        Assertions.assertEquals(fullInformationResponse.getKi(), fullInformationRepositoryById.getKi());
        Assertions.assertEquals(fullInformationResponse.getModule(), fullInformationRepositoryById.getModule());

    }


    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/clearFullStartInformationDB.sql",
            "/sql/addFullInformation.sql", "/sql/addFullStartInformation.sql"})
    public void getAllInformation() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/getAllInformation"))
                .andExpect(status()
                        .isOk())
                .andReturn();


        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();

        FullInformationResponseDTO fullInformationResponseDTO = objectMapper.readValue(body, FullInformationResponseDTO.class);
        List<FullInformation> fullInformationResponseList = fullInformationResponseDTO.getFullInformationList();
        List<FullStartInformation> fullStartInformationResponseList = fullInformationResponseDTO.getFullStartInformationList();

        List<FullInformation> fullInformationRepositoryList = fullInformationRepository.findAll();
        List<FullStartInformation> fullStartInformationRepositoryList = fullStartInformationRepository.findAll();


        Assertions.assertEquals(fullInformationResponseList.get(0).getFullInformationId(),
                fullInformationRepositoryList.get(0).getFullInformationId());
        Assertions.assertEquals(fullInformationResponseList.get(1).getFullInformationId(),
                fullInformationRepositoryList.get(1).getFullInformationId());


        Assertions.assertEquals(fullStartInformationResponseList.get(0).getFullInformationId(),
                fullStartInformationRepositoryList.get(0).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getFullStartInformationId(),
                fullStartInformationRepositoryList.get(0).getFullStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(0).getStartInformationId(),
                fullStartInformationRepositoryList.get(0).getStartInformationId());

        Assertions.assertEquals(fullStartInformationResponseList.get(1).getFullInformationId(),
                fullStartInformationRepositoryList.get(1).getFullInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getFullStartInformationId(),
                fullStartInformationRepositoryList.get(1).getFullStartInformationId());
        Assertions.assertEquals(fullStartInformationResponseList.get(1).getStartInformationId(),
                fullStartInformationRepositoryList.get(1).getStartInformationId());

    }

    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/addFullInformation.sql"})
    public void checkAvailability() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/check/3"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        boolean fromRepository = fullInformationRepository.existsById((short) 3);
        String body = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        boolean fromResponse = objectMapper.readValue(body, Boolean.class);

        Assertions.assertEquals(fromRepository, fromResponse);
    }


    @Test
    @Sql(scripts = {"/sql/clearFullInformationDB.sql", "/sql/addFullInformation.sql"})
    public void deleteBusbar() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/delete/4"))
                .andExpect(status()
                        .isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        List<FullInformation> fullInformationRepositoryById = fullInformationRepository.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        FullInformationResponseDTO fullInformationResponse = objectMapper.readValue(body, FullInformationResponseDTO.class);

        Assertions.assertEquals(fullInformationResponse.getFullInformationList().get(0).getFullInformationId(),
                fullInformationRepositoryById.get(0).getFullInformationId(), (short) 1);
        Assertions.assertEquals(fullInformationResponse.getFullInformationList().get(1).getFullInformationId(),
                fullInformationRepositoryById.get(1).getFullInformationId(), (short) 2);

    }




    private StartInformationResponseDTO createStartInformationServiceClientResponseDTO() {
        StartInformationResponseDTO startInformationResponseDTO = new StartInformationResponseDTO();
        List<StartInformationDTO> startInformationResponseDTOList = new ArrayList<>();

        StartInformationDTO startInformationDTO = new StartInformationDTO((short) 1, "Станок вертикально сверлильный",
                17.5F, (short) 5, 0.16F, 0.5F, 1.73F, 2.8F, 4.84F);

        StartInformationDTO startInformationDTO2 = new StartInformationDTO((short) 2, "Станок заточной",
                7.5F, (short) 2, 0.56F, 0.5F, 1.73F, 4.2F, 7.27F);

        startInformationResponseDTOList.add(startInformationDTO);
        startInformationResponseDTOList.add(startInformationDTO2);

        startInformationResponseDTO.setStartInformationList(startInformationResponseDTOList);

        return startInformationResponseDTO;
    }


    private String createFullInformationRequestAsString() throws JsonProcessingException {

        List<FullStartInformIdRequestDTO> fullStartInformationList = new ArrayList<>();
        fullStartInformationList.add(new FullStartInformIdRequestDTO((short) 1, (short) 1, (short) 5));
        fullStartInformationList.add(new FullStartInformIdRequestDTO((short) 1, (short) 2, (short) 2));

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(new FullInformationRequestDTO((short) 1, "ШРА-1",
                fullStartInformationList));
    }

    private String createFullInformationMainBusbarRequestAsString() throws JsonProcessingException {

        FullInformationMainBusbarRequestDTO requestDTO = new FullInformationMainBusbarRequestDTO();
        requestDTO.setFullInformationId((short) 3);
        requestDTO.setBusbarName("ШМА-1");

        List<Short> numbersBusbarsIncludedInMain = new ArrayList<>();
        numbersBusbarsIncludedInMain.add((short) 1);
        numbersBusbarsIncludedInMain.add((short) 2);

        requestDTO.setNumbersBusbarsIncludedInMain(numbersBusbarsIncludedInMain);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(requestDTO);
    }
    private String createFullInformationMainBusbarUpdateRequestAsString() throws JsonProcessingException {

        FullInformationMainBusbarRequestDTO requestDTO = new FullInformationMainBusbarRequestDTO();
        requestDTO.setFullInformationId((short) 4);
        requestDTO.setBusbarName("ШМА-3");

        List<Short> numbersBusbarsIncludedInMain = new ArrayList<>();
        numbersBusbarsIncludedInMain.add((short) 1);
        numbersBusbarsIncludedInMain.add((short) 2);

        requestDTO.setNumbersBusbarsIncludedInMain(numbersBusbarsIncludedInMain);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(requestDTO);
    }
}