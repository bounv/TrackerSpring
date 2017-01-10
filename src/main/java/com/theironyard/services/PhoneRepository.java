package com.theironyard.services;

import com.theironyard.entities.Phone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by boun on 1/4/17.
 */
public interface PhoneRepository extends CrudRepository<Phone, Integer> {
    List<Phone> findByManufacturer (String manufacturer);

}
