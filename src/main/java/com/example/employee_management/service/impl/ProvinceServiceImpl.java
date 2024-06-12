package com.example.employee_management.service.impl;
import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.constant.SqlParameter;
import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.search.SearchProvinceDto;
import com.globits.da.exception.NotFoundException;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.PagingUtil;
import com.globits.da.validation.ProvinceValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Service(value = BEAN_SERVICE.PROVINCE_SERVICE)
public class ProvinceServiceImpl implements ProvinceService {
    private final EntityManager entityManager;
    private final ProvinceRepository provinceRepository;
    private final ProvinceValidation provinceValidation;
    private final PagingUtil pagingUtil;

    @Autowired
    public ProvinceServiceImpl(EntityManager entityManager,
                               ProvinceRepository provinceRepository,
                               ProvinceValidation provinceValidation,
                               PagingUtil pagingUtil) {
        this.entityManager = entityManager;
        this.provinceRepository = provinceRepository;
        this.provinceValidation = provinceValidation;
        this.pagingUtil = pagingUtil;
    }

    @Override
    public List<ProvinceDto> findAll() {
        return this.provinceRepository.getListDto();
    }

    @Override
    public ProvinceDto findById(UUID id) {
        return new ProvinceDto(this.findProvinceEntityById(id));
    }

    @Override
    public SearchProvinceDto findByCondition(SearchProvinceDto searchDto) {
        StringBuilder selectSql = new StringBuilder("SELECT new com.globits.da.dto.ProvinceDto(p) FROM Province p");
        StringBuilder countSql = new StringBuilder("Select count(p.id) from Province p");

        this.addAndClause(searchDto, selectSql, countSql);
        Query selectQuery = this.entityManager.createQuery(selectSql.toString());
        Query countQuery = this.entityManager.createQuery(countSql.toString());

        this.pagingUtil.setPageSearchDto(searchDto, selectQuery);
        this.setParameter(searchDto, selectQuery, countQuery);
        searchDto.setTotalPage( (int) Math.ceil( countQuery.getFirstResult() / searchDto.getPageSize()));
        searchDto.setResultList(selectQuery.getResultList());

        return searchDto;
    }

    @Override
    public ProvinceDto saveProvince(ProvinceDto provinceDto) {
        //Kiểm tra xem dữ liệu của Province và list các District của nó có hợp lệ hay không
        Province saveProvince = provinceValidation.checkValid(provinceDto, VALIDATION.CREATE);
        saveProvince = this.provinceRepository.save(saveProvince);
        return new ProvinceDto(saveProvince);
    }

    @Override
    @Transactional
    public ProvinceDto updateProvince(ProvinceDto provinceDto) {
        Province findToUpdate = provinceValidation.checkValid(provinceDto, VALIDATION.UPDATE);
        //Nếu hợp lệ thì sẽ tiến hành update
        findToUpdate = this.provinceRepository.save(findToUpdate);
        //trả về toàn bộ thông tin của tỉnh đã được update
        return new ProvinceDto(findToUpdate);
    }

    @Override
    public boolean checkExistById(UUID id) {
        return provinceRepository.existsById(id);
    }

    @Override
    public boolean checkExsitByName(String name, UUID provinceId) {
        //nếu có lớn hơn 1 kết quả thì đã tồn tại
        return this.provinceRepository.checkNameExisted(name, provinceId) > 0;
    }

    @Override
    public boolean checkProvinceHasDistrictName(String name, UUID provinceId, UUID districtId) {
        return this.provinceRepository.checkProvinceHasDistrictName(name, provinceId, districtId) > 0;
    }

    @Override
    public void delete(UUID id){
        if(provinceRepository.existsById(id)){
            provinceRepository.deleteById(id);
        }
        else{
            throw new NotFoundException(String.format(PROVINCE_ERRORS.PROVINCE_ID_NOT_EXIST, id));
        }
    }

    private void addAndClause(SearchProvinceDto searchDto, StringBuilder... sqls){
        StringBuilder sb = new StringBuilder(" WHERE (1 = 1)");

        String name = searchDto.getName();

        if(!ObjectUtils.isEmpty(name)){
            sb.append(" AND lower(p.name) like lower(:name)");
        }

        for(StringBuilder sql: sqls){
            sql.append(sb);
        }
    }

    private void setParameter(SearchProvinceDto provinceDto, Query... queries){
        String name = provinceDto.getName();

        for(Query query: queries){
            if(!ObjectUtils.isEmpty(name)){
                query.setParameter(SqlParameter.NAME, "%" + name + "%");
            }
        }
    }

    private Province findProvinceEntityById(UUID id){
        //Hàm này có tác dụng tìm kiếm 1 entity Province, chứ k phải là ProvinceDto
        return this.provinceRepository.findById(id).orElseThrow(() -> {
            String idNotFound = String.format(PROVINCE_ERRORS.ID_NOT_FOUND, id);
            return new NotFoundException(idNotFound);
        });
    }
}
