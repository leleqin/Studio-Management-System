package com.interest.service.impl;

import com.interest.dao.WorkspaceDao;
import com.interest.model.entity.AssetsEntity;
import com.interest.model.entity.WorkspaceEntity;
import com.interest.service.WorkspaceService;
import com.interest.utils.DateUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leleqin
 * @date 2020/2/18
 */
@MapperScan("com.interest.dao")
@Service("workspaceServiceImpl")
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceDao workspaceDao;

    @Override
    public List<WorkspaceEntity> workspacesList(int pageSize, int start) {
        return workspaceDao.workspacesList(pageSize, start);
    }

    @Override
    public Integer workspaceSize(int pageSize, int start) {
        return workspaceDao.workspaceSize(pageSize, start);
    }

    @Override
    public List<AssetsEntity> assetsList(int pageSize, int start) {
        return workspaceDao.assetsList(pageSize,start);
    }

    @Override
    public Integer assetsSize(int pageSize, int start) {
        return workspaceDao.assetsSize(pageSize,start);
    }

    @Override
    public void insertWorkspace(WorkspaceEntity workspaceEntity) {
        workspaceEntity.setCreatetime(DateUtil.currentTimestamp());
        workspaceDao.insertWorkspace(workspaceEntity);
    }

    @Override
    public void deleteWorkspaces(List<String> groupId) {
        workspaceDao.deleteWorkspaces(groupId);
    }

    @Override
    public void updateWorkspace(WorkspaceEntity workspaceEntity) {
        workspaceDao.updateWorkspace(workspaceEntity);
    }

    @Override
    public void insertAssets(AssetsEntity assetsEntity) {
        assetsEntity.setCreateTime(DateUtil.currentTimestamp());
        workspaceDao.insertAssets(assetsEntity);
    }

    @Override
    public void deleteAssets(List<String> groupId) {
        workspaceDao.deleteAssets(groupId);
    }

    @Override
    public void updateAssets(AssetsEntity assetsEntity) {
        workspaceDao.updateAssets(assetsEntity);
    }


}
