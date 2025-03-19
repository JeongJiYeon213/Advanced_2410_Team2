package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Inbound;
import org.zerock.b02.domain.Product;

import org.zerock.b02.domain.Supplier;
import org.zerock.b02.dto.InboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.InboundRepository;
import org.zerock.b02.repository.ProductRepository;
import org.zerock.b02.repository.SupplierRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class InboundServiceImpl implements InboundService{
    private final ModelMapper modelMapper;
    private final InboundRepository inboundRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public Long register(InboundDTO inboundDTO){

        Optional<Inbound> lastInbound = inboundRepository.findTopByOrderByInboundCodeDesc();

        String newInboundCode = generateNewInboundCode(lastInbound);

        inboundDTO.setInboundCode(newInboundCode);

        String[] productCodes = inboundDTO.getProductCode().split(",");
        String[] supplierIds = inboundDTO.getSupplierId().split(",");
        String[] inboundStatuses = inboundDTO.getInboundStatus().split(",");
        String[] descriptions = inboundDTO.getDescription().split(",");
        Long quantities = inboundDTO.getQuantity();
        LocalDateTime inboundDates = inboundDTO.getInboundDate();

        log.info("inboundstatus: " + Arrays.toString(inboundStatuses));
        log.info("descriptions: " + Arrays.toString(descriptions));
        log.info("quantities: " + quantities);
        log.info("inboundDates: " + inboundDates);

        for (int i = 0; i < productCodes.length; i++) {
            String productCode = productCodes[i].trim();
            String supplierId = supplierIds[i].trim();
            String inboundStatus = inboundStatuses[i].trim();
            String description = descriptions[i].trim();

            Product product = productRepository.findByProductCode(productCode)
                    .orElseThrow(() -> new RuntimeException("Product not found with productCode: " + productCode));

            Supplier supplier = supplierRepository.findBySupplierId(supplierId)
                    .orElseThrow(() -> new RuntimeException("Supplier not found with supplierId: " + supplierId));

            lastInbound = inboundRepository.findTopByOrderByInboundCodeDesc();

            newInboundCode = generateNewInboundCode(lastInbound);

            inboundDTO.setInboundCode(newInboundCode);

            Inbound inbound = modelMapper.map(inboundDTO, Inbound.class);
            inbound.setProduct(product);
            inbound.setSupplier(supplier);
            inbound.setInboundStatus(inboundStatus);
            inbound.setDescription(description);
            inbound.setQuantity(quantities);
            inbound.setInboundDate(inboundDates);

            inboundRepository.save(inbound);
        }
        return 1L;
    }

    private String generateNewInboundCode(Optional<Inbound> lastInbound) {
        String newInboundCode = "I001";  // 기본값

        if (lastInbound.isPresent()) {
            String lastInboundCode = lastInbound.get().getInboundCode();
            int lastCodeNumber = Integer.parseInt(lastInboundCode.substring(1));
            newInboundCode = "I" + String.format("%03d", lastCodeNumber + 1);
        }

        return newInboundCode;
    }

    @Override
    public InboundDTO readOne(Long inboundId){
        Optional<Inbound> result = inboundRepository.findById(inboundId);
        Inbound inbound = result.orElseThrow(() -> new RuntimeException("Inbound not found"));

        InboundDTO inboundDTO = modelMapper.map(inbound, InboundDTO.class);

        if (inbound.getProduct() != null) {
            inboundDTO.setProductCode(inbound.getProduct().getProductCode());
        }
        if (inbound.getSupplier() != null) {
            inboundDTO.setSupplierId(inbound.getSupplier().getSupplierId());
        }
        return inboundDTO;
    }

    @Override
    public void modify(InboundDTO inboundDTO){
        Optional<Inbound> result = inboundRepository.findById(inboundDTO.getInboundId());
        Inbound inbound = result.orElseThrow(() -> new RuntimeException("Inbound not found"));

        Product newProduct = productRepository.findByProductCode(inboundDTO.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found with productCode"));
        Supplier newSupplier = supplierRepository.findBySupplierId(inboundDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with supplierId"));

        inbound.change(newProduct,
                inboundDTO.getInboundCode(),
                newSupplier,
                inboundDTO.getQuantity(),
                inboundDTO.getInboundDate(),
                inboundDTO.getDescription(),
                inboundDTO.getInboundStatus());

        inboundRepository.save(inbound);
    }

    @Override
    public void remove(Long inboundId){
        inboundRepository.deleteById(inboundId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("inboundId");

        LocalDateTime from = pageRequestDTO.getFrom();
        LocalDateTime to = pageRequestDTO.getTo();

        Page<Inbound> result = inboundRepository.searchAll(types, keyword, pageable, from, to);

        List<InboundDTO> dtoList = result.getContent().stream().map(inbound -> {
            InboundDTO inboundDTO = modelMapper.map(inbound, InboundDTO.class);

            if (inbound.getProduct() != null) {
                inboundDTO.setProductCode(inbound.getProduct().getProductCode());
            }
            if (inbound.getSupplier() != null) {
                inboundDTO.setSupplierId(inbound.getSupplier().getSupplierId());
            }

            return inboundDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<InboundDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<InboundDTO> getAllInbound() {
        List<Inbound> inbounds = inboundRepository.findAll();

        log.info("data" + inbounds.size());

        return inbounds.stream()
                .map(inbound -> modelMapper.map(inbound, InboundDTO.class))
                .collect(Collectors.toList());
    }
}