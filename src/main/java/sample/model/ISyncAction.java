package sample.model;

/**
 * Created by No3x on 07.02.2017.
 */
public interface ISyncAction<T> {
    void added(Iterable<? extends T> added);
    void removed(Iterable<? extends T> removed);
}