package ru.kata3_1_4.service;

import ru.kata3_1_4.DAO.RoleDAO;
import ru.kata3_1_4.DAO.UserDAO;
import ru.kata3_1_4.model.Role;
import ru.kata3_1_4.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User passwordCoder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User getById(long id) {
        User user = null;
        Optional<User> optional = userDAO.findById(id);
        if(optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public void save(User user) {
        userDAO.save(passwordCoder(user));
    }

    @Override
    public void update(User user) {
        userDAO.save(user);
    }

    @Override
    public void deleteById(long id) {
        userDAO.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    @PostConstruct
    public void addTestUsers() {
        Set<Role> userSet = new HashSet<>();
        userSet.add(roleDAO.findById(1L).orElse(null));
        Set<Role> adminSet = new HashSet<>();
        adminSet.add(roleDAO.findById(1L).orElse(null));
        adminSet.add(roleDAO.findById(2L).orElse(null));


        User admin = (new User("Shur", "Push", (byte)41, "alex@mail.com", "admin",
                "12345",adminSet));
        User user = (new User("Sveta", "Ryzh", (byte)48, "sveta@mail.com", "user",
                "00000", userSet));
        save(admin);
        save(user);
    }

}

