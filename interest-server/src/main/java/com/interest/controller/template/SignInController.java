package com.interest.controller.template;

import com.interest.model.entity.SignInEntity;
import com.interest.model.utils.PageResult;
import com.interest.model.utils.PageWrapper;
import com.interest.model.utils.ResponseWrapper;
import com.interest.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sign")
public class SignInController {

    @Autowired
    private SignInService signInService;


    @GetMapping("/attendance")
    public ResponseWrapper<PageResult> getArticles(@RequestParam(value = "searchContent", required = false) String searchContent,
                                                   @RequestParam(value = "dateTimestamp", required = false) Date dateTimestamp,
                                                   @RequestParam("pageSize") int pageSize,
                                                   @RequestParam("page") int page) {
        PageWrapper pageWrapper = new PageWrapper(pageSize, page);
        PageResult pageResult = signInService.getAttendanceManagement(searchContent, dateTimestamp, pageWrapper);
        return new ResponseWrapper<>(pageResult);
    }

    /**
     * 学生签到接口（往表新增数据）
     * @param signState
     * @param signInEntity
     * @return
     */
    @PostMapping("/addSignIn")
    public String addSignIn(@RequestParam(value = "signState")Integer signState, @RequestParam(value = "userId")Integer userId, SignInEntity signInEntity){
        try {
            signInEntity.setUserId(userId);
            signInEntity.setState(signState);
            signInEntity.setCreateTime(new Date());
            //学生签到的同时给表新增一条数据
            signInService.insert(signInEntity);
            return "签到成功";
        }catch (Exception e){
            e.printStackTrace();
            return "签到失败";
        }
    }

    /**
     * 统计学生签到了多少次
     * @param userId
     * @return
     */
    @RequestMapping("/getSignInCount")
    public Map<String,Object> getSignInCount(@RequestParam(value = "userId")Integer userId){
        Map<String,Object> map = new HashMap<>();
        try {
            Integer count = signInService.getSignInCount(userId);
            System.out.println(count);
            map.put("count",count);
            map.put("state",200);
            map.put("message","success");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("state",500);
            map.put("message","failed");
            return map;
        }
    }

    /**
     * 判断当日是否签到
     * @param userId
     * @return
     */
    @RequestMapping("/isSignInToday")
    public Map<String,Object> isSignInToday(@RequestParam(value = "userId")Integer userId){
        Integer count = signInService.isSignInToday(userId);
//        System.out.println(count);
        Map<String,Object> map = new HashMap<>();
        //如果等于1说明今日已签到
        if (count == 1){
            map.put("isSign",true);
        }else if (count == 0){
            //等于0是今日未签到
            map.put("isSign",false);
        }
        return map;
    }


}
