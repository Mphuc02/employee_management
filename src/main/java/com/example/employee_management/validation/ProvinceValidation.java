package com.example.employee_management.validation;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.exception.DuplicateException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.exception.NotNullException;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.ReFormatNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProvinceValidation {
    private final DistrictValidation districtValidation;
    private final ProvinceService provinceService;
    private final ReFormatNameUtils reFormatNameUtils;
    private final ProvinceRepository provinceRepository;
    private Integer conditionCheck;

    @Autowired
    @Lazy//Nhằm tránh lỗi đệ quy vô tận, do bên ProvinceService chứa 1 đối tượng ProvinceValidation
    public ProvinceValidation (DistrictValidation districtValidation,
                               @Qualifier(BEAN_SERVICE.PROVINCE_SERVICE) ProvinceService provinceService,
                               ReFormatNameUtils reFormatNameUtils,
                               ProvinceRepository provinceRepository){
        this.districtValidation = districtValidation;
        this.provinceService = provinceService;
        this.reFormatNameUtils = reFormatNameUtils;
        this.provinceRepository = provinceRepository;
    }

    public Province checkValid(ProvinceDto validProvinceDto, int conditionCheck) {
        this.conditionCheck = conditionCheck;

        Province entitySetValue = new Province();
        if(this.conditionCheck == VALIDATION.UPDATE)
            entitySetValue = this.provinceRepository.findById(validProvinceDto.getId()).orElseThrow( () ->{
                    String idNotFound = String.format(PROVINCE_ERRORS.ID_NOT_FOUND, validProvinceDto.getId());
                    return new NotFoundException(idNotFound);
            } );

        //Kiểm tra name
        this.checkName(validProvinceDto);
        //Kiểm tra List các District
        this.checkListDistrict(validProvinceDto.getDistricts(), entitySetValue);

        if(!ObjectUtils.isEmpty(validProvinceDto.getName())){
            entitySetValue.setName(validProvinceDto.getName());
        }

        //Xóa các thuộc tính sau khi dùng xong
        this.conditionCheck = null;

        return entitySetValue;
    }

    private void checkId(UUID id){
        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(PROVINCE_ERRORS.ID_NOT_NULL);

        if(!provinceService.checkExistById(id)){
            //Nếu chưa tồn tại thì không thể update
            if(this.conditionCheck == VALIDATION.UPDATE) {
                String idNotExisted = String.format(PROVINCE_ERRORS.PROVINCE_ID_NOT_EXIST, id);
                throw new NotFoundException(idNotExisted);
            }
        }
    }

    public void checkId(ProvinceDto checkId){
        if(ObjectUtils.isEmpty(checkId)){
            throw new NotNullException(PROVINCE_ERRORS.ID_NOT_NULL);
        }
        this.conditionCheck = VALIDATION.UPDATE;
        //gọi đến hàm checkId(UUID id)
        this.checkId(checkId.getId());
        this.conditionCheck = null;
    }

    public void checkName(ProvinceDto validProvinceDto) {
        String name = validProvinceDto.getName();
        if(ObjectUtils.isEmpty(name)){
            if(conditionCheck == VALIDATION.CREATE){
                throw new NotNullException(PROVINCE_ERRORS.NAME_NOT_NULL);
            }
            else {
                return;
            }
        }

        UUID provinceId = validProvinceDto.getId();
        String reFormatName = this.reFormatNameUtils.reFormatName(name);
        //Todo: Thêm phần kiểm tra xem tên có ký tự đặc biệt nào không
        if (provinceService.checkExsitByName(reFormatName, provinceId)){
            String nameExisted = String.format(PROVINCE_ERRORS.PROVINCE_NAME_EXISTED, name);
            throw new DuplicateException(nameExisted);
        }
        //Đặt lại thuộc tính name thành đã được chuẩn hóa
        validProvinceDto.setName(reFormatName);
    }

    public void checkListDistrict(List<DistrictDto> districtsOfProvince, Province entitySetValue){
        if(!ObjectUtils.isEmpty(districtsOfProvince)){
            List<District> districtEntities = new ArrayList<>();
            for(DistrictDto district: districtsOfProvince){
                district.getProvince().setId(entitySetValue.getId());
                if(this.conditionCheck == VALIDATION.CREATE ){
                    //Kiểm tra district này với điều kiện được tạo kèm khi thêm 1 province mới
                    districtEntities.add( this.districtValidation.checkValid(district, VALIDATION.CREATE_WITH_OWNER));
                }
                else {
                    //Kiểm tra xem dữ liệu của District có hợp lệ hay không
                    districtEntities.add(this.districtValidation.checkValid(district, VALIDATION.UPDATE));
                }
            }
            entitySetValue.setDistricts(districtEntities);
        }
    }
}
