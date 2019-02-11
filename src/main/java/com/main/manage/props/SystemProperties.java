package com.main.manage.props;

import com.main.manage.modules.entity.UserEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import java.util.List;

@ConfigurationProperties(prefix = "sys")
public class SystemProperties {

    @Valid
    private List<UserEntity> users;

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
