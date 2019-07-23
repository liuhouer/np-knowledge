package cn.northpark.model;

import lombok.Data;

@Data
public class KnowledgeMessageBoard {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    private String postDate;

    private String message;

}