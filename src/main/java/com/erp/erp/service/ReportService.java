package com.erp.erp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

@Service
public class ReportService {

    /**
     * Generates a PDF report from a JRXML file and a collection of data.
     *
     * @param jrxmlPath  The path to the JRXML file in resources (e.g.,
     *                   "reports/product_report.jrxml").
     * @param parameters A map of parameters to pass to the report (can be null).
     * @param data       The collection of data to fill the report.
     * @return A byte array representing the generated PDF file.
     * @throws Exception If an error occurs during report generation.
     */
    public byte[] generatePdfReport(String jrxmlPath, Map<String, Object> parameters, Collection<?> data)
            throws Exception {
        // 1. Load the JRXML template from the classpath
        InputStream employeeReportStream = new ClassPathResource(jrxmlPath).getInputStream();

        // 2. Compile the template into a JasperReport object
        JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

        // 3. Create a JRDataSource from the collection
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // 4. Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 5. Export the report to PDF byte array
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
