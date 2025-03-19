package org.zerock.b02.service;

import org.zerock.b02.domain.Admin;
import org.zerock.b02.dto.AdminDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

public interface AdminService {

    Long register(AdminDTO adminDTO);

    AdminDTO readOne(Long bno);

    void modify(AdminDTO adminDTO);

    void remove(Long bno);

    PageResponseDTO list(PageRequestDTO pageRequestDTO);

    List<AdminDTO> getAllAdmins();

    Admin login(Long adminId, int adminPassword);
}
