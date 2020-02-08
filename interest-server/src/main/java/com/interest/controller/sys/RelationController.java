package com.interest.controller.sys;

import java.util.List;

import javax.annotation.Resource;

import com.interest.annotation.InterestLog;
import com.interest.model.entity.RelationEntity;
import com.interest.model.utils.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.interest.service.RelationService;

@RestController
public class RelationController {

    private Logger log = LoggerFactory.getLogger(RelationController.class);

    @Resource(name = "relationServiceImpl")
    private RelationService relationService;

    /**
     * 通过userId得到关系List
     *
     * @param userId
     * @return
     */
    @InterestLog
    @GetMapping("/relations/{userId}")
    public ResponseWrapper<List<RelationEntity>> getRelationByUserId(@PathVariable int userId) {
        log.debug("The method is ending");
        return new ResponseWrapper<>(relationService.getRelationByUserId(userId));
    }

    /**
     * 批量插入关系数据
     *
     * @param relationList
     * @return
     */
    @InterestLog
    @PostMapping("/relations")
    public ResponseWrapper<List<RelationEntity>> insertRelations(@RequestBody() List<RelationEntity> relationList) {
        relationService.insertRelations(relationList);
        log.debug("The method is ending");
        return new ResponseWrapper<>(relationList);
    }
}
