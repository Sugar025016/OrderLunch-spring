package com.order_lunch.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.order_lunch.entity.User;
import com.order_lunch.repository.IUserRepository;

@Component
@Transactional
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	IUserRepository iUserRepository;
	private User user;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		// 如果驗證成功，返回一個包含用戶角色的Authentication對象
		Optional<User> findByAccount = iUserRepository.findByAccount(username);
		if (findByAccount.isPresent()) {
			user = findByAccount.get();
			if (!password.equals(user.getPassword()))
				throw new AuthenticationServiceException(String.format("please check account or password"));
		} else {
			throw new BadCredentialsException("please check account or password");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getRole().equals("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
        UserDetails userDetails = new CustomUserDetails(user.getId(), authentication.getName(), authentication.getCredentials().toString(), authorities);
		return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// 返回true表示支持對該類型的Authentication進行驗證
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
