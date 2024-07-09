package com.project.psdfullinformationservice.calculation;


import com.project.psdfullinformationservice.entity.FullInformation;
import com.project.psdfullinformationservice.entity.FullStartInformation;
import com.project.psdfullinformationservice.rest.StartInformationDTO;
import com.project.psdfullinformationservice.rest.StartInformationResponseDTO;
import com.project.psdfullinformationservice.rest.StartInformationServiceClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FullInformationCalculation {

    public FullInformation calculationNewBusbar(short fullInformationId, String nameOfBusbar,
                                                List<FullStartInformation> informationAboutBusbarIncludedEquipment) {

        short amountTotal = informationAboutBusbarIncludedEquipment.stream()
                .map(FullStartInformation::getAmount)
                .reduce((a, b) -> (short) (a + b))
                .orElseThrow(() -> new NoSuchElementException("No value present")); // amount of equipment in busbar

        float activePowerTotal = informationAboutBusbarIncludedEquipment.stream()
                .map((a) -> a.getActivePowerOfOne() * a.getAmount())
                .reduce(Float::sum)
                .orElseThrow(() -> new NoSuchElementException("No value present")); // active power of all groups included in the busbar


        float avgDailyActivePowerTotal = Math.round(
                informationAboutBusbarIncludedEquipment.stream()
                        .map(FullStartInformation::getAvgDailyActivePower)
                        .reduce(Float::sum)
                        .orElseThrow(() -> new NoSuchElementException("No value present"))
                        * 100.0) / 100.0F; // average daily active power of all groups included in the busbar

        float avgDailyReactivePowerTotal = Math.round(
                informationAboutBusbarIncludedEquipment.stream()
                        .map(FullStartInformation::getAvgDailyReactivePower)
                        .reduce(Float::sum)
                        .orElseThrow(() -> new NoSuchElementException("No value present"))
                        * 100.0) / 100.0F; // average daily reactive power of all groups included in the busbar

        float module = module(informationAboutBusbarIncludedEquipment.stream()
                .map(FullStartInformation::getActivePowerOfOne)
                .collect(Collectors.toList())); // module of the current busbar

        float kI = (Math.round((avgDailyActivePowerTotal / activePowerTotal) * 100.0) / 100.0F); // utilization factor

        float tgF = (Math.round((avgDailyReactivePowerTotal / avgDailyActivePowerTotal) * 100.0) / 100.0F); // tgf

        float cosF = (Math.round(Math.sqrt(1 / (1 + Math.pow(tgF, 2))) * 100.0) / 100.0F); //cosf

        short effectiveAmountOfEquipment;// effective amount of equipment

        if (amountTotal > 5 && kI > 0.2 && module > 3) {
            List<Float> list = informationAboutBusbarIncludedEquipment.stream()
                    .map(FullStartInformation::getActivePowerOfOne).toList(); // take all "power" from equipment list of the current busbar
            double maxPowerOfOne = list.stream().max(Float::compareTo)
                    .orElseThrow(() -> new NoSuchElementException("No value present"));
            effectiveAmountOfEquipment = (short) (2 * activePowerTotal / maxPowerOfOne);
        } else if (amountTotal < 5 && kI > 0.2 && module > 3) {
            short aShort = informationAboutBusbarIncludedEquipment.stream()
                    .map((e) -> (short) (Math.pow(e.getActivePowerOfOne(), 2) * e.getAmount()))
                    .reduce((a, b) -> (short) (a + b))
                    .orElseThrow(() -> new NoSuchElementException("No value present"));
            effectiveAmountOfEquipment = (short) (Math.pow(activePowerTotal, 2) * aShort);
        } else {
            effectiveAmountOfEquipment = amountTotal;
        }

        return getFullInformation(fullInformationId, nameOfBusbar, amountTotal, activePowerTotal, avgDailyActivePowerTotal, avgDailyReactivePowerTotal, module, kI, tgF, cosF, effectiveAmountOfEquipment);
    }

    public List<FullStartInformation> getInformationAboutBusbarIncludedEquipment(List<FullStartInformation> numbersAtAmountOfEquipments,
                                                                                 StartInformationServiceClient startInformationServiceClient) {
        HashMap<Short, Short> numbersAndAmountOfEquipments = new HashMap<>();
        numbersAtAmountOfEquipments
                .forEach((e) -> numbersAndAmountOfEquipments.put(e.getStartInformationId(), e.getAmount()));

        List<Short> numbersOfEquipments = new ArrayList<>(numbersAndAmountOfEquipments.keySet());

        List<FullStartInformation> fullStartInformationList = new ArrayList<>();

        StartInformationResponseDTO startInformationByIdList = startInformationServiceClient
                .getStartInformationByIdList(numbersOfEquipments);

        List<StartInformationDTO> startInformationDTOList = startInformationByIdList.getStartInformationList();

        for (int i = 0; i < numbersOfEquipments.size(); i++) {
            fullStartInformationList.add(getFullStartInformation(numbersAtAmountOfEquipments, i, startInformationDTOList.get(i)));
        }
        return fullStartInformationList;
    }

    private FullStartInformation getFullStartInformation(List<FullStartInformation> fullStartInformation, int i, StartInformationDTO startInformationDTO) {
        FullStartInformation fullStartInformId1 = new FullStartInformation();
        fullStartInformId1.setFullInformationId(fullStartInformation.get(i).getFullInformationId());
        fullStartInformId1.setStartInformationId(fullStartInformation.get(i).getStartInformationId());
        fullStartInformId1.setName(startInformationDTO.getName());
        fullStartInformId1.setActivePowerOfOne(startInformationDTO.getActivePower());
        fullStartInformId1.setActivePowerOfGroup(startInformationDTO.getActivePower() * startInformationDTO.getAmount());
        fullStartInformId1.setAmount(startInformationDTO.getAmount());
        fullStartInformId1.setKi(startInformationDTO.getKi());
        fullStartInformId1.setCosf(startInformationDTO.getCosf());
        fullStartInformId1.setTgf(startInformationDTO.getTgf());
        fullStartInformId1.setAvgDailyActivePower(startInformationDTO.getAvgDailyActivePower());
        fullStartInformId1.setAvgDailyReactivePower(startInformationDTO.getAvgDailyReactivePower());
        return fullStartInformId1;
    }


    public FullInformation calculationMainBusbar(List<FullInformation> fullInformationList,short id, String nameOfBusbar,
                                                 List<FullStartInformation> allEquipmentsActivePowerList) {


        short amount = fullInformationList.stream()
                .map(FullInformation::getAmount)
                .reduce((a, b) -> (short) (a + b))
                .orElseThrow(() -> new NoSuchElementException("No value present")); // amount of equipment in all busbar

        float powerOfGroup = fullInformationList.stream()
                .map(FullInformation::getActivePower)
                .reduce(Float::sum)
                .orElseThrow(() -> new NoSuchElementException("No value present")); // active power of all groups included in the busbar


        float avgDailyActivePower = fullInformationList.stream()
                .map(FullInformation::getAvgDailyActivePower)
                .reduce(Float::sum)
                .orElseThrow(() -> new NoSuchElementException("No value present")); // average daily active power of all groups included in the busbar

        float avgDailyReactivePower = fullInformationList.stream()
                .map(FullInformation::getAvgDailyReactivePower)
                .reduce(Float::sum)
                .orElseThrow(() -> new NoSuchElementException("No value present")); // average daily reactive power of all groups included in the busbar


        float module = module(allEquipmentsActivePowerList.stream()
                .map(FullStartInformation::getActivePowerOfOne)
                .collect(Collectors.toList())); // module of the current busbar

        float kI = (float) (Math.round((avgDailyActivePower / powerOfGroup) * 100.0) / 100.0); // utilization factor

        float tgF = (float) (Math.round((avgDailyReactivePower / avgDailyActivePower) * 100.0) / 100.0); // tgf

        float cosF = (float) (Math.round(Math.sqrt(1 / (1 + Math.pow(tgF, 2))) * 100.0) / 100.0); //cosf


        short effectiveAmountOfEquipment;// effective amount of equipment

        if (amount > 5 && kI > 0.2 && module > 3) {
            List<Float> list2 = fullInformationList.stream()
                    .map(FullInformation::getActivePower)
                    .toList(); // take all "power" from equipment list of the current busbar
            float maxPowerOfOne = list2.stream().max(Float::compareTo).orElseThrow(() -> new NoSuchElementException("No value present"));
            effectiveAmountOfEquipment = (short) (2 * powerOfGroup / maxPowerOfOne);
        } else if (amount < 5 && kI > 0.2 && module > 3) {
            float aFloat = fullInformationList.stream()
                    .map((e) -> (float) Math.pow(e.getActivePower(), 2) * e.getAmount())
                    .reduce(Float::sum)
                    .orElseThrow(() -> new NoSuchElementException("No value present"));
            effectiveAmountOfEquipment = (short) (Math.pow(powerOfGroup, 2) * aFloat);
        } else {
            effectiveAmountOfEquipment = fullInformationList.stream()
                    .map(FullInformation::getAmount)
                    .reduce((a, b) -> (short) (a + b))
                    .orElseThrow(() -> new NoSuchElementException("No value present"));
        }

        return getFullInformation(id, nameOfBusbar, amount, powerOfGroup, avgDailyActivePower, avgDailyReactivePower, module, kI, tgF, cosF, effectiveAmountOfEquipment);
    }


    private FullInformation getFullInformation(short id, String nameOfBusbar, short amount, float powerOfGroup, float avgDailyActivePower, float avgDailyReactivePower, float module, float kI, float tgF, float cosF, short effectiveAmountOfEquipment) {
        float coefficientMax = kMax(kI, amount); // coefficient max of the current busbar

        float maxActivePower = (float) (Math.round((avgDailyActivePower * coefficientMax) * 100.0) / 100.0);// max active power of the current busbar

        float maxReactivePower = avgDailyReactivePower; /* max reactive power of the current busbar (at amount > 10)
        At amount <= 10 the formula need to change (in the future)*/

        float maxFullPower = (float) (Math.round(Math.sqrt(Math.pow(maxActivePower, 2) +
                Math.pow(maxReactivePower, 2)) * 100.0) / 100.0); // max full power of the current busbar

        float maxElectricCurrent = (float) (Math.round(((maxFullPower * 1000) / (Math.sqrt(3) * 380)) * 100) / 100.0); // max electric current of this busbar


        return new FullInformation(id, nameOfBusbar, amount,
                avgDailyActivePower, avgDailyReactivePower, effectiveAmountOfEquipment,
                coefficientMax, maxActivePower, maxReactivePower, maxFullPower,
                maxElectricCurrent, powerOfGroup, cosF, tgF, kI, module);
    }


    private float module(List<Float> allEquipmentsActivePowerList) {

        float min = allEquipmentsActivePowerList.stream()
                .min(Float::compareTo)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        float max = allEquipmentsActivePowerList.stream()
                .max(Float::compareTo)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        return Math.round((max / min) * 10.0) / 10.0F;  // round to one argument after point
    }


    private float kMax(float kI, short amount) {
        short b = 0; // for selection from one-dimensional arrays ( at values of kI {0 - 0.1},{0.11 - 0.2},{0.21 - 0.4})
        short j = 0; // for selection from two-dimensional arrays ( at values of amount {0-6, 7-9, 10-14, 16-20, 21-40, 41-70, 71-100, 101-160, 161-200})

        float[][] kMax = {{3.23f, 2.72f, 2.24f, 1.91f, 1.62f, 1.34f, 1.23f, 1.17f, 1.05f},
                {2.42f, 1.99f, 1.75f, 1.55f, 1.34f, 1.19f, 1.13f, 1.1f, 1.06f},
                {1.76f, 1.52f, 1.36f, 1.26f, 1.19f, 1.12f, 1.09f, 1.06f, 1.05f}};//(it's not a full values) full values in the table from https://www.calc.ru/Raschetnaya-Nagruzka.html


        if (kI <= 0.1) {
            b = 0;
        } else if (kI <= 0.2) {
            b = 1;
        } else if (kI > 0.2) {
            b = 2;
        }

        if (amount <= 6) {
            b = 0;
        } else if (amount <= 9) {
            j = 1;
        } else if (amount <= 14) {
            j = 2;
        } else if (amount <= 20) {
            j = 3;
        } else if (amount <= 40) {
            j = 4;
        } else if (amount <= 70) {
            j = 5;
        } else if (amount <= 100) {
            j = 6;
        } else if (amount <= 160) {
            j = 7;
        } else {
            j = 8;
        } // use "else" if amount will be more than 160 \

        return (float) (Math.round((kMax[b][j]) * 100.0) / 100.0); //  round to two argument after point
    }


}
