package com.hpi.tpc.data.entities;

import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Getter
@Service
public class UserModelService implements UserService
{//implements FilterableCrudService<User> {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public UserModel getByUserName(String userName)
    {
        String sql;

        //get the user data from the Joomla database
        sql
            = "select id, name, username, email, password from hlhtxc5_hpiJoomla.hpiJ_users where username=?;";
        UserModel userModel = jdbcTemplate.queryForObject(sql,
            new Object[]
            {
                userName
            },
            (rs, rowNum) -> new UserModel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")));

        return userModel;
    }
}
