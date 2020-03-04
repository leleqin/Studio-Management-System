package com.interest.model.entity;

import lombok.Data;

/**
 * @author leleqin
 * @date 2020/2/18
 */
@Data
public class AssetsEntity {
    private Integer id;

    private String name;

    private String type;

    private String uid;

    private String workspaceId;

    private String userId;

    private String number;

    private String abrasion;

    private String createTime;
}
