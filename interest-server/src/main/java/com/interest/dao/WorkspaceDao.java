package com.interest.dao;

import com.interest.model.entity.AssetsEntity;
import com.interest.model.entity.WorkspaceEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkspaceDao {

    List<WorkspaceEntity> workspacesList(@Param("pageSize")int pageSize, @Param("start")int start);

    Integer workspaceSize(@Param("pageSize")int pageSize, @Param("start")int start);

    List<AssetsEntity> assetsList(@Param("pageSize") int pageSize, @Param("start") int start);

    Integer assetsSize(@Param("pageSize") int pageSize, @Param("start") int start);

    /**
     * 新建工作室
     * @param workspaceEntity
     */
    void insertWorkspace(@Param("workspaceEntity")WorkspaceEntity workspaceEntity);

    /**
     * 删除工作室
     * @param groupId
     */
    void deleteWorkspaces(@Param("groupId") List<String> groupId);

    /**
     * 修改工作室信息
     * @param workspaceEntity
     */
    void updateWorkspace(@Param("workspaceEntity") WorkspaceEntity workspaceEntity);

    /**
     * 新建工作室资产
     * @param assetsEntity
     */
    void insertAssets(@Param("assetsEntity") AssetsEntity assetsEntity);

    void deleteAssets(@Param("groupId") List<String> groupId);

    void updateAssets(@Param("assetsEntity") AssetsEntity assetsEntity);
}
