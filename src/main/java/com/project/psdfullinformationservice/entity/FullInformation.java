package com.project.psdfullinformationservice.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "full_information")
public class FullInformation {
    @Id
    @Column(name = "full_information_id")
    private short fullInformationId;
    @Column(name = "busbar_name")
    private String busbarName;
    @Column(name = "amount")
    private short amount;
    @Column(name = "avg_daily_active_power")
    private float avgDailyActivePower;
    @Column(name = "avg_daily_reactive_power")
    private float avgDailyReactivePower;
    @Column(name = "effective_amount_of_equipment")
    private short effectiveAmountOfEquipment;
    @Column(name = "coefficient_max")
    private float coefficientMax;
    @Column(name = "max_active_power")
    private float maxActivePower;
    @Column(name = "max_reactive_power")
    private float maxReactivePower;
    @Column(name = "max_full_power")
    private float maxFullPower;
    @Column(name = "max_electric_current")
    private float maxElectricCurrent;
    @Column(name = "power_of_group")
    private float activePower;
    @Column(name = "cos_f")
    private float cosF;
    @Column(name = "tg_f")
    private float tgF;
    @Column(name = "k_i")
    private float ki;
    @Column(name = "module")
    private float module;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullInformation that = (FullInformation) o;
        return fullInformationId == that.fullInformationId && amount == that.amount && Float.compare(avgDailyActivePower, that.avgDailyActivePower) == 0 && Float.compare(avgDailyReactivePower, that.avgDailyReactivePower) == 0 && effectiveAmountOfEquipment == that.effectiveAmountOfEquipment && Float.compare(coefficientMax, that.coefficientMax) == 0 && Float.compare(maxActivePower, that.maxActivePower) == 0 && Float.compare(maxReactivePower, that.maxReactivePower) == 0 && Float.compare(maxFullPower, that.maxFullPower) == 0 && Float.compare(maxElectricCurrent, that.maxElectricCurrent) == 0 && Float.compare(activePower, that.activePower) == 0 && Float.compare(cosF, that.cosF) == 0 && Float.compare(tgF, that.tgF) == 0 && Float.compare(ki, that.ki) == 0 && Float.compare(module, that.module) == 0 && Objects.equals(busbarName, that.busbarName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullInformationId, busbarName, amount, avgDailyActivePower, avgDailyReactivePower, effectiveAmountOfEquipment, coefficientMax, maxActivePower, maxReactivePower, maxFullPower, maxElectricCurrent, activePower, cosF, tgF, ki, module);
    }
}
