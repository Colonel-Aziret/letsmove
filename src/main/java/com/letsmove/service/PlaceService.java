package com.letsmove.service;

import com.letsmove.dao.PlaceRepository;
import com.letsmove.entity.City;
import com.letsmove.entity.Manager;
import com.letsmove.entity.Place;
import com.letsmove.entity.Users;
import com.letsmove.enums.PlaceType;
import com.letsmove.enums.Role;
import com.letsmove.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class PlaceService {
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ManagerService managerService;

    public void save(Place place, String cityName, MultipartFile img) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findByLogin(auth.getName());
        if (user.getRole().equals(Role.MANAGER)) {
            Manager manager = managerService.findByUserId(user);
            manager.setAllPlaces(manager.getAllPlaces() + 1);
            managerService.update(manager);
        }
        if (img != null && !img.isEmpty()) {
            byte[] imgBytes = img.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imgBytes);
            place.setImg(base64Image);
        } else {
            // Обработка ситуации, когда файл не был выбран.
            // Здесь можно установить значение по умолчанию или выполнить другую логику.
            place.setImg("https://upload.wikimedia.org/wikipedia/commons/9/9a/%D0%9D%D0%B5%D1%82_%D1%84%D0%BE%D1%82%D0%BE.png");
        }



        place.setUsersID(user);
        place.setCityID(cityName);
        place.setCreatedDate(new Date());
        place.setStatus(Status.NEW);
        place.setRating(0.0);
        placeRepository.save(place);
    }

    public List<Place> getAllActivePlace() {
        return placeRepository.findAllByStatus(Status.ACTIVE);
    }

    public List<Place> getByCity(String city) {
        return placeRepository.findByCityID(city);
    }

    public List<Place> getAllNewPlace() {
        return placeRepository.findAll();
    }

    public void updatePlaceStatus(Integer id, String status) {
        Place place = placeRepository.findPlaceById(id);
        Users users = place.getUsersID();
        if (status.equals("ACTIVE")) {
            place.setStatus(Status.ACTIVE);
            emailSenderService.sendEmail(users.getEmail(), "Поздравляю, по нашим взглядам ваше заведение " + place.getPlaceName() + " подходит для размещения на нашем сайте. \n Поэтому вам одобренно в доступе. \n Ваше заведение уже размещено на сайте :)", "Фидбек на заявку");

        } else if (status.equals("UN_ACTIVE")) {
            place.setStatus(Status.UN_ACTIVE);
            emailSenderService.sendEmail(users.getEmail(), "К сожалению, по нашим взглядам ваше заведение " + place.getPlaceName() + " не подходит для размещения на нашем сайте. \n Поэтому вам отказано в доступе. \n Попробуйте переделать вашу заявку и отправить повторно :)", "Фидбек на заявку");

        }
        placeRepository.save(place);
    }

    public Place getPlaceById(Integer id) {
        return placeRepository.findPlaceById(id);
    }

    public void changeRating(Integer placeId, Integer rating) {
        Place place = placeRepository.findPlaceById(placeId);
        if (place.getRating() == 0) {
            place.setRating(rating.doubleValue());
        } else {
            double ratingNum = (place.getRating() + rating) / 2;
            place.setRating(round(ratingNum, 1));
        }
        placeRepository.save(place);
    }

    public void deletePlace(Integer placeId) {
        Place place = placeRepository.findPlaceById(placeId);
        place.setStatus(Status.UN_ACTIVE);
        placeRepository.save(place);
    }

    public List<Place> allHotel() {
        return placeRepository.findPlacesByPlaceType(PlaceType.HOTEL);
    }

    public List<Place> allAttraction() {
        return placeRepository.findPlacesByPlaceType(PlaceType.ATTRACTION);
    }

    public List<Place> allCafe() {
        return placeRepository.findPlacesByPlaceType(PlaceType.CAFE);
    }

    public List<Place> allMarket() {
        return placeRepository.findPlacesByPlaceType(PlaceType.MARKET);
    }

    public List<Place> allShoppingCenter() {
        return placeRepository.findPlacesByPlaceType(PlaceType.SHOPPING_CENTER);
    }

    public List<Place> allStateInstitutions() {
        return placeRepository.findPlacesByPlaceType(PlaceType.STATE_INSTITUTIONS);
    }

    public List<Place> getAllAuthorPlace() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        return placeRepository.findPlacesByStatusAndUsersID(Status.ACTIVE, users);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
