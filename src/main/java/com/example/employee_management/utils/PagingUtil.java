package com.example.employee_management.utils;

import com.globits.da.constant.Constant.*;
import com.globits.da.dto.search.SearchDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.persistence.Query;

@Component
public class PagingUtil {
    public void setPageSearchDto(SearchDto searchDto, Query... queries){
        Integer pageIndex = searchDto.getPageIndex();
        Integer pageSize = searchDto.getPageSize();

        if(ObjectUtils.isEmpty(pageIndex)){
            pageIndex = 1;
            searchDto.setPageIndex(1);
        }

        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = PAGEABLE.DEFAULT_PAGE_SIZE;
            searchDto.setPageSize(pageSize);
        }

        int offset = (pageIndex - 1) * pageSize;
        for(Query query: queries){
            query.setFirstResult(offset).setMaxResults(pageSize);
        }
    }
}
