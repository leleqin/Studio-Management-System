package com.interest.controller.sys;

import java.util.List;

import javax.annotation.Resource;

import com.interest.annotation.InterestLog;
import com.interest.model.entity.MenuEntity;
import com.interest.model.utils.PageResult;
import com.interest.model.utils.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interest.service.MenuService;
import com.interest.utils.SecurityAuthenUtil;

@RestController
public class MenuController {

    private Logger log = LoggerFactory.getLogger(MenuController.class);

    @Resource(name = "menuServiceImpl")
    private MenuService menuService;

    /**
     * 获取该用户的菜单权限
     *
     * @return
     */
    @InterestLog
    @GetMapping("/manage/menu")
    public ResponseWrapper<List<MenuEntity>> menuList() {
        List<MenuEntity> menuList = menuService.menuList(SecurityAuthenUtil.getId());
        return new ResponseWrapper<>(menuList);
    }

    /**
     * 获取menus表数据
     *
     * @param pageSize
     * @param page
     * @return
     */
    @InterestLog
    @GetMapping("/menus")
    public ResponseWrapper<PageResult> menusList(int pageSize, int page, String menuId) {
        PageResult<MenuEntity> pageResult = new PageResult<>();
        pageResult.setData(menuService.menusList(pageSize, page * pageSize, menuId));
        pageResult.setTotalCount(menuService.menusSize(pageSize, page * pageSize, menuId));
        log.debug("The method is ending");
        return new ResponseWrapper<>(pageResult);
    }

    /**
     * 通过parentId得到menus列表
     *
     * @param parentId
     * @return
     */
    @InterestLog
    @GetMapping("/menus/parentId")
    public ResponseWrapper<List<MenuEntity>> menusByParentId(int parentId) {
        return new ResponseWrapper<>(menuService.menusByParentId(parentId));
    }

    /**
     * 新建菜单信息
     *
     * @param menuEntity
     * @return
     */
    @InterestLog
    @PostMapping("/menus/menu")
    public ResponseWrapper<MenuEntity> insertMenu(@RequestBody MenuEntity menuEntity) {
        menuService.insertMenu(menuEntity);
        log.debug("The method is ending");
        return new ResponseWrapper<>(menuEntity);
    }

    /**
     * 修改菜单信息
     *
     * @param menuEntity
     * @param id
     * @return
     */
    @InterestLog
    @PutMapping("/menus/{id}")
    public ResponseWrapper<MenuEntity> updateMenu(@RequestBody MenuEntity menuEntity, @PathVariable int id) {
        if (menuEntity.getId() == id) {
            menuService.updateMenu(menuEntity);
        }
        log.debug("The method is ending");
        return new ResponseWrapper<>(menuEntity);
    }

    /**
     * 删除菜单信息
     *
     * @param groupId
     * @return
     */
    @InterestLog
    @DeleteMapping("/menus")
    public ResponseWrapper<List<String>> deleteMenus(@RequestBody List<String> groupId) {
        menuService.deleteMenus(groupId);
        return new ResponseWrapper<>(groupId);
    }

    /**
     * 获取二级菜单
     *
     * @return
     */
    @InterestLog
    @GetMapping("/menus/submenus")
    public ResponseWrapper<List<MenuEntity>> getSubmenus() {
        return new ResponseWrapper<>(menuService.getSubmenus());
    }
}
