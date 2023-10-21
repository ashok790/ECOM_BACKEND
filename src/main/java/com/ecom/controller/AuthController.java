package com.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.config.JwtProvider;
import com.ecom.exception.UserException;
import com.ecom.model.Cart;
import com.ecom.model.User;
import com.ecom.repository.UserRepository;
import com.ecom.request.LoginRequest;
import com.ecom.response.AuthResponse;
import com.ecom.service.CartService;
import com.ecom.service.CustomeUserServiceImplementation;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final CustomeUserServiceImplementation customUserService;
	private final CartService cartService;
	private final JavaMailSender mailSender;
	
	
	

	
//	public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder,
//			CustomeUserServiceImplementation customUserService, CartService cartService, JavaMailSender mailSender) {
//		super();
//		this.userRepository = userRepository;
//		this.jwtProvider = jwtProvider;
//		this.passwordEncoder = passwordEncoder;
//		this.customUserService = customUserService;
//		this.cartService = cartService;
//		this.mailSender = mailSender;
//	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user)throws UserException{
		
		String email=user.getEmail();
		String password=user.getPassword();
		String firstName=user.getFirstName();
		String lastName=user.getLastName();
		
		User isEmailExist=userRepository.findByEmail(email);
		
		if(isEmailExist!=null) {
			throw new UserException("Email Is Already Used With Another Account");
		}
		
		
		
		User createdUser=new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setFirstName(firstName);
		createdUser.setLastName(lastName);
		createdUser.setRole("user");
		
		User savedUser=userRepository.save(createdUser);
		Cart cart=cartService.createCart(savedUser);
		
		Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup Success");
		sendEmail(email,firstName);
	
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
		
		String username=loginRequest.getEmail();
		String password=loginRequest.getPassword();
		
		Authentication authentication=authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(token);
		
		authResponse.setMessage("Signin Success");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		
		
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid Username...");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password...");
		}
		
		return new UsernamePasswordAuthenticationToken( userDetails,null,userDetails.getAuthorities());
	}

	
  private void sendEmail(String email,String firstName) {
	  SimpleMailMessage msg=new SimpleMailMessage();
	  msg.setFrom(firstName);
	  msg.setFrom(email);
		msg.setTo(email);
//		msg.setText("Sign Up Successfull");
//		msg.setSubject("Registration Mail.....Successfully Registered");
//		mailSender.send(msg);
		
//		message.setFrom("carHubShowroomAndServices@gmail.com");
//		message.setTo(emailId);
		msg.setSubject("Registration Mail.....Successfully Registered");
		msg.setText("Dear "+firstName+" \n\n\n"+
				"Thank you for registering with Us...!!!"+"\n\n"+
		"We are happy to serve you....!!!!"+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
		
		"**** Keep Shopping...!!! ****");
		mailSender.send(msg);
  }
}