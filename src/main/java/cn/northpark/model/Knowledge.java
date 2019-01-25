package cn.northpark.model;

import lombok.Data;

@Data
public class Knowledge {
    private Integer id;

    private String retCode;

    private String title;

    private String briefImg;

    private String brief;

    private String postDate;

    private String tags;

    private String tagsCode;

    private String returl;

    private String linkUrl;

    private Long viewTimes;

    private String content;

    private String path;

}