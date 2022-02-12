package com.example.restfulwebservice.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount =3;
    static {
        users.add(new User(1,"gunho",new Date(),"test1","951023-1111111"));
        users.add(new User(2,"minho",new Date(),"test2","940113-1111111"));
        users.add(new User(3,"jinho",new Date(),"test3","811123-1111111"));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if(user.getId()==null){
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if(user.getId()==id){
                return user;
            }
        }
        return null;
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();
        if(iterator.hasNext()){
            if(iterator.next().getId()==id){
                iterator.remove();
                return iterator.next();
            }
        }
        return null;
    }

    public User updateUser(int id, User user) {
        for (User originUser : users) {
            if(originUser.getId() == id){
                originUser.setName(user.getName());
                originUser.setJoinDate(new Date());
                return originUser;
            }
        }
        return null;
    }
}
