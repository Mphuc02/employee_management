package com.example.employee_management.validation;

import com.globits.da.constant.Constant.*;
import com.globits.da.constant.ErrorMessage.*;
import com.globits.da.domain.District;
import com.globits.da.domain.Village;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.VillageDto;
import com.globits.da.exception.DuplicateException;
import com.globits.da.exception.NotFoundException;
import com.globits.da.exception.NotNullException;
import com.globits.da.repository.VillageRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.service.VillageService;
import com.globits.da.utils.ReFormatNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class VillageValidation {
    private final VillageService villageService;
    private final VillageRepository villageRepository;
    private final DistrictService districtService;
    private final ReFormatNameUtils reFormatNameUtils;
    private Integer conditionCheck;

    @Autowired
    @Lazy
    public VillageValidation(@Qualifier(BEAN_SERVICE.DISTRICT_SERVICE) DistrictService districtService,
                             @Qualifier(BEAN_SERVICE.VILLAGE_SERVICE) VillageService villageService,
                             VillageRepository villageRepository,
                             ReFormatNameUtils reFormatNameUtils) {
        this.districtService = districtService;
        this.villageService = villageService;
        this.reFormatNameUtils = reFormatNameUtils;
        this.villageRepository = villageRepository;
    }

    public Village checkValid(VillageDto validVillage, int conditionCheck){
        this.conditionCheck = conditionCheck;

        Village entitySetValue = new Village();
        if(this.conditionCheck == VALIDATION.UPDATE){
            entitySetValue = this.villageRepository.findById(validVillage.getId()).orElseThrow( () -> {
               String idNotFound = String.format(VILLAGE_ERRORS.ID_NOT_FOUND, validVillage.getId());
               return new NotFoundException(idNotFound);
            });
        }

        //Thực hiện kiểm tra các dữ liệu
        this.checkDistrict(validVillage.getDistrict());
        this.checkName(validVillage);
        this.checkPopulation(validVillage.getPopulation());

        if(!ObjectUtils.isEmpty(validVillage.getDistrict())){
            District district = new District();
            district.setId(validVillage.getDistrict().getId());
            entitySetValue.setDistrict(district);
        }
        if(!ObjectUtils.isEmpty(validVillage.getName())){
            entitySetValue.setName(validVillage.getName());
        }
        if(!ObjectUtils.isEmpty(validVillage.getPopulation())){
            entitySetValue.setPopulation(validVillage.getPopulation());
        }

        //Xóa các thuộc tính sau khi dùng xong
        this.conditionCheck = null;

        return entitySetValue;
    }

    public void checkDistrict(DistrictDto districtOwnVillage){
        if(ObjectUtils.isEmpty(districtOwnVillage)){
            throw new NotNullException(VILLAGE_ERRORS.DISTRICT_NOT_NULL);
        }
        else {
            UUID id = districtOwnVillage.getId();
            //Nếu như được thêm mới cùng với District thì không cần phải kiểm tra District này đã tồn tại hay chưa
            if(this.conditionCheck != VALIDATION.CREATE_WITH_OWNER){
                if(!districtService.checkExistById(id)){//Nếu không tồn tại district nào có id này
                    String isDistrictNotExisted = String.format(VILLAGE_ERRORS.DISTRICT_ID_NOT_EXISTED, id);
                    throw new NotFoundException(isDistrictNotExisted);
                }
            }
        }
    }

    private void checkId(UUID id){
        if(ObjectUtils.isEmpty(id))
            throw new NotNullException(VILLAGE_ERRORS.ID_NOT_NULL);

        //Kiểm tra xem id này đã tồn tại hay chưa
        if(!villageService.checkExistById(id)){
            //Nếu update mà id này chưa tồn tại thì sẽ có ngoại lệ
            if(this.conditionCheck == VALIDATION.UPDATE){
                String idNotExisted = String.format(VILLAGE_ERRORS.ID_NOT_FOUND, id);
                throw new NotFoundException(idNotExisted);
            }
        }
    }

    public void checkId(VillageDto checkId){
        if(ObjectUtils.isEmpty(checkId)){
            throw new NotNullException(VILLAGE_ERRORS.ID_NOT_NULL);
        }
        this.conditionCheck = VALIDATION.UPDATE;
        this.checkId(checkId.getId());
        this.conditionCheck = null;
    }

    public void checkName(VillageDto validVillage){
        String name = validVillage.getName();
        if(ObjectUtils.isEmpty(name)){
            //Khi thêm mới hoặc di chuyển village vào 1 huyện mới thì phải có name
            if(this.conditionCheck == VALIDATION.CREATE || !ObjectUtils.isEmpty( validVillage.getDistrict() ))
                throw new NotNullException(VILLAGE_ERRORS.NAME_NOT_NULL);
            else
                return;
        }

        UUID villageId = validVillage.getId();
        UUID districtId = validVillage.getDistrict().getId();
        String reFormatName = this.reFormatNameUtils.reFormatName(name);
        //Kiểm tra xem đã có Village nào có tên này trong huyện này chưa
        if(districtService.checkDistrictHasVillageName(reFormatName, districtId, villageId)){
            String nameExisted = String.format(VILLAGE_ERRORS.NAME_EXISTED,reFormatName);
            throw new DuplicateException(nameExisted);
        }
        //Thay thế thuộc tính name của đối tượng thành đã được chuẩn hóa
        validVillage.setName(reFormatName);
    }

    public void checkPopulation(Long population){
    }
}
