package com.interest.service;

import com.interest.model.entity.SignInEntity;
import com.interest.model.utils.PageResult;
import com.interest.model.utils.PageWrapper;

import java.util.Date;

public interface SignInService {
    void insert(SignInEntity signInEntity);

    Integer getSignInCount(Integer userId);

    Integer isSignInToday(Integer userId);

    PageResult getAttendanceManagement(String searchContent, Date dateTimestamp, PageWrapper pageWrapper);
}
