package com.example.employee_management.repository;

import com.globits.da.domain.Village;
import com.globits.da.dto.VillageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VillageRepository extends JpaRepository<Village, UUID> {
    @Query("Select new com.globits.da.dto.VillageDto(v) from Village v")
    List<VillageDto> getAll();
    boolean existsByIdAndDistrictId(UUID id, UUID districtId);
}
