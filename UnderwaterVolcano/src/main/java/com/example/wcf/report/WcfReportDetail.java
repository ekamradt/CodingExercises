package com.example.wcf.report;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "cell_phone_summary_v")
public class WcfReportDetail implements WcfReportLine {

    @Id
    private Integer id;

    private Integer employeeId;
    private String employeeName;
    private String model;
    private LocalDate purchaseDate;
    private Integer minutesUsage;
    private Integer dataUsage;

    @Override
    public List<String> reportHeader() {
        return new ArrayList<String>() {{
            add("Employee Id");
            add("Employee Name");
            add("Model");
            add("Purchase Date");
            add("Minutes Usage");
            add("Data Usage");
        }};
    }

    @Override
    public List<String> reportLine() {
        final List<String> list = new ArrayList<String>();

        list.add(employeeId.toString());
        list.add(employeeName);
        list.add(model);
        if (purchaseDate != null) {
            list.add(purchaseDate.toString());
        }
        if (minutesUsage != null){
            list.add(minutesUsage.toString());
        }
        if (dataUsage != null){
            list.add(dataUsage.toString());
        }
        return list;
    }
}
