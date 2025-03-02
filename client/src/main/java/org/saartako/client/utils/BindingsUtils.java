package org.saartako.client.utils;

import javafx.beans.Observable;
import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;

import java.util.concurrent.Callable;

public class BindingsUtils {

    private BindingsUtils() {
    }

    public static <T> ListBinding<T> createListBinding(Callable<ObservableList<T>> func, Observable... dependencies) {
        return new ListBinding<>() {

            {
                bind(dependencies);
            }

            @Override
            protected ObservableList<T> computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    System.err.println(e);
                    return null;
                }
            }

            @Override
            public void dispose() {
                unbind(dependencies);
            }
        };
    }
}
