package com.example.employee_management.service.impl;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.constant.SqlParameter;
import com.globits.da.domain.Village;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.VillageDto;
import com.globits.da.dto.search.SearchVillageDto;
import com.globits.da.exception.NotFoundException;
import com.globits.da.repository.VillageRepository;
import com.globits.da.service.VillageService;
import com.globits.da.utils.PagingUtil;
import com.globits.da.validation.VillageValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service(value = BEAN_SERVICE.VILLAGE_SERVICE)
public class VillageServiceImpl implements VillageService {
    private final VillageRepository villageRepository;
    private final VillageValidation villageValidation;
    private final PagingUtil pagingUtil;
    private final EntityManager entityManager;

    @Autowired
    public VillageServiceImpl(VillageRepository repository,
                              VillageValidation villageValidation,
                              PagingUtil pagingUtil,
                              EntityManager entityManager) {
        this.villageRepository = repository;
        this.villageValidation = villageValidation;
        this.pagingUtil = pagingUtil;
        this.entityManager = entityManager;
    }

    @Override
    public List<VillageDto> findAllVillages() {
        return this.villageRepository.getAll();
    }

    @Override
    public SearchVillageDto findByCondition(SearchVillageDto searchVillageDto) {
        StringBuilder selectSql = new StringBuilder("Select new com.globits.da.dto.VillageDto(v) from Village v");
        StringBuilder countSql = new StringBuilder("Select count(v.id) from Village v");

        this.addAndClause(searchVillageDto, selectSql, countSql);
        Query selectQuery = this.entityManager.createQuery(selectSql.toString());
        Query countQuery = this.entityManager.createQuery(countSql.toString());
        this.pagingUtil.setPageSearchDto(searchVillageDto, selectQuery);
        this.setParameters(searchVillageDto, selectQuery, countQuery);

        searchVillageDto.setTotalPage( (int) Math.ceil( countQuery.getFirstResult() / searchVillageDto.getPageSize()));
        searchVillageDto.setResultList(selectQuery.getResultList());

        return searchVillageDto;
    }

    @Override
    public VillageDto findOneById(UUID id) {
        return new VillageDto(this.findVillageEntityById(id));
    }

    @Override
    public VillageDto saveVillage(VillageDto villageDto) {
        Village saveVillage = this.villageValidation.checkValid(villageDto, VALIDATION.CREATE);
        saveVillage = this.villageRepository.save(saveVillage);
        return new VillageDto(saveVillage);
    }

    @Override
    @Transactional
    public VillageDto updateVillage(VillageDto villageDto) {
        //Kiểm tra xem các dữ liệu có hợp lệ hay không
        Village findToUpdate = this.villageValidation.checkValid(villageDto, VALIDATION.UPDATE);
        findToUpdate = this.villageRepository.save(findToUpdate);
        //Trả về toàn bộ thông tin
        return new VillageDto(findToUpdate);
    }

    @Override
    public boolean checkExistById(UUID id) {
        return villageRepository.existsById(id);
    }

    @Override
    public boolean checkDistrictHasVillage(UUID districtId, UUID villageId) {
        //Kiểm tra xem có village nào có id và districtId này chưa
        return this.villageRepository.existsByIdAndDistrictId(villageId, districtId);
    }

    @Override
    public void deleteVillage(UUID id) {
        //Kiểm tra xem có tồn tại Village nào có id này không
        if(!this.villageRepository.existsById(id)){
            String idNotFound = String.format(VILLAGE_ERRORS.ID_NOT_FOUND, id);
            throw new NotFoundException(idNotFound);
        }
        this.villageRepository.deleteById(id);
    }

    private void addAndClause(SearchVillageDto searchDto, StringBuilder... sqls){
        StringBuilder andClause = new StringBuilder(" where (1 = 1)");
        if(!ObjectUtils.isEmpty(searchDto.getName())){
            andClause.append(" and name like :name");
        }

        if(!ObjectUtils.isEmpty(searchDto.getPopulation())){
            andClause.append(" and population = :population");
        }

        if(!ObjectUtils.isEmpty(searchDto.getDistrict()) && !ObjectUtils.isEmpty(searchDto.getDistrict().getId())){
            andClause.append(" and district_id = :district_id");
        }

        for(StringBuilder sql: sqls){
            sql.append(andClause);
        }
    }

    private void setParameters(SearchVillageDto villageDto, Query... queries){
        String name = villageDto.getName();
        Long population = villageDto.getPopulation();
        DistrictDto districtOwnVillage = villageDto.getDistrict();

        for(Query query: queries){
            if(!ObjectUtils.isEmpty(name)){
                query.setParameter(SqlParameter.NAME, "%" + name + "%");
            }
            if(!ObjectUtils.isEmpty(population)){
                query.setParameter(SqlParameter.POPULATION, population);
            }
            if(!ObjectUtils.isEmpty(districtOwnVillage) && !ObjectUtils.isEmpty(districtOwnVillage.getId())){
                query.setParameter(SqlParameter.DISTRICT_ID, districtOwnVillage.getId().toString());
            }
        }
    }

    private Village findVillageEntityById(UUID id){
        //Hàm này có tác dụng tìm ra 1 Village entity
        return this.villageRepository.findById(id).orElseThrow(() -> {
            String idNotFound = String.format(VILLAGE_ERRORS.ID_NOT_FOUND, id);
            return new NotFoundException(idNotFound);
        });
    }
}
