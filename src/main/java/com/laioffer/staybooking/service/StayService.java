package com.laioffer.staybooking.service;

import com.laioffer.staybooking.exception.StayNotExistException;
import com.laioffer.staybooking.model.Stay;
import com.laioffer.staybooking.model.User;
import com.laioffer.staybooking.repository.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StayService {
    private final StayRepository stayRepository;

    public StayService(StayRepository stayRepository) {
        this.stayRepository = stayRepository;
    }

    public List<Stay> listByUser(String username) {
        return stayRepository.findByHost(new User.Builder().setUsername(username).build());
    }

    public Stay findByIdAndHost(Long stayId, String username) throws StayNotExistException {

        User user = new User.Builder().setUsername(username).build();
        Stay stay = stayRepository.findByIdAndHost(stayId, user);

        if(stay == null){
            throw new StayNotExistException("Stay Does Not Exist");
        }

        return stay;
    }

    public void add(Stay stay) {
        stayRepository.save(stay);
    }

    @Transactional
    public void delete(Long stayId, String username) throws StayNotExistException {

        User user = new User.Builder().setUsername(username).build();
        Stay stay = stayRepository.findByIdAndHost(stayId, user);

        if(stay == null){
            throw new StayNotExistException("Stay Does Not Exist");
        }

        stayRepository.deleteById(stayId);
    }
}
