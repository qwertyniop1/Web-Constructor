package by.itransition.webconstructor.dto;

import lombok.Data;

@Data
public class ElementDto {

    private String type;

    private int location;

    private int width;

    private int height;

    private String url;

    private String text;

    private boolean autoplay;

    private boolean loop;

    private boolean chart;
}

