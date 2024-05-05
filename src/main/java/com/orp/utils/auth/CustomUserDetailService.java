package com.orp.utils.auth;

import com.orp.model.Staff;
import com.orp.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staffByEmail = staffRepository.findByEmail(username);
        if (staffByEmail == null) {
            throw new UsernameNotFoundException("Email is not found, please check again!");
        }
        return new CustomUserDetail(staffByEmail);
    }
}
