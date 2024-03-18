package com.ojt.service.UserService;


import com.ojt.model.dto.request.SignInRequest;

public interface UserService {
    boolean signIn (SignInRequest signInRequest);
}
