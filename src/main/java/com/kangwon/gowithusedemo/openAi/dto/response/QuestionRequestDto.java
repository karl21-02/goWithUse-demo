package com.kangwon.gowithusedemo.openAi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class QuestionRequestDto implements Serializable {
    private String location_from;
    private String location_to;
    private String experience_time;
    private String companion;
    private String interests;
}
