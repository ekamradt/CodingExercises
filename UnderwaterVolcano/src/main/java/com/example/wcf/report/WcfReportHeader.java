package com.example.wcf.report;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "cell_phone_usage_summary_v")
public class WcfReportHeader implements WcfReportLine {

    @Id
    private Integer id;

    private LocalDate reportDate;
    private Integer numberOfPhones;
    private Integer totalMinutes;
    private Integer totalData;
    private Float averageMinutes;
    private Float averageData;

    @Override
    public List<String> reportHeader() {
        return new ArrayList<String>() {{
            add("Report Date");
            add("Number Of Phones");
            add("Total Minutes");
            add("Total Data");
            add("Average Minutes");
            add("Average Data");
        }};
    }

    @Override
    public List<String> reportLine() {
        return new ArrayList<String>() {{
            add(reportDate.toString());
            add(numberOfPhones.toString());
            add(totalMinutes.toString());
            add(totalData.toString());
            add(averageMinutes.toString());
            add(averageData.toString());
        }};
    }
}
