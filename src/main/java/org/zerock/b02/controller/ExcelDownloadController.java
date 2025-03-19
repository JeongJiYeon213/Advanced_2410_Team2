package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b02.dto.*;
import org.zerock.b02.service.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelDownloadController {

    private final AdminService adminService;
    private final ProductService productService;
    private final StockService stockService;
    private final InboundService inboundService;
    private final OutboundService outboundService;
    private final SupplierService supplierService;
    private final CustomerService customerService;
    private final ExcelExportService excelExportService;

    @GetMapping("/product/download")
    public ResponseEntity<byte[]> downloadExcelProduct() throws IOException {
        List<ProductDTO> productDTOList = productService.getAllProducts();
        byte[] excelData = excelExportService.generateExcel("product", productDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "product_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/stock/download")
    public ResponseEntity<byte[]> downloadExcelStock() throws IOException {
        List<StockDTO> stockDTOList = stockService.getAllStocks();
        byte[] excelData = excelExportService.generateExcel("stock", stockDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "stock_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/admin/download")
    public ResponseEntity<byte[]> downloadExcelAdmin() throws IOException {
        List<AdminDTO> adminDTOList = adminService.getAllAdmins();
        byte[] excelData = excelExportService.generateExcel("admin", adminDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "admin_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/inbound/download")
    public ResponseEntity<byte[]> downloadExcelInbound() throws IOException {
        List<InboundDTO> inboundDTOList = inboundService.getAllInbound();
        byte[] excelData = excelExportService.generateExcel("inbound", inboundDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "inbound_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/outbound/download")
    public ResponseEntity<byte[]> downloadExcelOutbound() throws IOException {
        List<OutboundDTO> outboundDTOList = outboundService.getAllOutbound();
        byte[] excelData = excelExportService.generateExcel("outbound", outboundDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "outbound_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/supplier/download")
    public ResponseEntity<byte[]> downloadExcelSupplier() throws IOException {
        List<SupplierDTO> supplierDTOList = supplierService.getAllSuppliers();
        byte[] excelData = excelExportService.generateExcel("supplier", supplierDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "supplier_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    @GetMapping("/customer/download")
    public ResponseEntity<byte[]> downloadExcelCustomer() throws IOException {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        byte[] excelData = excelExportService.generateExcel("customer", customerDTOList);

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String fileName = "customer_" + currentDate + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }
}