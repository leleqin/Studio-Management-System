package com.interest.service;

import com.interest.model.entity.AssetsEntity;
import com.interest.model.entity.WorkspaceEntity;

import java.util.List;

public interface WorkspaceService {
    /**
     * 工作List
     * @param id
     * @return
     */
//    List<WorkspaceEntity> workspaceList(int id);

    /**
     * 获取workspace列表
     * @param pageSize
     * @param start
     * @return
     */
    List<WorkspaceEntity> workspacesList(int pageSize, int start);

    /**
     * 获取workspace列表的总量
     * @param pageSize
     * @param start
     * @return
     */
    Integer workspaceSize(int pageSize, int start);

    /**
     * 工作室财产List
     * @param pageSize
     * @param start
     * @return
     */
    List<AssetsEntity> assetsList(int pageSize, int start);

    Integer assetsSize(int pageSize, int start);

    /**
     * 新建workspace
     * @param workspaceEntity
     */
    void insertWorkspace(WorkspaceEntity workspaceEntity);

    /**
     * 删除工作室
     * @param groupId
     */
    void deleteWorkspaces(List<String> groupId);

    /**
     * 修改工作室信息
    * @param workspaceEntity
     */
    void updateWorkspace(WorkspaceEntity workspaceEntity);

    /**
     * 新建工作室资产
     * @param assetsEntity
     */
    void insertAssets(AssetsEntity assetsEntity);

    void deleteAssets(List<String> groupId);

    void updateAssets(AssetsEntity assetsEntity);
}
