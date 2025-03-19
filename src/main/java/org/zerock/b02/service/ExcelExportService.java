package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.dto.*;
import org.zerock.b02.repository.InboundRepository;
import org.zerock.b02.repository.OutboundRepository;
import org.zerock.b02.repository.ProductRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ExcelExportService {

    private final ProductRepository productRepository;
    private final InboundRepository inboundRepository;
    private final OutboundRepository outboundRepository;

    public <E> byte[] generateExcel(String type, List<E> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(type);
            createHeader(sheet, workbook, type);
            populateData(sheet, workbook, data, type);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void createHeader(Sheet sheet, Workbook workbook, String type) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        String[] headers;
        if (type.equals("product")) {
            headers = new String[]{"제품코드", "제품명", "제품타입", "무게", "크기", "등록일", "수정일"};
        } else if (type.equals("stock")) {
            headers = new String[]{"제품코드", "현재수량", "재고등록일", "최종수정일"};
        } else if (type.equals("admin")) {
            headers = new String[]{"사원번호", "사원이름", "비밀번호", "전화번호", "직급", "등록일", "수정일"};
        } else if (type.equals("inbound")) {
            headers = new String[]{"입고번호", "제품번호", "거래처명", "수량", "입고일", "상태", "기타"};
        } else if (type.equals("outbound")) {
            headers = new String[]{"출고번호", "제품번호", "고객명", "수량", "출고일", "상태", "기타"};
        } else if (type.equals("supplier")) {
            headers = new String[]{"ID", "거래처명", "전화번호", "등록일"};
        } else if (type.equals("customer")) {
            headers = new String[]{"ID", "고객명", "전화번호", "등록일"};
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }



        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private <E> void populateData(Sheet sheet, Workbook workbook, List<E> data, String type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CellStyle evenRowStyle = workbook.createCellStyle();
        evenRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle oddRowStyle = workbook.createCellStyle();
        oddRowStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int rowIdx = 1;
        for (E obj : data) {
            Row row = sheet.createRow(rowIdx);
            CellStyle rowStyle = (rowIdx % 2 == 0) ? evenRowStyle : oddRowStyle;

            switch (obj.getClass().getName()) {
                case "org.zerock.b02.dto.ProductDTO": {
                    ProductDTO product = (ProductDTO) obj;
                    row.createCell(0).setCellValue(product.getProductCode());
                    row.createCell(1).setCellValue(product.getProductName());
                    row.createCell(2).setCellValue(product.getProductType());
                    row.createCell(3).setCellValue(product.getProductWeight());
                    row.createCell(4).setCellValue(product.getProductSize());
                    row.createCell(5).setCellValue(product.getRegDate().format(formatter));
                    row.createCell(6).setCellValue(product.getModDate().format(formatter));
                    break;
                }

                case "org.zerock.b02.dto.StockDTO": {
                    StockDTO stock = (StockDTO) obj;
                    row.createCell(0).setCellValue(stock.getProductCode());
                    row.createCell(1).setCellValue(stock.getCurrentStock());
                    row.createCell(2).setCellValue(stock.getRegDate().format(formatter));
                    row.createCell(3).setCellValue(stock.getModDate().format(formatter));
                    break;
                }

                case "org.zerock.b02.dto.AdminDTO": {
                    AdminDTO admin = (AdminDTO) obj;
                    row.createCell(0).setCellValue(admin.getAdminId());
                    row.createCell(1).setCellValue(admin.getAdminName());
                    row.createCell(2).setCellValue(admin.getAdminPassword());
                    row.createCell(3).setCellValue(admin.getPhoneNum());
                    row.createCell(4).setCellValue(admin.getPosition());
                    row.createCell(5).setCellValue(
                            admin.getRegDate() != null ? admin.getRegDate().format(formatter) : ""
                    );
                    row.createCell(6).setCellValue(
                            admin.getModDate() != null ? admin.getModDate().format(formatter) : ""
                    );

                    break;
                }

                case "org.zerock.b02.dto.InboundDTO": {
                    InboundDTO inboundDTO = (InboundDTO) obj;
                    Long inboundId = inboundDTO.getInboundId();
                    Optional<Inbound> inboundResult = inboundRepository.findById(inboundId);
                    Inbound inbound = inboundResult.orElseThrow(() -> new RuntimeException("Inbound not found"));
                    inboundDTO.setProductCode(inbound.getProduct().getProductCode());
                    inboundDTO.setSupplierId(inbound.getSupplier().getSupplierId());

                    row.createCell(0).setCellValue(inboundDTO.getInboundCode());
                    row.createCell(1).setCellValue(inboundDTO.getProductCode());
                    row.createCell(2).setCellValue(inboundDTO.getSupplierId());
                    row.createCell(3).setCellValue(inboundDTO.getQuantity());
                    row.createCell(4).setCellValue(inboundDTO.getInboundDate().format(formatter));
                    row.createCell(5).setCellValue(inboundDTO.getInboundStatus());
                    row.createCell(6).setCellValue(inboundDTO.getDescription());
                    break;
                }

                case "org.zerock.b02.dto.OutboundDTO": {
                    OutboundDTO outboundDTO = (OutboundDTO) obj;
                    Long outboundId = outboundDTO.getOutboundId();
                    Optional<Outbound> outboundResult = outboundRepository.findById(outboundId);
                    Outbound outbound = outboundResult.orElseThrow(() -> new RuntimeException("Outbound not found"));
                    outboundDTO.setProductCode(outbound.getProduct().getProductCode());
                    outboundDTO.setCustomerId(outbound.getCustomer().getCustomerId());

                    row.createCell(0).setCellValue(outboundDTO.getOutboundCode());
                    row.createCell(1).setCellValue(outboundDTO.getProductCode());
                    row.createCell(2).setCellValue(outboundDTO.getCustomerId());
                    row.createCell(3).setCellValue(outboundDTO.getQuantity());
                    row.createCell(4).setCellValue(outboundDTO.getOutboundDate().format(formatter));
                    row.createCell(5).setCellValue(outboundDTO.getOutboundStatus());
                    row.createCell(6).setCellValue(outboundDTO.getDescription());
                    break;
                }

                case "org.zerock.b02.dto.SupplierDTO": {
                    SupplierDTO supplier = (SupplierDTO) obj;
                    row.createCell(0).setCellValue(supplier.getSupplierId());
                    row.createCell(1).setCellValue(supplier.getSupplierName());
                    row.createCell(2).setCellValue(supplier.getSupplierInfo());
                    row.createCell(3).setCellValue(supplier.getRegDate().format(formatter));
                    break;
                }

                case "org.zerock.b02.dto.CustomerDTO": {
                    CustomerDTO customer = (CustomerDTO) obj;
                    row.createCell(0).setCellValue(customer.getCustomerId());
                    row.createCell(1).setCellValue(customer.getCustomerName());
                    row.createCell(2).setCellValue(customer.getCustomerInfo());
                    row.createCell(3).setCellValue(customer.getRegDate().format(formatter));
                    break;
                }

                default:
                    throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
            }

            // 스타일 적용
            for (int i = 0; i < sheet.getRow(rowIdx).getLastCellNum(); i++) {
                sheet.getRow(rowIdx).getCell(i).setCellStyle(rowStyle);
            }
            rowIdx++;
        }

        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, currentWidth + 256 * 2);
        }
    }
}