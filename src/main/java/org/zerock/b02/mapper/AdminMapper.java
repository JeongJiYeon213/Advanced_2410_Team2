package org.zerock.b02.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zerock.b02.domain.Admin;


@Mapper
public interface AdminMapper {
    Long insert(Admin admin);
}
