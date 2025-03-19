package org.zerock.b02.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;

    private int total;
    private int start;
    private int end;

    private boolean prev;
    private boolean next;

    private List<E> dtoList;


    @Builder
    public PageResponseDTO
            (PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {


        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = (dtoList == null) ? Collections.emptyList() : dtoList;
        
        // 페이지 계산
        if(total > 0) {
            this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
            this.start = this.end - 9;
            int last = (int) (Math.ceil((total / (double) size)));

            this.end = end > last ? last : end;
            this.prev = this.start > 1;
            this.next = total > this.end * this.size;
        } else{
            this.start = 1;
            this.end = 1;
            this.prev = false;
            this.next = false;
        }
    }
}