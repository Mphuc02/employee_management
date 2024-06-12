package com.example.employee_management.service;

import com.globits.da.dto.VillageDto;
import com.globits.da.dto.search.SearchVillageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface VillageService {
    List<VillageDto> findAllVillages();
    SearchVillageDto findByCondition(SearchVillageDto searchVillageDto);
    VillageDto findOneById(UUID id);
    VillageDto saveVillage(VillageDto villageDto);
    VillageDto updateVillage(VillageDto villageDto);
    boolean checkExistById(UUID id);
    boolean checkDistrictHasVillage(UUID districtId, UUID villageId);
    void deleteVillage(UUID id);
}
