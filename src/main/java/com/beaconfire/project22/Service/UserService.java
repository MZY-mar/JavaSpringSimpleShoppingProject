package com.beaconfire.project22.Service;

import com.beaconfire.project22.Dao.UserDao;
import com.beaconfire.project22.Model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class UserService {


    @Autowired
    private UserDao userDao;


    public  boolean isUsernameExists(String username){
        return  userDao.existsByUsername(username);
    }

    public boolean isEmailExists(String email){
        return  userDao.existsByEmail(email);
    }

    @Transactional
    public Users getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    public Users getUserByUserId(Long userid) {
        return userDao.findById(userid);
    }


    @Transactional
    public void registerUser(Users user) throws Exception{
        if(userDao.existsByUsername(user.getUsername())){
            throw new Exception("Username already exists");
        }
        if(userDao.existsByEmail(user.getEmail())){
            throw new Exception("User email already exists");
        }
        String pwd = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        System.out.println("password after encode : " + pwd);
        user.setPassword(pwd);
        userDao.add(user);
    }


    @Transactional
    public Users login(String username, String password) throws Exception {
        Users user = userDao.findByUsername(username);
        byte[] decodedBytes = Base64.getDecoder().decode(user.getPassword());
        String decodedPwd = new String(decodedBytes);
        if (user !=null && decodedPwd.equals(password)) {
            return user;
        }else {
        throw new Exception("Incorrect credentials, please try again.");
        }

    }
}
