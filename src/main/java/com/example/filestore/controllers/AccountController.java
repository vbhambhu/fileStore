package com.example.filestore.controllers;


import com.example.filestore.entities.ActionStatus;
import com.example.filestore.entities.PasswordResetForm;
import com.example.filestore.entities.PasswordUpdateForm;
import com.example.filestore.entities.User;
import com.example.filestore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
public class AccountController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "account/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(User user){
        return "account/register";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.GET)
    public String passwordReset(PasswordResetForm passwordResetForm){
        return "account/password_reset";
    }

    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public String validateAndSendPasswordRestLink(@RequestParam(name = "email") String email,
                                                  @Valid PasswordResetForm passwordResetForm,
                                                  BindingResult bindingResult,
                                                  Model model){

        if(bindingResult.hasErrors()){
            return "account/password_reset";
        } else if(!userService.isValidEmailAddress(passwordResetForm.getEmail())){
            bindingResult.rejectValue("email", "email", "Invalid email address. No account exists with this email.");
            return "account/password_reset";
        }


        ActionStatus actionStatus = userService.sendPasswordRestLink(email);

        model.addAttribute("message", "Reset email sent.Please check your email and follow the instructions.");
        return "account/message";



    }


    @RequestMapping(value = "/admin/user/create", method = RequestMethod.GET)
    public String createUser(User user){
        return "admin/users/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String validateAndSaveUser(@Valid User user, BindingResult bindingResult){

        if(!userService.usernameExists(user.getUsername())){
            bindingResult.rejectValue("username", "username", "Username already exists.");
        } else if(!userService.emailExists(user.getEmail())){
            bindingResult.rejectValue("email", "email", "Email address already exists");
        }

        if(bindingResult.hasErrors()){
            return "admin/users/create";
        }

        userService.create(user);

        return "redirect:/users";
    }

    @RequestMapping(value = "/password/update", method = RequestMethod.GET)
    public String templogin(@RequestParam(name = "token", required = false) String token,
                            Model model, PasswordUpdateForm passwordUpdateForm){

        if(token == null){
            return "account/invalid_token";
        }

        if(!userService.checkLoginToken(token)){
            return "account/invalid_token";
        }

        model.addAttribute("token", token);

        return "account/password_update";
    }

    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public String activateAccount(@RequestParam(name = "token", required = true) String token,
                                  @Valid PasswordUpdateForm passwordUpdateForm,
                                  BindingResult bindingResult,
                                  Model model){


        if(!userService.checkLoginToken(token)){
            return "account/invalid_token";
        }

        model.addAttribute("token", token);

        if(!passwordUpdateForm.getPassword().equals(passwordUpdateForm.getConfirmPassword())){
            bindingResult.rejectValue("password","passwordResetForm.password", "New Password and Verify New Password did not match.");
        }

        if(bindingResult.hasErrors()){
            return "account/password_update";
        }

        //reset password and activate account

        userService.activateAccountByToke(token, passwordUpdateForm.getPassword());



        System.out.println("all okkkkk");

        return "account/message";

    }




    /*

    @Autowired
    UserRepository userRepository;

    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "account/login";
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(User user){
        return "account/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerCheck(@Valid User user, BindingResult bindingResult){

        String tempPassword = randomAlphaNumeric(10);

        if(!isValidUsername(user.getUsername())){
            bindingResult.rejectValue("username", "username", "Invalid username.");
        } else if(!usernameExists(user.getUsername())){
            bindingResult.rejectValue("username", "username", "Username already exists, please login.");
        } else if(!emailExists(user.getEmail())){
            bindingResult.rejectValue("email", "email", "Email address already exists, please login.");
        } else if (!user.getEmail().contains("ox.ac.uk")) {
            bindingResult.rejectValue("email", "email", "Only Oxford University emails are allowed to register.");
        }

        if (bindingResult.hasErrors()) {
            return "account/register";
        }

        //Create user
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setTempPassword(passwordEncoder.encode(tempPassword));
        user.setStatus(-1);
        user.setRole("USER");
        userRepository.save(user);

        String msg = "Thank you for registering KIR Imaging Competition. Your password is "+ tempPassword;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify your account.");
        message.setText(msg);
        emailSender.send(message);

        return "redirect:/register/verifiy";
    }

    @RequestMapping(value = "/register/verifiy", method = RequestMethod.GET)
    public String verifyAccountMessage(Model model){
        model.addAttribute("message", "An email sent to you. Please verify your email.");
        return "message";
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.GET)
    public String updatePassword(Model model){

        return "account/updatepassword";
    }


    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public String updatePasswords(Model model, Principal principal,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "confirmpassword") String confirmpassword){


        if(password.isEmpty()){
            model.addAttribute("errMsg", "Password field is required.");
            return "account/updatepassword";
        } else if(password.length() < 5){
            model.addAttribute("errMsg", "Password field must be minimum 5 characters in length.");
            return "account/updatepassword";
        } else if(!password.equals(confirmpassword)){
            model.addAttribute("errMsg", "Password and Confirm password field did not match.");
            return "account/updatepassword";
        }

        //update password

        System.out.println(principal.getName());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByUsername(principal.getName());
        user.setPassword(passwordEncoder.encode(password));
        user.setTempPassword(null);
        user.setStatus(1);
        userRepository.save(user);

        return "redirect:/dashboard";

    }


    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String forgotPassForm(){
        return "account/forgot";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String forgotPass(Model model,
                             @RequestParam(name = "email") String email){

        System.out.println(email);

        if(email.isEmpty()){
            model.addAttribute("errMsg", "Email field is required.");
            return "account/forgot";
        } else if(!isValidEmailAddress(email) || !email.contains("ox.ac.uk")){
            model.addAttribute("errMsg", "Email field must be valid email address.");
            return "account/forgot";
        }


        User user = userRepository.findByEmail(email);

        if(user != null){
            //Update temp password, make passnull, status = -1
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String tempPassword = randomAlphaNumeric(10);
            user.setPassword(null);
            user.setTempPassword(passwordEncoder.encode(tempPassword));
            user.setStatus(-1);
            userRepository.save(user);
            //send reset email

            String msg = "Your new password is "+ tempPassword;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Reset password.");
            message.setText(msg);
            emailSender.send(message);
        }


        model.addAttribute("message", "Reset email sent.Please check your email and follow the instructions.");
        return "message";


    }

    public boolean isValidEmailAddress(String email) {

        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }





    private boolean isValidUsername(String username){

        String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }


    private String randomAlphaNumeric(int count) {

        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private boolean usernameExists(String username){
        User user = userRepository.findByUsername(username);
        return (user == null) ? true : false;
    }

    private boolean emailExists(String email){
        User user = userRepository.findByEmail(email);
        return (user == null) ? true : false;
    }
    */
}
