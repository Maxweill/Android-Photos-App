package com.example.clifford.myapplication;
import java.util.*;
import java.io.*;

/**
 * Album Class
 * @author Max Mucha, Clifford Imhomoh
 */
public class Album implements Serializable{
    List<Photo> photos;

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;

    }

    String name;

    /**
     * getter for photos
     * @return
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * getter for name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getter for photolocations
     * @return
     */
    public HashSet<String> getPhotolocations() {
        return photolocations;
    }

    HashSet<String> photolocations;

    /**
     * Album constructor, takes in a name.
     * @param name
     */
    public Album(String name)
    {
        photos = new ArrayList<Photo>();
        this.name = name;
        photolocations = new HashSet<String>();
    }

    /**
     * adds a photo object to an album.
     * @param p
     * @return
     */
    public boolean addPhoto(Photo p)
    {
        if(photos.contains(p))
        {
            return false;
        }
        else
        {
            photos.add(p);
            photolocations.add(p.location);
            return true;
        }
    }

    /**
     * deletes a photo object from an album
     * @param p
     */
    public void deletePhoto(Photo p)
    {
            photolocations.remove(p.location);
            photos.remove(p);
    }

    /**
     * copies an photo from this album to a destination.
     * @param destination
     * @param copy
     * @return
     */
    public boolean copy(Album destination, Photo copy)
    {
        if (destination.photolocations.contains(copy.location))
        {
            return false;
        }
        destination.photos.add(copy);
        destination.photolocations.add(copy.location);
        return true;
    }

    /**
     * moves an photo from this album to another, then deletes it from this one.
     * @param destination
     * @param move
     * @return
     */
    public boolean move(Album destination, Photo move)
    {
        if (destination.photolocations.contains(move.location))
        {
            return false;
        }
        Photo temp = move;
        //System.out.println(photos.remove(move));
        photos.remove(move);
        photolocations.remove(move.location);

        destination.photos.add(move);
        destination.photolocations.add(move.location);
        return true;
    }




}
