package by.itransition.webconstructor.dto;

import lombok.Data;

@Data
public class PageDto {

    private String name;

    private int layout;

    private ElementDto[] elements;

}
