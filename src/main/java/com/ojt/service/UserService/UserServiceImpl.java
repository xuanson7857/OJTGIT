package com.ojt.service.UserService;

import com.ojt.model.dto.request.SignInRequest;
import com.ojt.model.entity.User;
import com.ojt.responsitoty.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService{
 @Autowired
 UserRepository userRepository;
 @Autowired
    HttpSession httpSession;
    @Override
    public boolean signIn(SignInRequest signInRequest) {
       User user = userRepository.findByUsername(signInRequest.getUsername()).orElse(null);
       if (user != null){
           if (user.getPassword().equals(signInRequest.getPassword())){
//               System.out.println("dsdsd");
               httpSession.setAttribute("userLogin", user);
               return true;
           }
       }
       return false;
    }
}
