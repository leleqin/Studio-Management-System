package com.interest.model.entity;

import lombok.Data;

/**
 * @author leleqin
 * @date 2020/2/18
 */
@Data
public class WorkspaceEntity {
    private Integer id;

    private String name;

    private String desc;

    private String address;

    private String uid;

    private Integer userId;

    private String createtime;

}
