package org.saartako.client.utils;

import javafx.beans.Observable;
import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
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

    public static <T> ListBinding<T> createJavaListBinding(Callable<List<T>> func, Observable... dependencies) {
        return createListBinding(() -> FXCollections.observableArrayList(func.call()), dependencies);
    }
}
