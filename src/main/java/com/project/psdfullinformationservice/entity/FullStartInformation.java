package com.project.psdfullinformationservice.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "full_start_information")
public class FullStartInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "full_start_information_id")
    private short fullStartInformationId;

    @Column(name = "full_information_id")
    private short fullInformationId;
    @Column(name = "start_information_id")
    private short startInformationId;
    @Column(name = "name")
    private String name;
    @Column(name = "power")
    private float activePowerOfOne;
    @Column(name = "power_of_group")
    private float activePowerOfGroup;
    @Column(name = "amount")
    private short amount;
    @Column(name = "k_i")
    private float ki;
    @Column(name = "cos_f")
    private float cosf;
    @Column(name = "tg_f")
    private float tgf;
    @Column(name = "avg_daily_active_power")
    private float avgDailyActivePower;
    @Column(name = "avg_daily_reactive_power")
    private float avgDailyReactivePower;

    public FullStartInformation(short fullInformationId, short startInformationId, short amount) {
        this.fullInformationId = fullInformationId;
        this.startInformationId = startInformationId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullStartInformation that = (FullStartInformation) o;
        return fullStartInformationId == that.fullStartInformationId && fullInformationId == that.fullInformationId && startInformationId == that.startInformationId && Float.compare(activePowerOfOne, that.activePowerOfOne) == 0 && Float.compare(activePowerOfGroup, that.activePowerOfGroup) == 0 && amount == that.amount && Float.compare(ki, that.ki) == 0 && Float.compare(cosf, that.cosf) == 0 && Float.compare(tgf, that.tgf) == 0 && Float.compare(avgDailyActivePower, that.avgDailyActivePower) == 0 && Float.compare(avgDailyReactivePower, that.avgDailyReactivePower) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullStartInformationId, fullInformationId, startInformationId, name, activePowerOfOne, activePowerOfGroup, amount, ki, cosf, tgf, avgDailyActivePower, avgDailyReactivePower);
    }
}
