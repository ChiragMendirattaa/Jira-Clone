package com.example.jiraclone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowStatusDTO {
    private Long id;
    private String name;
    private Integer order;
}