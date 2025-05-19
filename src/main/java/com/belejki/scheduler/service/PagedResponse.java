package com.belejki.scheduler.service;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {

    private List<T> content;
    private int totalPages;
    private int totalElements;
    private int number; // current page number
    private int size;   // page size
    private boolean last;
    private boolean first;

    // Getters and setters
}

