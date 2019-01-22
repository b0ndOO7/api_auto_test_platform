package com.main.manage.modules.service;

import com.main.manage.modules.dao.MenuDao;
import com.main.manage.modules.dao.UserDao;
import com.main.manage.modules.entity.MenuEntity;
import com.main.manage.modules.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private MenuDao menuDao;

    public UserEntity login(String username, String password) {
        UserEntity existUser = userDao.findByUsername(username);
        if ( existUser == null || !existUser.getPassword().equals(password)) {
            existUser = null;
        }
        return existUser;
    }

    public boolean checkPassword(String username, String password) {
        System.out.println("check password" + username + password);
        UserEntity user = userDao.checkPassword(username, password);

        return user == null?false:true;
    }

    public List<MenuEntity> getUserMenu(String uid) {

        return menuDao.getUserMenuById(uid);
    }

}
