package com.example.employee_management.repository;

import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {
    @Query("SELECT new com.globits.da.dto.DistrictDto(d) FROM District d")
    List<DistrictDto> getAll();
    @Query("Select count(d.id) from District d inner join Village v on d.id = v.district.id where d.id = :id and v.name = :name and v.id <> :villageId")
    int checkDistrictHasVillageName(String name, UUID id, UUID villageId);
    boolean existsByIdAndProvinceId(UUID id, UUID provinceId);
}
