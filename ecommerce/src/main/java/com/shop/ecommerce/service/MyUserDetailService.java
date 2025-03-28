package com.shop.ecommerce.service;


import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.entity.UserPriciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    com.shop.ecommerce.repo.UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUsername(username);
        if(user==null)
        {
            System.out.println("no user found");
            throw new UsernameNotFoundException("user name not found");
        }
        return new UserPriciple(user);
    }
}
