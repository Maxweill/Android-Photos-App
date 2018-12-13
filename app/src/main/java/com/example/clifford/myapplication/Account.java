package com.example.clifford.myapplication;
import java.util.*;
import java.io.*;

/**
 * Album Class
 * @author Max Mucha, Clifford Imhomoh
 */

public class Account implements Serializable {
    List<Album> albums;

    /**
     * getter for masterphotolist
     * @return
     */
    public HashMap<String, Photo> getMasterphotolist() {
        return masterphotolist;
    }

    HashMap<String,Photo> masterphotolist;

    /**
     * getter for list of albums
     * @return
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * getter for username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * getter for albumnames
     * @return
     */
    public HashSet<String> getAlbumnames() {
        return albumnames;
    }

    /**
     * getter for tagtypes.
     * @return
     */
    public HashMap<String, Integer> getTagtypes() {
        return tagtypes;
    }

    String username;
    HashSet<String> albumnames;
    HashMap<String, Integer> tagtypes;

    /**
     * Account constructor, takes in a username.
     * @param username
     */
    public Account(String username) {
        this.username = username;
        albums = new ArrayList<Album>();
        albumnames = new HashSet<String>();
        masterphotolist = new HashMap<String,Photo>();
        tagtypes = new HashMap<String, Integer>();
        tagtypes.put("location", 1);
        tagtypes.put("person", 2);
    }

    /**
     * Creates an album, handles naming conflicts.
     * @param name
     * @return
     */
    public boolean createAlbum(String name) {
        if (albumnames.contains(name)) {
            return false;
        }
        albums.add(new Album(name));
        albumnames.add(name);

        return true;

    }

    /**
     * deletes an album.
     * @param name
     */
    public void deleteAlbum(String name) {
        Album temp = null;
        for (Album a : albums) {
            if (a.name.equals(name)) {
                temp = a;
                break;
            }
        }

        if (temp != null) {
            albums.remove(temp);
            albumnames.remove(name);
        }
    }

    /**
     * renames an album for a users list.
     * @param original
     * @param newname
     * @return
     */
    public boolean renameAlbum(String original, String newname) {
        if (albumnames.contains(newname)) {
            return false;
        }
        for (Album a : albums) {
            if (a.name.equals(original)) {
                a.name = newname;
                albumnames.remove(original);
                albumnames.add(newname);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a photo to an album, takes in photo path and the album
     * @param album
     * @param location
     * @return
     */
    public boolean addPhotoToAlbum(Album album, String location)
    {

        if(album.photolocations.contains(location))
        {
            return false;
        }
        if(masterphotolist.containsKey(location))
        {
            return album.addPhoto(masterphotolist.get(location));
        }
        
        Photo p = new Photo(location);
        if (album.addPhoto(p)) {
            masterphotolist.put(location, p);
            System.out.println("Successfully added stock photo:"+location);
            return true;
        }
        return false;
    }

    /**
     * removes a photo from an album, needs an object.
     * @param album
     * @param p
     * @return
     */

    public boolean removePhotoFromAlbum(Album album, Photo p)
    {
        boolean b = false;
        album.deletePhoto(p);
        for(Album a: albums)
        {
            if (a.photos.contains(p))
            {
                b = true;
            }
        }

        if(b)
        {
            return true;
        }
        else
        {
            masterphotolist.remove(p.location);
            return true;
        }
    }

    /**
     * searches for all photos between the start of day START and the end of day END
     * @param start
     * @param end
     * @return
     */
    public List<Photo> search(Date start, Date end)
    {
        List<Photo> output = new ArrayList<Photo>();
        HashSet<Photo> temp = new HashSet<Photo>();

        for(Album a: albums)
        {
            for(Photo p: a.photos)
            {
                temp.add(p);
            }
        }
        List<Photo> iterator = new ArrayList<Photo>(temp);

        for(Photo p: iterator)
        {
            if (!p.date.after(end) && !p.date.before(start))
            {
                output.add(p);
            }
        }
        return output;
    }

    /**
     * Searches based on tags and their values. type relates to the AND or OR property of the search.
     * @param tag
     * @param val1
     * @param tag2
     * @param val2
     * @param type
     * @return
     */
    public List<Photo> search(String tag, String val1, String tag2, String val2, int type)
    {
        List<Photo> output = new ArrayList<Photo>();
        HashSet<Photo> temp = new HashSet<Photo>();

        for(Album a: albums)
        {
            for(Photo p: a.photos)
            {
                temp.add(p);
            }
        }
        List<Photo> iterator = new ArrayList<Photo>(temp);

        for(Photo p: iterator)
        {
            if(type ==0) //1
            {
                if(p.hasTag(tag,val1))
                {
                    System.out.print(tag + ":" + val1);
                    output.add(p);
                }
            }
            if(type ==1) //and
            {
                if(p.hasTag(tag,val1) && p.hasTag(tag2,val2))
                {
                    output.add(p);
                }
            }
            if(type ==2)//or
            {
                if(p.hasTag(tag,val1) || p.hasTag(tag2,val2))
                {
                    output.add(p);
                }
            }
        }
        return output;
    }

    /**
     * Creates an album given a list and a name, used for turning search results into an album.
     * @param input
     * @param name
     * @return
     */
    public boolean createAlbumFromResults(List<Photo> input, String name)
    {
        if (albumnames.contains(name))
        {
            return false;
        }

        Album newalbum = new Album(name);
        for(Photo p: input)
        {
            newalbum.addPhoto(p);
        }
        albumnames.add(name);
        albums.add(newalbum);
        return true;
    }

}
