package com.main.manage.modules.controller;

import com.main.manage.RestTemplate.Result;
import com.main.manage.modules.entity.MenuEntity;
import com.main.manage.modules.entity.MenuTree;
import com.main.manage.modules.entity.UserEntity;
import com.main.manage.modules.model.UserModel;
import com.main.manage.modules.service.RedisService;
import com.main.manage.modules.service.UserService;
import com.main.manage.utils.RedisKeys;
import com.main.manage.utils.ResultCode;
import com.main.manage.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/login")
    public Result doLogin(@RequestBody UserModel userModel){
        log.info("doLogin:" + userModel);
        //  参数校验
        if ( userModel == null || StringUtils.isEmpty(userModel.getUsername()) || StringUtils.isEmpty(userModel.getPassword())) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }

        UserEntity user = userService.login(userModel.getUsername(), userModel.getPassword());
        if (user == null){
            return ResultUtils.warn(ResultCode.PASSWORD_ERROR);
        }else {
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            user.setPassword("");
            user.setToken(token);
            redisService.removePattern(user.getUid() + RedisKeys.LOGIN_TOKEN + "*");
            redisService.set(user.getUid() + RedisKeys.LOGIN_TOKEN + token, user.getUid(), RedisKeys.TOKEN_TTL);

            return ResultUtils.success(user);
        }
    }

    @RequestMapping("logout")
    public Result doLogout(@RequestBody Map<String,String> reqMap){
        log.info("doLogout" + reqMap);
        boolean isLogout = redisService.remove(reqMap.get("uid") + RedisKeys.LOGIN_TOKEN + reqMap.get("token"));
        if (isLogout) {
            log.info("用户：" + reqMap.get("uid") + ",退出成功，已清除redis");
        }else {
            log.error("用户：" + reqMap.get("uid") + ",退出失败");
        }

        return isLogout ? ResultUtils.success("退出成功"): ResultUtils.warn(ResultCode.FAIL,"退出失败");
    }

    @RequestMapping("getmenu")
    public Result getUserMenu(@RequestBody UserModel userModel) {
        log.info("getUserMenu" + userModel);
        if ( userModel == null || StringUtils.isEmpty(userModel.getUid()) ) {
            return ResultUtils.warn(ResultCode.PARAMETER_ERROR);
        }
        String uid = userModel.getUid();
        List<MenuEntity> list = userService.getUserMenu(uid);

        return ResultUtils.success(new MenuTree().getMenuList(list));
    }

}
