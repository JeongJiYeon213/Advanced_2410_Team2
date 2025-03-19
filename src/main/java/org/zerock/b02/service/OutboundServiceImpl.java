package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Customer;
import org.zerock.b02.domain.Outbound;
import org.zerock.b02.domain.Product;

import org.zerock.b02.dto.OutboundDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.CustomerRepository;
import org.zerock.b02.repository.OutboundRepository;
import org.zerock.b02.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService{
    private final ModelMapper modelMapper;
    private final OutboundRepository outboundRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Long register(OutboundDTO outboundDTO){
        Optional<Outbound> lastOutbound = outboundRepository.findTopByOrderByOutboundCodeDesc();

        String newOutboundCode = generateNewOutboundCode(lastOutbound);

        outboundDTO.setOutboundCode(newOutboundCode);

        String[] productCodes = outboundDTO.getProductCode().split(",");
        String[] customerIds = outboundDTO.getCustomerId().split(",");
        String[] outboundStatuses = outboundDTO.getOutboundStatus().split(",");
        String[] descriptions = outboundDTO.getDescription().split(",");
        Long quantities = outboundDTO.getQuantity();
        LocalDateTime outboundDates = outboundDTO.getOutboundDate();

        log.info("outboundStatuses: " + Arrays.toString(outboundStatuses));
        log.info("descriptions: " + Arrays.toString(descriptions));
        log.info("quantities: " + quantities);
        log.info("outboundDates: " + outboundDates);


        for (int i = 0; i < productCodes.length; i++) {
            String productCode = productCodes[i].trim();
            String customerId = customerIds[i].trim();
            String outboundStatus = outboundStatuses[i].trim();
            String description = descriptions[i].trim();

            Product product = productRepository.findByProductCode(productCode)
                    .orElseThrow(() -> new RuntimeException("Product not found with productCode: " + productCode));
            Customer customer = customerRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with customerId: " + customerId));

            lastOutbound = outboundRepository.findTopByOrderByOutboundCodeDesc();

            newOutboundCode = generateNewOutboundCode(lastOutbound);

            outboundDTO.setOutboundCode(newOutboundCode);

            Outbound outbound = modelMapper.map(outboundDTO, Outbound.class);
            outbound.setProduct(product);
            outbound.setCustomer(customer);
            outbound.setOutboundStatus(outboundStatus);
            outbound.setDescription(description);
            outbound.setQuantity(quantities);
            outbound.setOutboundDate(outboundDates);

            outboundRepository.save(outbound);
        }
        return 1L;
    }

    private String generateNewOutboundCode(Optional<Outbound> lastOutbound) {
        String newOutboundCode = "O001";

        if (lastOutbound.isPresent()) {
            String lastOutboundCode = lastOutbound.get().getOutboundCode();
            int lastCodeNumber = Integer.parseInt(lastOutboundCode.substring(1)); // "i001" -> 1
            newOutboundCode = "O" + String.format("%03d", lastCodeNumber + 1); // "i002", "i003", ...
        }
        return newOutboundCode;
    }

    @Override
    public OutboundDTO readOne(Long outboundId){
        Optional<Outbound> result = outboundRepository.findById(outboundId);
        Outbound outbound = result.orElseThrow(() -> new RuntimeException("Outbound not found"));
        OutboundDTO outboundDTO = modelMapper.map(outbound, OutboundDTO.class);

        if (outbound.getProduct() != null) {
            outboundDTO.setProductCode(outbound.getProduct().getProductCode());
        }
        if (outbound.getCustomer() != null) {
            outboundDTO.setCustomerId(outbound.getCustomer().getCustomerId());
        }

        return outboundDTO;
    }

    @Override
    public void modify(OutboundDTO outboundDTO){
        Optional<Outbound> result = outboundRepository.findById(outboundDTO.getOutboundId());

        Outbound outbound = result.orElseThrow(() -> new RuntimeException("Outbound not found"));

        Product newProduct = productRepository.findByProductCode(outboundDTO.getProductCode())
                .orElseThrow(() -> new RuntimeException("Product not found with productCode"));
        Customer newCustomer = customerRepository.findByCustomerId(outboundDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with customerId"));

        outbound.change(newProduct,
                outboundDTO.getOutboundCode(),
                newCustomer,
                outboundDTO.getQuantity(),
                outboundDTO.getOutboundDate(),
                outboundDTO.getDescription(),
                outboundDTO.getOutboundStatus());

        outboundRepository.save(outbound);
    }

    @Override
    public void remove(Long outboundId){
        outboundRepository.deleteById(outboundId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("outboundId");

        LocalDateTime from = pageRequestDTO.getFrom();
        LocalDateTime to = pageRequestDTO.getTo();

        Page<Outbound> result = outboundRepository.searchAll(types,keyword, pageable, from, to);

        List<OutboundDTO> dtoList = result.getContent().stream().map(outbound -> {
            OutboundDTO outboundDTO = modelMapper.map(outbound, OutboundDTO.class);

            if (outbound.getProduct() != null) {
                outboundDTO.setProductCode(outbound.getProduct().getProductCode());
            }
            if (outbound.getCustomer() != null) {
                outboundDTO.setCustomerId(outbound.getCustomer().getCustomerId());
            }

            return outboundDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<OutboundDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<OutboundDTO> getAllOutbound() {
        List<Outbound> outbounds = outboundRepository.findAll();

        log.info("data" + outbounds.size());

        return outbounds.stream()
                .map(outbound -> modelMapper.map(outbound, OutboundDTO.class))
                .collect(Collectors.toList());
    }
}