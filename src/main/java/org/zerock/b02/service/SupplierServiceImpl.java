package org.zerock.b02.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Supplier;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.SupplierDTO;
import org.zerock.b02.repository.SupplierRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService{

    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;

    @Override
    public String register(SupplierDTO supplierDTO){

        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        String supplierId = supplierRepository.save(supplier).getSupplierId();
        return supplierId;
    }

    @Override
    public SupplierDTO readOne(String supplierId){

        Optional<Supplier> result = supplierRepository.findById(supplierId);
        Supplier supplier = result.orElseThrow();
        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);
        return supplierDTO;
    }

    @Override
    @Transactional
    public void modify(SupplierDTO supplierDTO){

        Optional<Supplier> result = supplierRepository.findById(supplierDTO.getSupplierId());
        Supplier supplier = result.orElseThrow();
        supplier.change(supplierDTO.getSupplierId(),
                supplierDTO.getSupplierName(),
                supplierDTO.getSupplierInfo());
        supplierRepository.save(supplier);
    }

    @Override
    public void remove(String supplierId){
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public PageResponseDTO list(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("supplierId");

        Page<Supplier> result = supplierRepository.searchAll(types, keyword, pageable);

        List<SupplierDTO> dtoList = result.getContent().stream()
                .map(supplier -> modelMapper
                        .map(supplier, SupplierDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<SupplierDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public List<SupplierDTO> getAllSuppliers(){
        List<Supplier> suppliers = supplierRepository.findAll();

        log.info("data" + suppliers.size());

        return suppliers.stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
                .collect(Collectors.toList());
    }
}

