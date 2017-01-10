package com.theironyard.controllers;

import com.theironyard.entities.Phone;
import com.theironyard.entities.User;
import com.theironyard.services.PhoneRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by boun on 1/4/17.
 */
public class TrackerSpringController {
    @Autowired
    PhoneRepository phones;

    @Autowired
    UserRepository users;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model, String manufacturer, String brand) {

        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);

        if (user != null) {
            model.addAttribute("user", user);
        }

        List<Phone> phoneList;

        if (manufacturer != null) {
            phoneList = phones.findByManufacturer(manufacturer);
        } else if(brand != null) {
            phoneList = phones.findByBrand(brand);
        } else {
            phoneList = (List<Phone>) phones.findAll();
        }

        model.addAttribute("phones", phoneList);
        return "home";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String username, String password) throws Exception {
        User user = users.findFirstByName(username);
        if(user == null) {
            user = new User(username, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if(!PasswordStorage.verifyPassword(password, user.password)) {
            throw new Exception("Incorrect Password");
        }
        session.setAttribute("user", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }





    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        if(users.count() == 0) {
            User user = new User();
            user.name = "Boun";
            user.password = PasswordStorage.createHash("password");
            users.save(user);
        }
    }

}
