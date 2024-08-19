package com.hpi.tpc.app.security;

import com.hpi.tpc.data.entities.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service("userDetailsService")
@Component
public class CustomUserDetailsService 
    implements UserDetailsService {
    
    @Autowired private final UserModelService userModelService;
    
    public CustomUserDetailsService(UserModelService userModelService)
    {
        this.userModelService = userModelService;
    }

    @Override
    public CustomUser loadUserByUsername(final String username) 
        throws UsernameNotFoundException {

        UserModel userModel;
        // UserDetails securityUser;
        CustomUser customUser;
        
        // get user info from Joomla
        userModel = userModelService.getByUserName(username);
        
        // create spring security user
        // securityUser = new org.springframework.security.
        //     core.userdetails.User(
        //         userModel.getUserName(), userModel.getPassword(),
        //         Collections.singletonList(new SimpleGrantedAuthority("User")));
        
        // create spring security customUser
        customUser = new CustomUser(
                userModel.getUserName(), userModel.getPassword(),
                true, true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("User")));
        
        customUser.setEmail(userModel.getEmail());
        customUser.setId(userModel.getId());
        customUser.setName(userModel.getName());
        
        return customUser;
    }
}