package com.example.wcf.repo;

import com.example.wcf.report.WcfReportHeader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WcfReportHeaderRepo extends CrudRepository<WcfReportHeader, Integer> {
}
