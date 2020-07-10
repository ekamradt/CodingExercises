package com.example.wcf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Accessors(chain = true)
public class CellUsageMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cellUsageMonthId;

    private Integer employeeId;
    private Integer totalMinutes;
    private Float totalData;

    @JsonFormat(pattern = "M/d/yyyy")
    private LocalDate usageDate;

    protected CellUsageMonth() {
    }
}
