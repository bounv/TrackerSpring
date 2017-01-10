package com.theironyard.services;

import com.theironyard.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by boun on 1/4/17.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findFirstByName(String username);
}
