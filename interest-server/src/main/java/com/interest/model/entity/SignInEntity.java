package com.interest.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author leleqin
 * @date 2020/2/24
 */
@Data
public class SignInEntity {
    private Integer id;

    private Integer state;

    private String comment;

    private Integer userId;

    private Integer workspaceId;

    private Date createTime;

}
