package com.sfs.image.mgmt.service;

import org.springframework.web.multipart.MultipartFile;

import com.sfs.image.mgmt.entity.Image;

import java.io.IOException;
import java.util.List;

public interface IImgurService {

    Image uploadImage(MultipartFile file, String username, String password) throws IOException;

    List<Image> getUserImages(String username, String password);

    void deleteImage(Long id, String username, String password);
}

