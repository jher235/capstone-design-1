package org.example.capstonedesign1.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginationResponse<T> {
    private final List<T> content;
    private final Pagination pagination;

    public static <T> PaginationResponse from(Page<T> pageData){
        return new PaginationResponse(pageData.getContent(), Pagination.from(pageData));
    }

    @Getter
    @AllArgsConstructor
    public static class Pagination {
        private final int currentPage;
        private final int totalPage;

        public static  <T> Pagination from(Page<T> page){
            return new Pagination(page.getNumber(), page.getTotalPages());
        }
    }
}
