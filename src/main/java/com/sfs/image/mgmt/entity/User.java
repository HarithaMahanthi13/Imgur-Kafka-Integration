package com.sfs.image.mgmt.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

/**
 * Entity class representing a user in the system.
 */
@Entity
@Data
@Table(name = "sfs_users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The username of the user.
     */
    public String sfsUser;

    /**
     * The password of the user.
     */
    @ToString.Exclude
    public String sfsUserpassword;

    /**
     * The list of images associated with the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> image;
}
