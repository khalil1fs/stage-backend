package com.fst.sir.security.service.facade;

import com.fst.sir.security.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findByUsername(String username);

    User findByPhone(String phone);

    User findById(Long id);

    void deleteById(Long id);


    /*  Client  */
    User save(User user);

    /*  Admin  */
    public User saveAdmin(User user);

    /*  Gerant  */
    public User saveAGENT(User user);

    User update(User user);

    int delete(Long id);

    User findByUsernameWithRoles(String username);

    int  deleteByUsername(String username);

    public UserDetails loadUserByUsername(String username)  ;

}
