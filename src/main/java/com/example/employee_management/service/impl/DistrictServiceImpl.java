package com.example.employee_management.service.impl;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.constant.SqlParameter;
import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.search.SearchDistrictDto;
import com.globits.da.exception.NotFoundException;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.PagingUtil;
import com.globits.da.validation.DistrictValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Service(value = BEAN_SERVICE.DISTRICT_SERVICE)
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final EntityManager entityManager;
    private final DistrictValidation districtValidation;
    private final PagingUtil pagingUtil;

    @Autowired
    public DistrictServiceImpl(DistrictRepository repository,
                               EntityManager manager,
                               DistrictValidation districtValidation,
                               PagingUtil pagingUtil) {
        this.districtRepository = repository;
        this.entityManager = manager;
        this.districtValidation = districtValidation;
        this.pagingUtil = pagingUtil;
    }

    @Override
    public List<DistrictDto> findAll() {
        return districtRepository.getAll();
    }

    @Override
    public SearchDistrictDto findAllByCondition(SearchDistrictDto searchDistrictDto) {
        StringBuilder selectSql = new StringBuilder("SELECT new com.globits.da.dto.DistrictDto(d) FROM District d");
        StringBuilder countSql = new StringBuilder("SELECT count(d.id) FROM District d");

        //Thêm mệnh đề and cho câu truy vấn
        this.addAndClause(searchDistrictDto, selectSql, countSql);
        Query selectQuery = entityManager.createQuery(selectSql.toString());
        Query countQuery = entityManager.createQuery(countSql.toString());

        this.setParameters(searchDistrictDto, selectQuery, countQuery);
        this.pagingUtil.setPageSearchDto(searchDistrictDto, selectQuery);

        int totalItem = countQuery.getFirstResult();
        searchDistrictDto.setTotalPage((int) Math.ceil(totalItem / searchDistrictDto.getPageSize()));
        searchDistrictDto.setResultList(selectQuery.getResultList());

        return searchDistrictDto;
    }

    @Override
    public DistrictDto findById(UUID id) {
        return new DistrictDto(this.findDistrictEntityById(id));
    }

    @Override
    public DistrictDto saveDistrict(DistrictDto districtDto) {
        District saveDistrict = districtValidation.checkValid(districtDto, VALIDATION.CREATE);
        saveDistrict = this.districtRepository.save(saveDistrict);
        return new DistrictDto(saveDistrict);
    }

    @Override
    @Transactional
    public DistrictDto updateDistrict(DistrictDto districtDto) {
        //Kiểm tra các dữ liệu của District cần update
        District findToUpdate = districtValidation.checkValid(districtDto, VALIDATION.UPDATE);
        findToUpdate = this.districtRepository.save(findToUpdate);
        //Tìm kiếm toàn bộ dữ liệu của đối tượng vừa được update
        return new DistrictDto(findToUpdate);
    }

    @Override
    public void deleteDistrict(UUID id) {
        //Kiểm tra xem có District nào có id này không
        if(!this.districtRepository.existsById(id)){
            String idNotFound = String.format(DISTRICT_ERRORS.ID_NOT_FOUND, id);
            throw new NotFoundException(idNotFound);
        }
        this.districtRepository.deleteById(id);
    }

    private void addAndClause(SearchDistrictDto districtDto, StringBuilder... sqls){
        StringBuilder andClause = new StringBuilder(" where (1 = 1)");

        if(!ObjectUtils.isEmpty(districtDto.getName())){
            andClause.append(" and name like :name");
        }

        if(!ObjectUtils.isEmpty(districtDto.getProvince()) && !ObjectUtils.isEmpty(districtDto.getProvince().getId())){
            andClause.append(" and province_id = :province_id");
        }

        for(StringBuilder statement: sqls){
            statement.append(andClause);
        }
    }

    private void setParameters(SearchDistrictDto districtDto, Query... queries){
        for(Query query: queries){
            if(!ObjectUtils.isEmpty(districtDto.getName())){
                query.setParameter(SqlParameter.NAME, "%" + districtDto.getName() + "%");
            }

            if(!ObjectUtils.isEmpty(districtDto.getProvince()) && !ObjectUtils.isEmpty(districtDto.getProvince().getId())){
                query.setParameter("province_id", districtDto.getProvince().getId());
            }
        }
    }

    @Override
    public boolean checkExistById(UUID id) {
        return districtRepository.existsById(id);
    }

    @Override
    public boolean checkDistrictHasVillageName(String name, UUID districtId, UUID villageId){
        return districtRepository.checkDistrictHasVillageName(name, districtId, villageId) > 0;
    }

    @Override
    public boolean checkProvinceHasDistrict(UUID provinceId, UUID districtId) {
        return this.districtRepository.existsByIdAndProvinceId(districtId, provinceId);
    }

    private District findDistrictEntityById(UUID id){
        //Hàm này có tác dụng tìm 1 thực thể Village
        return this.districtRepository.findById(id).orElseThrow(() -> {
            String idNotFound = String.format(DISTRICT_ERRORS.ID_NOT_FOUND, id);
            return new NotFoundException(idNotFound);
        });
    }
}
