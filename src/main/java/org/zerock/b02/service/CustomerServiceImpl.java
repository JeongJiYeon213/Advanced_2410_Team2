package org.zerock.b02.service;

import org.zerock.b02.domain.Customer;
import org.zerock.b02.dto.CustomerDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    @Override
    public String register(CustomerDTO customerDTO) {

        Customer customer = customerDTO.toEntity();
        customerRepository.save(customer);
        return customer.getCustomerId();
    }

    @Override
    public CustomerDTO readOne(String customerId) {

        Optional<Customer> result = customerRepository.findById(customerId);
        Customer customer = result.orElseThrow();
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    @Override
    @Transactional
    public void modify(CustomerDTO customerDTO) {

        Optional<Customer> result = customerRepository.findById(customerDTO.getCustomerId());
        Customer customer = result.orElseThrow();
        customer.change(customerDTO.getCustomerId(),
                        customerDTO.getCustomerName(),
                        customerDTO.getCustomerInfo());
        customerRepository.save(customer);
    }

    @Override
    public void remove(String customerId) {
        log.info("remove: " + customerId);
        customerRepository.deleteById(customerId);
    }

    @Override
    public PageResponseDTO<CustomerDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("customerId");

        Page<Customer> result = customerRepository.searchAll(types, keyword, pageable);

        List<CustomerDTO> dtoList = result.getContent().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<CustomerDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        log.info("data" + customers.size());

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }
}