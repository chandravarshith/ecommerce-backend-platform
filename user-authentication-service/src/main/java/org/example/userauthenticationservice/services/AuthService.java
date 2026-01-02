package org.example.userauthenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.clients.KafkaProducerClient;
import org.example.userauthenticationservice.dtos.EmailDto;
import org.example.userauthenticationservice.exceptions.InvalidPasswordException;
import org.example.userauthenticationservice.exceptions.UserExistException;
import org.example.userauthenticationservice.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice.models.Session;
import org.example.userauthenticationservice.models.Status;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.SessionRepository;
import org.example.userauthenticationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${email.username}")
    private String senderUsername;

    private final String signupTopic = "SIGNUP";

    @Override
    public User signup(String email,  String password, String name, String phoneNumber) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            throw new UserExistException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);

        try{
            EmailDto emailDto = new EmailDto();
            emailDto.setSender(senderUsername);
            emailDto.setRecipient(email);
            emailDto.setSubject("Welcome to Product Catalog System!");
            emailDto.setMessage("Thank you for successfully registering with us." +
                    " Have a pleasant stay!");
            this.kafkaProducerClient.sendMessage(signupTopic,
                    this.objectMapper.writeValueAsString(emailDto));
        }catch (JsonProcessingException ex){
            throw new RuntimeException(ex.getMessage());
        }

        return userRepository.save(user);
    }

    @Override
    public Pair<User, String> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotRegisteredException("User not registered");
        }
        User user = userOptional.get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidPasswordException("Invalid password");
        }

        Map<String,Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("issued_by", "PCVG");
        claims.put("iat", System.currentTimeMillis());
        claims.put("exp", System.currentTimeMillis() + 900*1000);
        claims.put("roles", user.getRoles());

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setStatus(Status.ACTIVE);
        sessionRepository.save(session);

        return new Pair<>(user, token);
    }

    @Override
    public Boolean validateToken(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            return false;
        }

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiryTime = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        System.out.println("expiry = "+ expiryTime);
        System.out.println("currentTime = "+ currentTime);
        if (expiryTime < currentTime) {
            System.out.println("Token has expired");
            Session session = sessionOptional.get();
            session.setStatus(Status.INACTIVE);
            sessionRepository.save(session);
            return false;
        }
        return true;
    }

    @Override
    public Boolean logout(Long userId, String token) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if(sessionOptional.isEmpty()){
            return false;
        }

        Session session = sessionOptional.get();
        if(session.getStatus().equals(Status.INACTIVE)){
            return false;
        }
        session.setStatus(Status.INACTIVE);
        sessionRepository.save(session);
        return true;
    }
}
