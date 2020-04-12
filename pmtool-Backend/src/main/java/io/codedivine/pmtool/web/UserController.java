package io.codedivine.pmtool.web;

import io.codedivine.pmtool.domain.User;
import io.codedivine.pmtool.payload.JWTLoginSucessResponse;
import io.codedivine.pmtool.payload.LoginRequest;
import io.codedivine.pmtool.security.JwtTokenProvider;
import io.codedivine.pmtool.services.MapValidationErrorService;
import io.codedivine.pmtool.services.UserService;
import io.codedivine.pmtool.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.codedivine.pmtool.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
     private UserService userService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap !=null) return errorMap;

        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()

                )

        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessResponse(true,jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){//validiranje dali passwordite se isti
        //validacija
        userValidator.validate(user,result);
        ResponseEntity<?> errorMap=mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        User newUser=userService.saveUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

}
