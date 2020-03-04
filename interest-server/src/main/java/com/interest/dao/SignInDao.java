package com.interest.dao;

import com.interest.model.entity.SignInEntity;
import com.interest.model.response.AttendanceResponse;
import com.interest.model.utils.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SignInDao {
    public void insert(SignInEntity signInEntity);

    Integer getSignInCount(@Param("userId") Integer userId);

    Integer isSignInToday(@Param("userId") Integer userId);

    List<AttendanceResponse> getAttendanceManagement(@Param("searchContent") String searchContent, @Param("dateTimestamp") Date dateTimestamp, @Param("pageWrapper") PageWrapper pageWrapper);

    int getAttendanceSizeOnManagement(@Param("searchContent") String searchContent, @Param("dateTimestamp") Date dateTimestamp);
}
