package Model;
import java.util.*;
import java.io.*;

/**
 * Photo class
 * @author Max Mucha, Clifford Imhomoh
 */

public class Photo implements Serializable{
    Date date;
    List<Tag> tags;
    String location;
    String caption;
    HashSet<String> tagtypes;

    /**
     * Photo constructor, takes in a filepath
     * @param location
     */
    public Photo(String location)
    {
        this.location = location;
        this.tags = new ArrayList<Tag>();
        File f = new File(location);
        if (f.exists())
        {
            date = new Date(f.lastModified());
        }
        else
        {
            //System.out.println("Error, failed to import picture. Try again.");
        }
        this.caption = "-";
        tagtypes = new HashSet<String>();
    }

    /**
     * Setter for caption.
     * @param caption
     */
    public void addCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * Checks if a photo has a tag with matching value/
     * @param tag
     * @param value
     * @return
     */
    public boolean hasTag(String tag, String value)
    {
        for(Tag t: tags)
        {
            if(t.name.equals(tag))
            {
                for(String s: t.values)
                {
                    if (s.startsWith(value) || s.equals(value))
                    {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Adds a tag to a photo, handles checking for proper tag types.
     * @param tag
     * @param value
     * @param type
     * @return
     */
    public boolean addTag(String tag, String value, int type)
    {
        if (tagtypes.contains(tag))
        {
            if(type==1)
            {
                return false;
            }
            else
            {
                for(Tag t: tags)
                {
                    if (t.name.equals(tag))
                    {
                        for(String v: t.values)
                        {
                            if(v.equals(value))
                            {
                                return false;
                            }
                        }
                        t.addValue(value);
                        return true;
                    }
                }
            }
        }
        else {
            tagtypes.add(tag);
            tags.add(new Tag(tag, value));
            return true;
        }
        return true;
    }

    /**
     * Deletes a tag object form the list of tags.
     * @param tag
     */
    public void deleteTag(String tag)
    {
        Tag temp=null;
        for(Tag t: tags)
        {
            if (t.name.equals(tag))
            {
                temp = t;
            }
        }
        tags.remove(temp);
        tagtypes.remove(tag);
    }

    /**
     * Getter for location.
     * @return
     */
    public String getLocation (){

        return this.location;
    }

    /**
     * getter for caption.
     * @return
     */
    public String getCaption (){

        return  this.caption;
    }

    /**
     * Getter for date
     * @return
     */
    public Date getDate () {

        return this.date;
    }

    /**
     * Getter for tags.
     * @return
     */

    public List<Tag> getTags () {

        return this.tags;
    }

}
