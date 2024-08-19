package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;

@Getter
@Setter
public class CustomUser extends User {
    private static final long serialVersionUID = -3531439484732724601L;
    private Integer id;
    private String name;
    private String email;
    
    //this is data from the Joomla user table
    public CustomUser(String username, 
        String password, boolean enabled, 
        boolean accountNonExpired, boolean credentialsNonExpired, 
        boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password,
            enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked,
            authorities);
    }
}
