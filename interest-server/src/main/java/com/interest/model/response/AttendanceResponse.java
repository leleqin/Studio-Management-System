package com.interest.model.response;

import lombok.Data;

import java.util.Date;

/**
 * @author leleqin
 * @date 2020/3/3
 */
@Data
public class AttendanceResponse {

    private int id;

    private String name;

    private Date createTime;

    private String workspaceName;
}
