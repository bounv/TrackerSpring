package com.theironyard.controllers;

import com.theironyard.entities.Phone;
import com.theironyard.entities.User;
import com.theironyard.services.PhoneRepository;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by boun on 1/4/17.
 */
@Controller
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
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/add-phone", method = RequestMethod.POST)
    public String addPhone(HttpSession session, String manufacturer, String brand) {
        String username = (String) session.getAttribute("username");
        User user = users.findFirstByName(username);

        Phone phone = new Phone(manufacturer, brand, user);
        phones.save(phone);
        return "redirect:/";
    }

    @RequestMapping(path = "/update-phone", method = RequestMethod.POST)
    public String updatePhone(HttpSession session, int id, String manufacturer, String brand) throws Exception {

        if (session.getAttribute("username") == null) {
            throw new Exception("Not logged in.");
        }


        if (manufacturer == null) {
            throw new Exception("Didn't get necessary query parameters.");
        }

        if (brand == null) {
            throw new Exception("Didn't get necessary query parameters.");
        }

        String username = (String) session.getAttribute("username");

        Phone phone = (Phone) phones.findOne(id);

        if(phone.user.equals(users.findFirstByName(username))) {
            phone.manufacturer = manufacturer;
            phone.brand = brand;
            phones.save(phone);
        } else {
            throw new Exception("EDIT your own phone");
        }

        return "redirect:/";

    }

    @RequestMapping(path = "delete-phone", method = RequestMethod.POST)
    public String addPhone(HttpSession session, int id, String manufacturer, String brand) throws Exception {

        if (session.getAttribute("username") == null) {
            throw new Exception("Not logged in.");
        }

        String username = (String) session.getAttribute("username");

        Phone phone = (Phone) phones.findOne(id);

        if(phone.user.equals(users.findFirstByName(username))) {
            phones.delete(phone);
        } else {
            throw new Exception("Delete your own phone");
        }

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
