package com.beaconfire.project22.Service;

import com.beaconfire.project22.Dao.UserDao;
import com.beaconfire.project22.Model.Users;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<Users> getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
    public void registerUser(Users user) throws Exception{
        if(userDao.existsByUsername(user.getUsername())){
            throw  new Exception("Username already exists");
        }
        if(userDao.existsByEmail(user.getEmail())){
            throw new Exception("User email already exists");
        }
        userDao.add(user);
    }

    public void login(String username, String password) throws Exception {
        Users user = userDao.findByUsername(username).orElseThrow(() ->
                new Exception("Incorrect credentials, please try again."));
        System.out.println("find user " + user.getUsername());
        if (!password.equals(user.getPassword())) {
            throw new Exception("Incorrect credentials, please try again.");
        }

    }
}
