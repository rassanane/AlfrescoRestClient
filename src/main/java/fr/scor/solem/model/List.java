package fr.scor.solem.model;

import java.util.ArrayList;

import com.google.api.client.util.Key;

/**
 * @author Rachid
 *
 * @param <T>
 */
public class List<T extends Entry> {
    @Key
    public ArrayList<T> entries;

    @Key
    public Pagination pagination;
}
