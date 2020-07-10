package com.example.wcf;

import com.example.wcf.model.CellPhone;
import com.example.wcf.model.CellUsageMonth;
import com.example.wcf.repo.*;
import com.example.wcf.report.WcfReport;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WcfApplication {

    public static final String CSV_FILE_PATH_PREFIX = "src/main/resources/";

    // These days people prefer a Autowired constructor. I did this for speed to make this example.
    @Autowired
    private CellPhoneRepo cellPhoneRepo;

    @Autowired
    private CellUsageMonthRepo cellUsageMonthRepo;

    @Autowired
    private WcfReportHeaderRepo headerRepo;

    @Autowired
    private WcfReportDetailRepo detailRepo;

    @Autowired
    private WcfReportRepo reportRepo;

    private CsvMapper csvMapper = new CsvMapper();

    public static void main(String[] args) {
        SpringApplication.run(WcfApplication.class, args);
    }

    @PostConstruct
    public void setup() {
        // To parse a value into LocalDate from an input String.  And other date types for that matter.
        csvMapper.registerModule(new JavaTimeModule());
    }

    // Show report upon startup of the application
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        readFileIntoDatabase("CellPhone.csv", CellPhone.class, cellPhoneRepo);
        readFileIntoDatabase("CellPhoneUsageByMonth.csv", CellUsageMonth.class, cellUsageMonthRepo);

        final WcfReport report = reportRepo.getReport();
        report.showReport();
    }

    // Read a sfile and write a list of Entities to the database.
    private <T> void readFileIntoDatabase(String csvFilename, Class<T> entityClass, CrudRepository<T, ?> repo) {
        try {
            final List<T> entities = readWholeFileIntoList(csvFilename, entityClass);
            final Iterable<T> s = repo.saveAll(entities);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // Read the input file into a list of Entities.
    private <T> List<T> readWholeFileIntoList(String csvFilename, Class<T> entityClass) throws IOException {
        final List<T> entities = new ArrayList<>();
        final CsvSchema schema = CsvSchema.emptySchema().withHeader();
        final ObjectReader oReader = csvMapper.reader(entityClass).with(schema);
        try (Reader reader = new FileReader(CSV_FILE_PATH_PREFIX + csvFilename)) {
            final MappingIterator<T> objectMappingIterator = oReader.readValues(reader);
            while (objectMappingIterator.hasNext()) {
                final T next = objectMappingIterator.next();
                entities.add(next);
            }
        }
        return entities;
    }
}
