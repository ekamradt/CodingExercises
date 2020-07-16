package com.example.wcf.repo;

import com.example.wcf.report.WcfReport;
import com.example.wcf.report.WcfReportDetail;
import com.example.wcf.report.WcfReportHeader;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Getter
@Accessors(chain = true)
@Service
public class WcfReportRepo {

    private WcfReportHeaderRepo headerRepo;
    private WcfReportDetailRepo detailRepo;

    private WcfReportRepo() {
    }

    @Autowired
    public WcfReportRepo(WcfReportHeaderRepo headerRepo, WcfReportDetailRepo detailRepo) {
        this.headerRepo = headerRepo;
        this.detailRepo = detailRepo;
    }

    public WcfReport getReport() {
        final Iterable<WcfReportHeader> all = headerRepo.findAll();
        final Optional<WcfReportHeader> first = Lists.newArrayList(all).stream().findFirst();
        return first.map(wcfReportHeader -> {
            final Iterable<WcfReportDetail> details = detailRepo.findAll();
            return new WcfReport()
                    .setHeader(wcfReportHeader)
                    .setDetails(Lists.newArrayList(details));
        })
                .orElse(null);
    }
}