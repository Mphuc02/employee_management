package com.example.employee_management.constant;

public class ErrorMessage {
    public static final class EMPLOYEE_ERRORS{
        public static final String ID_EXISTED = "employee có id = %s đã tồn tại";
        public static final String ID_NOT_FOUND = "không tìm thấy employee có id = %s";
        public static final String ID_NOT_NULL = "id của employee không được bỏ trống";
        public static final String CODE_NOT_NULL = "code không được bỏ trống";
        public static final String CODE_EXISTED = "employee có code = %s đã tồn tại";
        public static final String CODE_LENGTH = "code phải có độ dài từ 6 đến 10";
        public static final String NAME_NOT_NULL = "name không được bỏ trống";
        public static final String NAME_LENGTH = "name phải có độ dài từ 1 đến 50";
        public static final String EMAIL_NOT_NULL = "email không được bỏ trống";
        public static final String EMAIL_EXISTED = "email: %s đã tồn tại";
        public static final String EMAIL_LENGTH = "email phải có độ dài từ 1 đến 50";
        public static final String EMAIL_NOT_FORMAT = "email không đùng định dạng";
        public static final String PHONE_NOT_NULL = "phone không được bỏ trống";
        public static final String PHONE_LENGTH = "phone phải có độ dài từ 1 đến 11";
        public static final String AGE_NOT_NULL = "age không được bỏ trống";
        public static final String AGE_MIN = "age không được dưới 0";
        public static final String AGE_MAX = "age không được vượt quá 150";
        public static final String AGE_NOT_FORMAT_EXCEL = "age tại cột thứ 5 không đúng định dạng ";
        public static final String EMPLOYEE_HAS_TOO_MUCH_CERTIFICATE = "số lượng certificate có id = %s còn hiệu lực vượt quá %d";
        public static final String PROVINCE_ID_EXCEL_NOT_FORMAT = "province iđ tại cột 6 không đúng định dạng";
        public static final String DISTRICT_ID_EXCEL_NOT_FORMAT = "district id tại cột 7 không đúng định dạng";
        public static final String VILLAGE_ID_EXCEL_NOT_FORMAT = "village id taị cột 8 không đúng định dạng";
        public static final String ERROR_AT_EXCEL_ROW = "lỗi tại dòng %d:";
    }

    public static final class PROVINCE_ERRORS{
        public static final String PROVINCE_NOT_NULL = "province không được để trống";
        public static final String ID_NOT_NULL = "id của province không được để trống";
        public static final String ID_NOT_FOUND = "không tìm thấy province có id = %s";
        public static final String NAME_NOT_NULL = "name không được bỏ trống";
        public static final String NAME_LENGTH = "name phải trong khoảng từ 0 đến 50 ký tự";
        public static final String PROVINCE_ID_NOT_EXIST = "province có id = %s không tồn tại";
        public static final String PROVINCE_ID_EXISTED = "province có id = %s đã tồn tại";
        public static final String PROVINCE_NAME_EXISTED = "đã tồn tại province có name = %s";
        public static final String DELETE_SUCCESS = "xóa thành công province có id = %s";
        public static final String PROVINCE_NOT_OWN_DISTRICT = "không tồn tại district có id = %s thuộc province có id = %s";
    }

    public static final class DISTRICT_ERRORS{
        public static final String ID_NOT_NULL = "id của district không được để trống";
        public static final String ID_EXISTED = "district có id = %s đã tồn tại";
        public static final String ID_NOT_FOUND = "district có id = %s chưa tồn tại";
        public static final String PROVINCE_NOT_NULL = "province không được để trống";
        public static final String PROVINCE_NOT_EXIST = "province có id = %s không tồn tại";
        public static final String PROVINCE_ID_NOT_NULL = "province id không được null";
        public static final String NAME_NOT_NULL = "name của district không được bỏ trống";
        public static final String NAME_EXISTED = "đã tồn tại district có name = %s trong province này";
        public static final String NAME_LENGTH = "name phải có độ dài từ 0 đén 50 ký tự";
        public static final String DELETE_SUCCESS = "xóa thành công district có id = %s";
        public static final String DISTRICT_NOT_OWN_VILLAGE = "không tồn tại village có id = %s thuộc district có id = %s";
    }

    public static final class VILLAGE_ERRORS{
        public static final String VILLAGE_NOT_NULL = "village không được bỏ trống";
        public static final String ID_NOT_NULL = "id của village không được bỏ trống";
        public static final String ID_EXISTED = "id = %s đã tồn tại";
        public static final String ID_NOT_FOUND = "village có id = %s chưa tồn tại";
        public static final String DISTRICT_NOT_NULL = "district không được bỏ trống";
        public static final String DISTRICT_ID_NOT_NULL = "id của disctrict không được bỏ trống";
        public static final String DISTRICT_ID_NOT_EXISTED = "district có id = %s không tồn tại";
        public static final String NAME_NOT_NULL = "name của village không được bỏ trống";
        public static final String NAME_LENGTH = "name của village phải có từ 1 đến 50 ký tự";
        public static final String NAME_EXISTED = "đã tồn tại village có name = %s trong district này";
        public static final String POPULATION_NOT_NULL = "population của village không được bỏ trống";
        public static final String POPULATION_MIN = "population của village không được có giá trị âm";
        public static final String DELETE_SUCCESS = "xóa thành công village có id = %s";
    }
    public static final class CERTIFICATE_ERRORS{
        public static final String ID_EMPLOYEE_CERTIFICATE_NOT_FOUND = "id của employee_certificate = %s không tồn tại";
        public static final String ID_EMPLOYEE_CERTIFICATE_EXISTED = "id của employee_certificate = %s đã tồn tại";
        public static final String ID_EMPLOYEE_CERTIFICATE_NOT_NULL = "id của employee_certificate không được null";
        public static final String EMPLOYEE_ID_NOT_NULL = "id của Employee không được null";
        public static final String CERTIFICATE_ID_NOT_NULL = "id của certificate không được null";
        public static final String CERTIFICATE_ID_NOT_FOUND = "certificate có id = %s không tồn tại";
        public static final String CERTIFICATE_ID_EXISTED = "certificate có id = %s đã tồn tại";
        public static final String CERTIFICATE_NAME_NOT_NULL = "name của certificate không được bỏ trống";
        public static final String CERTIFICATE_NAME_EXISTED = "đã tồn tại certificate có name = %s";
        public static final String PROVINCE_ID_NOT_NULL = "id của province không được bỏ trống";
        public static final String VALID_DATE_NOT_NULL = "ngày bắt đầu có hiệu lực không được bỏ trống";
        public static final String EXPIRE_DATE_NOT_NULL = "ngày hết hạn không được bỏ trống";
        public static final String CERTIFICATE_OF_PROVINCE_STILL_VALID = "không thể thêm certificate có id = %s do đã tồn tại 1 certifiacte chưa hết hạn";
    }
    public static final class FILE_ERRORS{
        public static final String FILE_NOT_IN_EXCEL_FORMAT = "file tải lên không đúng định dạng xlsx";
        public static final String NOT_ENOUGH_CELL_IN_ROW = "dòng này không đủ %d dòng";
    }
}
