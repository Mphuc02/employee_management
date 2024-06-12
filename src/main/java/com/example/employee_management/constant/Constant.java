package com.example.employee_management.constant;

import java.text.SimpleDateFormat;

public class Constant {
    public static final SimpleDateFormat DATE_SDF = new SimpleDateFormat("yyyy-MM-dd");
    public static class BEAN_SERVICE{
        public static final String PROVINCE_SERVICE = "provinceService";
        public static final String DISTRICT_SERVICE = "districtService";
        public static final String VILLAGE_SERVICE = "villageService";
        public static final String EMPLOYEE_SERVICE = "employeeService";
        public static final String CERTIFICATE_SERVICE = "certificateService";
    }

    public static class REQUEST{
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static class PAGEABLE{
        public static final int DEFAULT_PAGE_SIZE = 5;
        public static final String DESC_SORT = "desc";
        public static final String ASC_SORT = "asc";
    }

    public static final class EMPLOYEE{
        public static final int MAX_CERTIFICATE_VALID_OF_EMPLOYEE = 3;
        public static final int NUMBER_FIELD_CELL_IN_A_EXCEL_ROW = 8;
    }

    public static class PROVINCE{
    }

    public static class DISTRICT{
    }

    public static class VILLAGE{
    }

    public static class VALIDATION{
        public static final int CREATE = 1;
        public static final int UPDATE = 2;
        public static final int CREATE_WITH_OWNER = 3;
    }
}
