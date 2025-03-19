package org.zerock.b02.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b02.domain.Admin;
import org.zerock.b02.repository.search.AdminSearch;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, AdminSearch {
    @Query("select b from Admin b where b.adminName in :adminNames")
    List<Admin> findByAdminNames(@Param("adminNames") List<String> adminNames);

    @Query("select b from Admin b where b.adminName like concat('%', :keyword, '%')")
    Page<Admin> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Admin> findByAdminId(Long adminId);
}


