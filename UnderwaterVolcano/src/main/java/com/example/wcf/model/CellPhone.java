package com.example.wcf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Accessors(chain = true)
public class CellPhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cellPhoneId;

    private Integer employeeId;
    private String employeeName;
    private String model;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate purchaseDate;

    protected CellPhone() {
    }
}
