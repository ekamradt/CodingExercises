package com.example.wcf.report;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class WcfReport {

    private WcfReportHeader header;
    private List<WcfReportDetail> details;

    public List<String> reportToList() {
        String headerFormat = "%12s  | %18s | %18s | %20s | %20s | %20s |";
        String detailFormat = "%12s  | %18s | %18s | %20s | %20s | %20s |";

        final ArrayList<String> list = new ArrayList<>();

        list.add(String.format(headerFormat, header.reportHeader().toArray()));
        list.add(String.format(headerFormat, header.reportLine().toArray()));

        list.add("");
        if (details != null && !details.isEmpty()) {
            list.add(String.format(detailFormat, details.get(0).reportHeader().toArray()));
            for (WcfReportDetail detail : details) {
                list.add(String.format(detailFormat, detail.reportLine().toArray()));
            }
        }
        return list;
    }

    public void showReport() {
        outln("*****************************************");
        outln("*** Start of Report                   ***");
        outln("*****************************************");
        reportToList().forEach(this::outln);
        outln("*****************************************");
        outln("*** End of Report                     ***");
        outln("*****************************************");
    }

    private void outln(String s) {
        System.out.println(s);
    }
}
