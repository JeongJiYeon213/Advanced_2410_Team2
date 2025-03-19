package org.zerock.b02.service;

import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface CustomerService {

    String register(CustomerDTO customerDTO);

    CustomerDTO readOne(String customerId);

    void modify(CustomerDTO customerDTO);

    void remove(String customerId);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

    List<CustomerDTO> getAllCustomers();

}
