package com.example.finalProject.services;

import com.example.finalProject.domain.Employee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ISecurityGeneratorImp implements ISecurityTokenGenerator {
    @Override
    public Map<String, String> generateToken(Employee employee) {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> userData = new HashMap<>();
        userData.put("userName", employee.getUserName());
        String myToken = Jwts.builder().setClaims(userData)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "PROJECTEzhilMahekPriyanshu")
                .compact();
        result.put("Token", myToken);
        result.put("Message", "Employee loggedIn Successfully");
        return result;
    }
}
