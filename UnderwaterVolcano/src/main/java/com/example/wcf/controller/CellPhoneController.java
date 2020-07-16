package com.example.wcf.controller;

import com.example.wcf.model.CellPhone;
import com.example.wcf.repo.CellPhoneRepo;
import com.example.wcf.repo.WcfReportRepo;
import com.example.wcf.report.WcfReport;
import com.example.wcf.service.CellPhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api")
public class CellPhoneController {
    final Logger logger = LoggerFactory.getLogger(CellPhoneController.class);

    private final CellPhoneService cellPhoneService;
    private final CellPhoneRepo cellPhoneRepo;
    private final WcfReportRepo reportRepo;

    @Autowired
    public CellPhoneController(CellPhoneService cellPhoneService, CellPhoneRepo cellPhoneRepo, WcfReportRepo wcfReportRepo) {
        this.cellPhoneService = cellPhoneService;
        this.cellPhoneRepo = cellPhoneRepo;
        this.reportRepo = wcfReportRepo;
    }

    @GetMapping("/health")
    public ResponseEntity<String> findAllStates() {
        try {
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            logger.error("Error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/report")
    public ResponseEntity<List<String>> getReport() {
        return runAndReturn(() -> reportRepo.getReport().reportToList(), "Problem with Report List.");
    }

    @GetMapping("/report/json")
    public ResponseEntity<WcfReport> getJsonReport() {
        return runAndReturn(reportRepo::getReport, "Problem with Json Report.");
    }

    @PostMapping("/cellPhone")
    public ResponseEntity<CellPhone> addCellPhone(@RequestBody CellPhone cellPhone) {
        return runAndReturn(() -> cellPhoneRepo.save(cellPhone), "Problem saving Cell Phone");
    }

    @PutMapping("/cellPhone/{id}")
    public ResponseEntity<CellPhone> updateCellPhone(@PathVariable("id") Integer cellPhoneId, @RequestBody CellPhone cellPhone) {
        return runAndReturn(() -> cellPhoneService.updateCellPhone(cellPhoneId, cellPhone), "Problem updating Cell Phone");
    }

    @GetMapping("/cellPhone/{id}")
    public ResponseEntity<CellPhone> getCellPhone(@PathVariable("id") Integer cellPhoneId) {
        try {
            final Optional<CellPhone> cellPhoneOpt = cellPhoneRepo.findById(cellPhoneId);
            return cellPhoneOpt
                    .map(cellPhone -> ResponseEntity.ok().body(cellPhone))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            logger.error("Problem getting Cell Phone", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Example of a convenience method and functional programming.
    private <T> ResponseEntity<T> runAndReturn(Supplier<T> supplier, String msg) {
        try {
            return ResponseEntity.ok().body(supplier.get());
        } catch (Exception e) {
            logger.error("Error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
