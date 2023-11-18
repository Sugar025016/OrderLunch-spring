package com.order_lunch.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.order_lunch.entity.User;
import com.order_lunch.repository.IUserRepository;


@Configuration
@EnableWebSecurity
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByAccount(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 在這裡可以根據用戶的角色來添加權限
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (user.getRole().equals("admin")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            // 返回 UserDetails 對象
            return new org.springframework.security.core.userdetails.User(
                user.getAccount(),
                user.getPassword(),
                authorities
            );
        } else {
            throw new UsernameNotFoundException("用戶名不存在：" + username);
        }
    }
}
