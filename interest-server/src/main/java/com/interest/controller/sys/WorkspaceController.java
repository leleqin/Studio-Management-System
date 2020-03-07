package com.interest.controller.sys;

import com.interest.annotation.InterestLog;
import com.interest.model.entity.AssetsEntity;
import com.interest.model.entity.WorkspaceEntity;
import com.interest.model.utils.PageResult;
import com.interest.model.utils.ResponseWrapper;
import com.interest.service.WorkspaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leleqin
 * @date 2020/2/18
 */
@RestController
public class WorkspaceController {

    private Logger log = LoggerFactory.getLogger(WorkspaceController.class);

    @Resource(name = "workspaceServiceImpl")
    private WorkspaceService workspaceService;

    /**
     * 获取用户管理权限
     * @return
     */
//    @InterestLog
//    @GetMapping("/manage/workspace")
//    public ResponseWrapper<List<WorkspaceEntity>> menuList() {
//        List<WorkspaceEntity> workspaceList = workspaceService.workspaceList(SecurityAuthenUtil.getId());
//        return new ResponseWrapper<>(workspaceList);
//    }

    /**
     * 工作室列表
     * @param pageSize
     * @param page
     * @param workspaceId
     * @return
     */
    @InterestLog
    @GetMapping("/workspaces")
    public ResponseWrapper<PageResult> workspacesList(int pageSize, int page, String workspaceId) {
        PageResult<WorkspaceEntity> pageResult = new PageResult<>();
        pageResult.setData(workspaceService.workspacesList(pageSize, page * pageSize));
        pageResult.setTotalCount(workspaceService.workspaceSize(pageSize, page * pageSize));
        log.debug("The method is ending");
        return new ResponseWrapper<>(pageResult);
    }


    /**
     * 新建工作室
     * @param workspaceEntity
     * @return
     */
    @InterestLog
    @PostMapping("/workspaces/workspace")
    public ResponseWrapper<WorkspaceEntity> insertWorkspace(@RequestBody WorkspaceEntity workspaceEntity) {
        workspaceService.insertWorkspace(workspaceEntity);
        log.debug("The method is ending");
        return new ResponseWrapper<>(workspaceEntity);
    }


    /**
     * 删除工作室
     * @param groupId
     * @return
     */
    @InterestLog
    @DeleteMapping("/workspaces")
    public ResponseWrapper<List<String>> deleteWorkspaces(@RequestBody List<String> groupId) {
        workspaceService.deleteWorkspaces(groupId);
        return new ResponseWrapper<>(groupId);
    }

    /**
     * 更新工作室信息
     * @param workspaceEntity
     * @param id
     * @return
     */
    @InterestLog
    @PutMapping("/workspaces/{id}")
    public ResponseWrapper<WorkspaceEntity> updateWorkspace(@RequestBody WorkspaceEntity workspaceEntity, @PathVariable int id) {
        if (workspaceEntity.getId() == id) {
            workspaceService.updateWorkspace(workspaceEntity);
        }
        log.debug("The method is ending");
        return new ResponseWrapper<>(workspaceEntity);
    }

    /**
     * 工作室财产List
     * @param pageSize
     * @param page
     * @return
     */
    @InterestLog
    @GetMapping("/assets")
    public ResponseWrapper<PageResult> assetsList(int pageSize,int page, String assetsId) {
        PageResult<AssetsEntity> pageResult = new PageResult<>();
        pageResult.setData(workspaceService.assetsList(pageSize, page * pageSize));
        pageResult.setTotalCount(workspaceService.assetsSize(pageSize, page * pageSize));
        log.debug("The method is ending");
        return new ResponseWrapper<>(pageResult);
    }

    /**
     * 新建工作室资产
     * @param assetsEntity
     * @return
     */
    @InterestLog
    @PostMapping("/assets/insertAssets")
    public ResponseWrapper<AssetsEntity> insertAssets(@RequestBody AssetsEntity assetsEntity) {
        workspaceService.insertAssets(assetsEntity);
        log.debug("The method is ending");
        return new ResponseWrapper<>(assetsEntity);
    }

    /**
     * 删除工作室资产
     * @param groupId
     * @return
     */
    @InterestLog
    @DeleteMapping("/assets")
    public ResponseWrapper<List<String>> deleteAssets(@RequestBody List<String> groupId) {
        workspaceService.deleteAssets(groupId);
        return new ResponseWrapper<>(groupId);
    }


    /**
     * 更新工作室资产
     * @param assetsEntity
     * @param id
     * @return
     */
    @InterestLog
    @PutMapping("/assets/{id}")
    public ResponseWrapper<AssetsEntity> updateAssets(@RequestBody AssetsEntity assetsEntity, @PathVariable int id) {
        if (assetsEntity.getId() == id) {
            workspaceService.updateAssets(assetsEntity);
        }
        log.debug("The method is ending");
        return new ResponseWrapper<>(assetsEntity);
    }
}
