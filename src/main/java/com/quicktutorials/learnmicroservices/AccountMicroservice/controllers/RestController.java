package com.quicktutorials.learnmicroservices.AccountMicroservice.controllers;

import com.quicktutorials.learnmicroservices.AccountMicroservice.entities.User;
import com.quicktutorials.learnmicroservices.AccountMicroservice.services.LoginService;
import com.quicktutorials.learnmicroservices.AccountMicroservice.services.OperationService;
import com.quicktutorials.learnmicroservices.AccountMicroservice.utils.UserNotLoggedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

// E' un controller che si occupa di gestire tutte le richieste http

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    OperationService operationService;

    // Mappatura delle request
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello everyone";
    }

    // User1
    @RequestMapping("/newuser1")
    public String addUser(User user){
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser2")
    public String addUserValid(@Valid User user) {
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser3")
    public String addUserValidPlusBinding(@Valid User user, BindingResult result) {
        if(result.hasErrors()) {
            return result.toString();
        }
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser4")
    public String addUserValidPlusBinding2(User user, BindingResult result) {
        /* Spring Validation */
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return result.toString();
        }
        return "User added correctly: " + user.getId() + ", " + user.getUsername();
    }

    /*---------------------------INNER CLASS------------------------*/
    //Spring Validator Example
    private class UserValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.equals(clazz);
        }

        @Override
        public void validate(Object obj, Errors errors) {
            User user = (User) obj;
            if (user.getPassword().length() < 8) {
                errors.rejectValue("password", "the password must be at least 8 chars long!");
            }
        }
    }

    /**
     * inner class used as the Object tied into the Body of the ResponseEntity.
     * It's important to have this Object because it is composed of server response code and response object.
     * Then, JACKSON LIBRARY automatically convert this JsonResponseBody Object into a JSON response.
     */
    @AllArgsConstructor
    public class JsonResponseBody{
        @Getter
        @Setter
        private int server;
        @Getter @Setter
        private Object response;
    }

    /*---------------------------------------------------------*/

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<JsonResponseBody> loginUser(@RequestParam(value="id") String id, @RequestParam(value="password") String pwd){
        try {
            Optional<User> userr = loginService.getUserFromDbAndVerifyPassword(id, pwd);
            if(userr.isPresent()){
                User user = userr.get();
                String jwt = loginService.createJwt(user.getId(), user.getUsername(), user.getPermission(), new Date());
                return ResponseEntity.status(HttpStatus.OK).header("jwt", jwt).body(new JsonResponseBody(HttpStatus.OK.value(), "Success! User logged in!"));
            }
        } catch (UserNotLoggedException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Login failed! Wrong credentials" + e1.toString())); // ResponseEntity
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Token error" + e2.toString())); // ResponseEntity
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "No corrispondence in DB")); // ResponseEntity
    }
}
