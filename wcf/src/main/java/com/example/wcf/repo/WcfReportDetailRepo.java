package com.example.wcf.repo;

import com.example.wcf.report.WcfReportDetail;
import com.example.wcf.report.WcfReportHeader;
import org.assertj.core.util.Lists;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WcfReportDetailRepo extends CrudRepository<WcfReportDetail, Integer> {
}
