package com.example.clifford.myapplication;
import java.util.*;
import java.io.*;

/**
 * Tag class
 * @author Max Mucha, Clifford Imhomoh
 */
public class Tag implements Serializable{
    String name;
    List<String> values;

    /**
     * Tag constructor, takes in a name (tagtype) and value.
     * @param name
     * @param value
     */
    public Tag(String name, String value)
    {
        this.name = name;
        values = new ArrayList<String>();
        values.add(value);
    }

    /**
     *  addValue, adds a value to the list of values.
     * @param value
     */
    public void addValue(String value)
    {
            values.add(value);
    }

    /**
     *  Getter for name.
     * @return
     */
    public String getName () {

        return this.name;
    }

    /**
     *  Getter for values
     * @return
     */
    public List<String> getValues (){

        return this.values;
    }

    /**
     *  ToString used to neatly print out tags and their values.
     * @return
     */
    public String toString()
    {
        String output=name+": ";
        for(String s: values)
        {
            output+=s+", ";
        }
        return output.substring(0,output.length()-2);
    }

}
