package com.example.employee_management.repository;

import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    @Query("Select new com.globits.da.dto.ProvinceDto(p) from Province p")
    List<ProvinceDto> getListDto();
    @Query("Select count(p.id) from Province p inner join District d on p.id = d.province.id where p.id = :provinceId and d.name = :name and d.id <> :districtId")
    int checkProvinceHasDistrictName(String name, UUID provinceId, UUID districtId);
    @Query("Select count(p.id) from Province p where p.name = :name and p.id <> :provinceId ")
    int checkNameExisted(String name, UUID provinceId);
}