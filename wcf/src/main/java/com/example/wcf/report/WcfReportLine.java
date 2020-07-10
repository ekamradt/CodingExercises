package com.example.wcf.report;

import java.util.List;

public interface WcfReportLine {
    List<String> reportHeader();
    List<String> reportLine();
}
