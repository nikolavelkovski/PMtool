package io.codedivine.pmtool.services;

import io.codedivine.pmtool.domain.User;
import io.codedivine.pmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Transient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username);
        if(user==null) new UsernameNotFoundException("User not found");
        return user;//vraka userdetails type

    } //dokolku klient probuva da se logira so user koj go kreira da proveri dali userot postoi
    @Transactional
    public User loadUserById(Long id){
        User user=userRepository.getById(id);
        if(user==null) new UsernameNotFoundException("User not found");
        return user;
    }
}

