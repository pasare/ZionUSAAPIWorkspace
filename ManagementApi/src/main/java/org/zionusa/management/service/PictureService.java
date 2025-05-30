package org.zionusa.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.zionusa.management.dao.ChurchPictureDao;
import org.zionusa.management.dao.UserDao;
import org.zionusa.management.dao.UserPictureDao;
import org.zionusa.management.domain.Church;
import org.zionusa.management.domain.User;
import org.zionusa.management.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class PictureService {

    private final ChurchPictureDao churchPictureDao;

    private final UserPictureDao userPictureDao;

    private final UserDao userDao;

    @Autowired
    public PictureService(ChurchPictureDao churchPictureDao, UserPictureDao userPictureDao, UserDao userDao) {
        this.churchPictureDao = churchPictureDao;
        this.userPictureDao = userPictureDao;
        this.userDao = userDao;
    }

    public List<User.UserPicture> getAllUserPictures() {
        return userPictureDao.findAll();
    }

    public User.UserPicture getUserProfilePicture(Integer userId) {
        return userPictureDao.findByUserId(userId);
    }

    @CacheEvict(cacheNames = "users-cache", allEntries = true)
    public void deleteUserPicture(Integer id) {
        Optional<User.UserPicture> userPictureOptional = userPictureDao.findById(id);

        if (!userPictureOptional.isPresent())
            throw new NotFoundException("Cannot delete a picture that does not exist");

        Optional<User> userOptional = userDao.findById(userPictureOptional.get().getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPictureId(null);
            userDao.save(user);
        }
        
        userPictureDao.delete(userPictureOptional.get());
    }

    @CacheEvict(cacheNames = "users-cache", allEntries = true)
    public User.UserPicture saveUserPicture(User.UserPicture userPicture) {

        //  profile pictures should be replaced, cannot have more than one
        Optional<User> userOptional = userDao.findById(userPicture.getUserId());

        if (!userOptional.isPresent())
            throw new NotFoundException("Cannot add a profile picture for a user that does not exist");
        User user = userOptional.get();

        if (user.getProfilePicture() != null) {
            userPicture.setId(user.getProfilePicture().getId());
        }

        userPicture.setPictureFull(null);
        userPicture.setPictureMedium(null);
        userPicture.setThumbnail(null);
        User.UserPicture savedPicture = userPictureDao.save(userPicture);
        user.setPictureId(savedPicture.getId());
        user.setProfilePicture(savedPicture);
        userDao.save(user);

        return savedPicture;
    }

    public List<Church.ChurchPicture> getAllChurchPictures() {
        return churchPictureDao.findAll();
    }

    public Church.ChurchPicture getChurchProfilePicture(Integer churchId) {
        return churchPictureDao.findByChurchIdAndTypeId(churchId, 2);
    }

    public List<Church.ChurchPicture> getChurchPicturesByChurchId(Integer churchId) {
        return churchPictureDao.findAllByChurchIdAndTypeId(churchId, 1);
    }

    public void deleteChurchPicture(Integer id) {
        Optional<Church.ChurchPicture> churchPictureOptional = churchPictureDao.findById(id);

        if (!churchPictureOptional.isPresent())
            throw new NotFoundException("Cannot delete a picture that does not exist");

        churchPictureDao.delete(churchPictureOptional.get());
    }

    public Church.ChurchPicture saveChurchPicture(Church.ChurchPicture churchPicture) {

        churchPicture.setPictureFull(null);
        churchPicture.setPictureMedium(null);
        churchPicture.setThumbnail(null);
        return churchPictureDao.save(churchPicture);
    }
}
