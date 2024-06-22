package com.sfs.image.mgmt.dto;



import lombok.Data;

import java.util.List;

import com.sfs.image.mgmt.entity.Image;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private List<Image> images;
}
