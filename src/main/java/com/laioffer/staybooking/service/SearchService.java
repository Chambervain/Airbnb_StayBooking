package com.laioffer.staybooking.service;

import com.laioffer.staybooking.model.Stay;
import com.laioffer.staybooking.repository.LocationRepository;
import com.laioffer.staybooking.repository.StayRepository;
import com.laioffer.staybooking.repository.StayReservationDateRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {

    private final StayRepository stayRepository;
    private final LocationRepository locationRepository;
    private final StayReservationDateRepository stayReservationDateRepository;

    public SearchService(StayRepository stayRepository, LocationRepository locationRepository, StayReservationDateRepository stayReservationDateRepository) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }

    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance) {

//        1. search in ElasticSearch
        List<Long> stayIds = locationRepository.searchByDistance(lat, lon, distance);
        if (stayIds == null || stayIds.isEmpty()) {
            return new ArrayList<>();
        }

//        2. search stays that already reserved
        Set<Long> reservedStayIds = stayReservationDateRepository.findByIdInAndDateBetween(stayIds, checkinDate, checkoutDate.minusDays(1));

//        3. create a new arraylist to filter the stay_id that already reserved
        List<Long> filteredStayIds = new ArrayList<>();
        for (Long stayId : stayIds) {
            if (!reservedStayIds.contains(stayId)) {
                filteredStayIds.add(stayId);
            }
        }

//        4. check stay guest number >= guest number passed by search request
        return stayRepository.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }

}
