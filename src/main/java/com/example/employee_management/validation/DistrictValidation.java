package com.example.employee_management.validation;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.domain.Village;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.VillageDto;
import com.globits.da.exception.DuplicateException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.exception.NotNullException;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.DistrictService;
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
public class DistrictValidation {
    private Integer conditionCheck;
    private final DistrictRepository districtRepository;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final VillageValidation villageValidation;
    private final ReFormatNameUtils reFormatNameUtils;

    @Autowired
    @Lazy
    public DistrictValidation(@Qualifier(BEAN_SERVICE.DISTRICT_SERVICE) DistrictService districtService,
                              @Qualifier(BEAN_SERVICE.PROVINCE_SERVICE) ProvinceService provinceService,
                              DistrictRepository districtRepository,
                              ReFormatNameUtils reFormatNameUtils,
                              VillageValidation villageValidation) {
        this.districtService = districtService;
        this.provinceService = provinceService;
        this.reFormatNameUtils = reFormatNameUtils;
        this.villageValidation = villageValidation;
        this.districtRepository = districtRepository;
    }


    public District checkValid(DistrictDto validDistrict, int conditionCheck){
        this.conditionCheck = conditionCheck;

        District entitySetValue = new District();
        if(this.conditionCheck == VALIDATION.UPDATE){
            entitySetValue = this.districtRepository.findById(validDistrict.getId()).orElseThrow( () ->{
               String idNotFount = String.format(DISTRICT_ERRORS.ID_NOT_FOUND, validDistrict.getId());
               return new NotFoundException(idNotFount);
            });
        }

        //Kiểm tra hợp lệ của các thuộc tính
        this.checkProvince(validDistrict.getProvince());
        this.checkName(validDistrict);
        this.checkListVillage(validDistrict.getVillages(), entitySetValue);

        if(!ObjectUtils.isEmpty(validDistrict.getProvince())){
            Province province = new Province();
            province.setId(validDistrict.getProvince().getId());
            entitySetValue.setProvince(province);
        }
        if(!ObjectUtils.isEmpty(validDistrict.getName())){
            entitySetValue.setName(validDistrict.getName());
        }

        //Xóa các thuộc tính sau khi dùng xong
        this.conditionCheck = null;

        return entitySetValue;
    }

    private void checkProvince(ProvinceDto provinceOwnDistrict){
        if(ObjectUtils.isEmpty(provinceOwnDistrict)){
            throw new NotNullException(DISTRICT_ERRORS.PROVINCE_NOT_NULL);
        }
        else{
            UUID provinceId = provinceOwnDistrict.getId();

            //Nếu như đây là 1 District được thêm mới kèm với Province thì không cần kiểm tra Province này đã tòn tại
            if(this.conditionCheck != VALIDATION.CREATE_WITH_OWNER){
                //Kiểm tra xem có District nào có id này tồn tại không
                if(!provinceService.checkExistById(provinceId)){
                    String idNotExisted = String.format(DISTRICT_ERRORS.PROVINCE_NOT_EXIST, provinceId);
                    throw new NotFoundException(idNotExisted);
                }
            }
        }
    }

    private void checkId(UUID id){
        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(DISTRICT_ERRORS.ID_NOT_NULL);

        //Kiểm tra xem District có id này đã tồn tại hay chưa
        if(districtService.checkExistById(id)){
            if(this.conditionCheck == VALIDATION.CREATE){
                String idExisted = String.format(DISTRICT_ERRORS.ID_EXISTED, id);
                throw new DuplicateException(idExisted);
            }
        }
    }

    public void checkId(DistrictDto checkId){
        if(ObjectUtils.isEmpty(checkId)){
            throw new NotNullException(DISTRICT_ERRORS.ID_NOT_NULL);
        }
        this.conditionCheck = VALIDATION.UPDATE;
        this.checkId(checkId.getId());
        this.conditionCheck = null;
    }

    public void checkName(DistrictDto validDistrict){
        String name = validDistrict.getName();

        if(ObjectUtils.isEmpty(name)){
            //Khi mà thêm mới hoặc là di chuyển huyện này vào 1 tỉnh khác thì cần phải có name
            if(this.conditionCheck == VALIDATION.CREATE || !ObjectUtils.isEmpty(validDistrict.getProvince()))
                throw new NotNullException(DISTRICT_ERRORS.NAME_NOT_NULL);
            else
                return;
        }

        UUID districtId = validDistrict.getId();
        UUID provinceId = validDistrict.getProvince().getId();
        String reFormatName = this.reFormatNameUtils.reFormatName(name);
        //Kiểm tra xem trông Tỉnh này đã có huyện nào chứa tên này hay chưa
        if(provinceService.checkProvinceHasDistrictName(reFormatName, provinceId, districtId)){
            String nameExisted = String.format(DISTRICT_ERRORS.NAME_EXISTED, name);
            throw new DuplicateException(nameExisted);
        }

        //Thay thế thuộc tính name thành đã chuẩn hóa
        validDistrict.setName(reFormatName);
    }

    public void checkListVillage(List<VillageDto> villagesOfDistrict, District entitySetValue){
        if(!ObjectUtils.isEmpty(villagesOfDistrict)){
            List<Village> villageEntities = new ArrayList<>();
            for(VillageDto village: villagesOfDistrict){
                //Thêm thuộc tính District để kiểm tra xem có Village nào có name như này trong District hay chưa
                village.getDistrict().setId(entitySetValue.getId());

                //Khi thêm mới thì bắt buộc phải tự tạo các Id của từng Village
                if(this.conditionCheck == VALIDATION.CREATE || this.conditionCheck == VALIDATION.CREATE_WITH_OWNER ){
                   villageEntities.add( this.villageValidation.checkValid(village, VALIDATION.CREATE_WITH_OWNER) );
                }
                else {
                    //Kiểm tra tinh hợp lệ của danh sách các Village của District này
                    villageEntities.add( this.villageValidation.checkValid(village, this.conditionCheck) );
                }
            }
            entitySetValue.setVillages(villageEntities);
        }
    }
}
