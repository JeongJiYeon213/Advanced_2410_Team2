package org.zerock.b02.service;


import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {

    String register(SupplierDTO supplierDTO);

    SupplierDTO readOne(String supplierId);

    void modify(SupplierDTO supplierDTO);

    void remove(String supplierId);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

    List<SupplierDTO> getAllSuppliers();
}
