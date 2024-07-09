package psdfullinformationservice.service;

import com.project.psdfullinformationservice.calculation.FullInformationCalculation;
import com.project.psdfullinformationservice.entity.FullInformation;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import com.project.psdfullinformationservice.repository.FullInformationRepository;
import com.project.psdfullinformationservice.repository.FullStartInformationRepository;
import com.project.psdfullinformationservice.rest.*;
import com.project.psdfullinformationservice.service.FullInformationService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FullInformationServiceTest {

    @Mock
    private FullInformationRepository fullInformationRepository;
    @Mock
    private StartInformationServiceClient startInformationServiceClient;
    @Mock
    private CompensationDeviceServiceClient compensationDeviceServiceClient;
    @Mock
    private PowerTransformerSelectionServiceClient powerTransformerSelectionServiceClient;
    @Mock
    private FullStartInformationRepository fullStartInformationRepository;
    @InjectMocks
    private FullInformationService fullInformationService;




    @Test
    public void fullInformationService_saveMainBusbar() {

        List<FullInformation> fullInformationList = createFullInformationList();

        List<FullStartInformation> allEquipmentsActivePowerList = new ArrayList<>();
        FullStartInformation fullStartInformation = new FullStartInformation();
        fullStartInformation.setActivePowerOfOne(55.0F);
        FullStartInformation fullStartInformation2 = new FullStartInformation();
        fullStartInformation2.setActivePowerOfOne(1.5F);
        allEquipmentsActivePowerList.add(fullStartInformation);
        allEquipmentsActivePowerList.add(fullStartInformation2);

        FullInformationCalculation fullInformationCalculation = new FullInformationCalculation();

        FullInformation fullInformation = fullInformationCalculation
                .calculationMainBusbar(fullInformationList, (short) 3, "ШМА-1", allEquipmentsActivePowerList);


        Assertions.assertEquals("ШМА-1", fullInformation.getBusbarName());
        Assertions.assertEquals((short)14, fullInformation.getAmount());
        Assertions.assertEquals(2.58F, fullInformation.getAvgDailyActivePower());
        Assertions.assertEquals(3.08F, fullInformation.getAvgDailyReactivePower());
        Assertions.assertEquals((short)14, fullInformation.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(2.24F, fullInformation.getCoefficientMax());
        Assertions.assertEquals(5.78F, fullInformation.getMaxActivePower());
        Assertions.assertEquals(3.08F, fullInformation.getMaxReactivePower());
        Assertions.assertEquals(6.55F, fullInformation.getMaxFullPower());
        Assertions.assertEquals(9.95F, fullInformation.getMaxElectricCurrent());
        Assertions.assertEquals(36.0F, fullInformation.getActivePower());
        Assertions.assertEquals(0.64F, fullInformation.getCosF());
        Assertions.assertEquals(1.19F, fullInformation.getTgF());
        Assertions.assertEquals(0.07F, fullInformation.getKi());
        Assertions.assertEquals(36.7F, fullInformation.getModule());

    }







    @Test
    public void fullInformationService_saveNewBusbar() {
        Mockito.when(startInformationServiceClient.getStartInformationByIdList(ArgumentMatchers.anyList()))
                .thenReturn(createStartInformationServiceClientResponseDTO());

        List<FullStartInformation> fullStartInformationList = new ArrayList<>();
        fullStartInformationList.add(new FullStartInformation((short)1, (short) 1, (short) 5));
        fullStartInformationList.add(new FullStartInformation((short)1, (short) 2, (short) 2));

        FullInformationCalculation fullInformationCalculation = new FullInformationCalculation();
        List<FullStartInformation> informationAboutBusbarIncludedEquipment = fullInformationCalculation
                .getInformationAboutBusbarIncludedEquipment(fullStartInformationList, startInformationServiceClient);

        FullInformation fullInformation = fullInformationCalculation
                .calculationNewBusbar((short) 1, "ШРА-1", informationAboutBusbarIncludedEquipment);


        Assertions.assertEquals("ШРА-1", fullInformation.getBusbarName());
        Assertions.assertEquals((short)7, fullInformation.getAmount());
        Assertions.assertEquals(7.0F, fullInformation.getAvgDailyActivePower());
        Assertions.assertEquals(12.11F, fullInformation.getAvgDailyReactivePower());
        Assertions.assertEquals((short)7, fullInformation.getEffectiveAmountOfEquipment());
        Assertions.assertEquals(2.72F, fullInformation.getCoefficientMax());
        Assertions.assertEquals(19.04F, fullInformation.getMaxActivePower());
        Assertions.assertEquals(12.11F, fullInformation.getMaxReactivePower());
        Assertions.assertEquals(22.56F, fullInformation.getMaxFullPower());
        Assertions.assertEquals(34.28F, fullInformation.getMaxElectricCurrent());
        Assertions.assertEquals(102.5F, fullInformation.getActivePower());
        Assertions.assertEquals(0.5F, fullInformation.getCosF());
        Assertions.assertEquals(1.73F, fullInformation.getTgF());
        Assertions.assertEquals(0.07F, fullInformation.getKi());
        Assertions.assertEquals(2.3F, fullInformation.getModule());

    }

    @Test
    public void fullInformationService_getInformationAboutBusbarIncludedEquipment (){
        Mockito.when(startInformationServiceClient.getStartInformationByIdList(ArgumentMatchers.anyList()))
                .thenReturn(createStartInformationServiceClientResponseDTO());

        List<FullStartInformation> fullStartInformationList = new ArrayList<>();
        fullStartInformationList.add(new FullStartInformation((short)1, (short) 1, (short) 5));
        fullStartInformationList.add(new FullStartInformation((short)1, (short) 2, (short) 2));

        FullInformationCalculation fullInformationCalculation = new FullInformationCalculation();
        List<FullStartInformation> informationAboutBusbarIncludedEquipment = fullInformationCalculation
                .getInformationAboutBusbarIncludedEquipment(fullStartInformationList, startInformationServiceClient);

        Assertions.assertEquals((short)1, informationAboutBusbarIncludedEquipment.get(0).getFullInformationId());
        Assertions.assertEquals((short)1, informationAboutBusbarIncludedEquipment.get(0).getStartInformationId());
        Assertions.assertEquals("Станок вертикально сверлильный",
                informationAboutBusbarIncludedEquipment.get(0).getName());
        Assertions.assertEquals(17.5F, informationAboutBusbarIncludedEquipment.get(0).getActivePowerOfOne());
        Assertions.assertEquals(87.5F, informationAboutBusbarIncludedEquipment.get(0).getActivePowerOfGroup());
        Assertions.assertEquals((short)5, informationAboutBusbarIncludedEquipment.get(0).getAmount());
        Assertions.assertEquals(0.16F, informationAboutBusbarIncludedEquipment.get(0).getKi());
        Assertions.assertEquals(0.5F, informationAboutBusbarIncludedEquipment.get(0).getCosf());
        Assertions.assertEquals(1.73F, informationAboutBusbarIncludedEquipment.get(0).getTgf());
        Assertions.assertEquals(2.8F, informationAboutBusbarIncludedEquipment.get(0).getAvgDailyActivePower());
        Assertions.assertEquals(4.84F, informationAboutBusbarIncludedEquipment.get(0).getAvgDailyReactivePower());

        Assertions.assertEquals((short)1, informationAboutBusbarIncludedEquipment.get(1).getFullInformationId());
        Assertions.assertEquals((short)2, informationAboutBusbarIncludedEquipment.get(1).getStartInformationId());
        Assertions.assertEquals("Станок заточной",
                informationAboutBusbarIncludedEquipment.get(1).getName());
        Assertions.assertEquals(7.5F, informationAboutBusbarIncludedEquipment.get(1).getActivePowerOfOne());
        Assertions.assertEquals(15.0F, informationAboutBusbarIncludedEquipment.get(1).getActivePowerOfGroup());
        Assertions.assertEquals((short)2, informationAboutBusbarIncludedEquipment.get(1).getAmount());
        Assertions.assertEquals(0.56F, informationAboutBusbarIncludedEquipment.get(1).getKi());
        Assertions.assertEquals(0.5F, informationAboutBusbarIncludedEquipment.get(1).getCosf());
        Assertions.assertEquals(1.73F, informationAboutBusbarIncludedEquipment.get(1).getTgf());
        Assertions.assertEquals(4.2F, informationAboutBusbarIncludedEquipment.get(1).getAvgDailyActivePower());
        Assertions.assertEquals(7.27F, informationAboutBusbarIncludedEquipment.get(1).getAvgDailyReactivePower());

    }




    private StartInformationResponseDTO createStartInformationServiceClientResponseDTO(){
        StartInformationResponseDTO startInformationResponseDTO = new StartInformationResponseDTO();
        List<StartInformationDTO> startInformationResponseDTOList = new ArrayList<>();

            StartInformationDTO startInformationDTO = new StartInformationDTO((short) 1,"Станок вертикально сверлильный",
                    17.5F, (short)5, 0.16F,0.5F,1.73F, 2.8F,4.84F);

            StartInformationDTO startInformationDTO2 = new StartInformationDTO((short) 2,"Станок заточной",
                    7.5F, (short)2, 0.56F,0.5F,1.73F, 4.2F,7.27F);

        startInformationResponseDTOList.add(startInformationDTO);
        startInformationResponseDTOList.add(startInformationDTO2);

        startInformationResponseDTO.setStartInformationList(startInformationResponseDTOList);

        return startInformationResponseDTO;
    }


    private List<FullInformation> createFullInformationList() {

        List<FullInformation> fullInformationList = new ArrayList<>();
        FullInformation fullInformation = new FullInformation((short) 1, "ШРА-1", (short) 5, 0.94F,
                1.09F, (short) 5, 3.23F, 3.04F, 1.09F, 3.23F,
                4.91F, 16.5F, 0.65F, 1.16F, 0.06F, 1);
        FullInformation fullInformation2 = new FullInformation((short) 2, "ШРА-2", (short) 9, 1.64F,
                1.99F, (short) 9, 2.43F, 5.04F, 2.09F, 6.23F,
                6.94F, 19.5F, 0.65F, 1.16F, 0.12F, 2);

        fullInformationList.add(fullInformation);
        fullInformationList.add(fullInformation2);
        return fullInformationList;
    }


}