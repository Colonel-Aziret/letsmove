package com.letsmove.service;

import com.letsmove.dao.TourRepository;
import com.letsmove.entity.*;
import com.letsmove.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GuidesService guidesService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private HistoryTourService historyTourService;

    public Tour save(Tour tour, MultipartFile img) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userService.findByLogin(auth.getName());
        Guides guides = guidesService.findByUserID(user);
        if (img != null && !img.isEmpty()) {
            byte[] imgBytes = img.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imgBytes);
            tour.setImg(base64Image);
        } else {
            // Обработка ситуации, когда файл не был выбран.
            // Здесь можно установить значение по умолчанию или выполнить другую логику.
            tour.setImg("https://upload.wikimedia.org/wikipedia/commons/9/9a/%D0%9D%D0%B5%D1%82_%D1%84%D0%BE%D1%82%D0%BE.png");
        }
        tour.setGuidesID(guides);
        tour.setCreatedDate(new Date());
        tour.setStatus(Status.NEW);
        tour.setVisitors(0);
        return tourRepository.save(tour);
    }

    public ArrayList<Tour> getAllNewTour() {
        return tourRepository.findAllByStatus(Status.NEW);
    }

    public ArrayList<Tour> getAllActiveTour() {
        return tourRepository.findAllByStatus(Status.ACTIVE);
    }

    public Tour getTourById(Integer id) {
        return tourRepository.findTourById(id);
    }

    public void updateTourStatus(Integer id, String status) {
        Tour tour = tourRepository.findTourById(id);
        Guides guides = tour.getGuidesID();
        Users users = guides.getUsersID();
        if (status.equals("ACTIVE")) {
            tour.setStatus(Status.ACTIVE);
            emailSenderService.sendEmail(users.getEmail(), "Поздравляю, по нашим взглядам ваш тур подходит для размещения на нашем сайте. \n Поэтому вам одобренно в доступе. \n Ваш тур уже размещён на сайте :)", "Фидбек на заявку");

        } else if (status.equals("UN_ACTIVE")) {
            tour.setStatus(Status.UN_ACTIVE);
            emailSenderService.sendEmail(users.getEmail(), "К сожалению, по нашим взглядам ваш тур не подходит для размещения на нашем сайте. \n Поэтому вам отказано в доступе. \n Попробуйте переделать вашу заявку и отправить повторно :)", "Фидбек на заявку");
        }
        tourRepository.save(tour);
    }

    public void bookTour(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        Tour tour = tourRepository.findTourById(id);
        Guides guides = tour.getGuidesID();
        Users guide = guides.getUsersID();
        tour.setVisitors(tour.getVisitors() + 1);
        emailSenderService.sendEmail(users.getEmail(), "Здравствуйте, " + users.getLogin() + " вы успешно забронировали тур " + tour.getNameTour() + "\n" + "Ваш гид для этого тура : " + guides.getFio() + "\n" + "Вы можете связаться с ним по этим данным: " + "Email: " + guide.getEmail() + "\nНомер: " + guides.getPhoneNumber(), "Бронирование тура!");
        emailSenderService.sendEmail(guide.getEmail(), "Здравствуйте, " + guides.getFio() + " ваш тур " + tour.getNameTour() + " успешно забронировали" + "Вы можете связаться с клиентом по этим данным: " + "Email: " + users.getEmail(), "Бронирование тура!");
        historyTourService.save(users, tour);
        tourRepository.save(tour);
    }

    public ArrayList<Tour> findAllToursByGuidesId(Guides id) {
        return tourRepository.findToursByGuidesID(id);
    }

    public List<Tour> getAllAuthorTour() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = userService.findByLogin(authentication.getName());
        Guides guides = guidesService.findByUserID(users);
        return tourRepository.findToursByStatusAndGuidesID(Status.ACTIVE, guides);
    }

    public void deleteTour(Integer tourId) {
        Tour tour = tourRepository.findTourById(tourId);
        tour.setStatus(Status.UN_ACTIVE);
        tourRepository.save(tour);
    }

}
