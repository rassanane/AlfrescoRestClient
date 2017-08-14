package fr.scor.solem.model;

import com.google.api.client.util.Key;

/**
 * @author Rachid
 *
 */
public class Document {

    @Key
    public String id;

    @Key
    public String name;
    
    @Key
    public boolean isFile;
    
}
