package com.interest.service.impl;

import com.interest.dao.SignInDao;
import com.interest.model.entity.SignInEntity;
import com.interest.model.response.AttendanceResponse;
import com.interest.model.utils.PageResult;
import com.interest.model.utils.PageWrapper;
import com.interest.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    private SignInDao signInDao;

    @Override
    public void insert(SignInEntity signInEntity) {
        signInDao.insert(signInEntity);
    }

    @Override
    public Integer getSignInCount(Integer userId) {
        return signInDao.getSignInCount(userId);
    }

    @Override
    public Integer isSignInToday(Integer userId) {
        return signInDao.isSignInToday(userId);
    }

    @Override
    public PageResult getAttendanceManagement(String searchContent, Date dateTimestamp, PageWrapper pageWrapper) {
        List<AttendanceResponse> list = signInDao.getAttendanceManagement(searchContent, dateTimestamp, pageWrapper);
        int size = signInDao.getAttendanceSizeOnManagement(searchContent, dateTimestamp);
        return new PageResult<>(list, size);
    }

}
