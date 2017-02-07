package sample.model;

import com.google.common.collect.Sets;

/**
 * Created by No3x on 07.02.2017.
 */
public class ListCompare<T> {

    private Iterable<? extends T> gui;
    private Iterable<? extends T> database;
    private ISyncAction<T> action;

    public ListCompare(Iterable<? extends T> gui, Iterable<? extends T> database, ISyncAction<T> action) {
        this.gui = gui;
        this.database = database;
        this.action = action;
    }

    public void syncToDatabase() {
        action.added( addedItems() );
        action.removed( removedItems() );
    }

    private Iterable<T> addedItems() {
        return Sets.difference( Sets.newHashSet(database), Sets.newHashSet(gui));
    }

    private Iterable<T> removedItems() {
        return Sets.difference( Sets.newHashSet(gui),  Sets.newHashSet(database));
    }
}
