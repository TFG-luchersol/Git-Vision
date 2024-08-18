package org.springframework.samples.gitvision.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>A <strong>Container</strong> is a typed abstract class used for parsing queries 
 * that return Collection< Object[]></p>
 * 
 * @param <T> Type of the elements in the container
 * 
 * @see ContainerItem
 */
public abstract class Container<T extends ContainerItem> {

    protected List<T> container;

    public Container(Collection<Object[]> collection){
        Objects.requireNonNull(collection, "The constructor of a Container can't receive null as a parameter");
        if(collection.isEmpty())
            this.container = new ArrayList<>();
        else {
            try {
                Class<?> clazz = getTypeOfT();
                Constructor<?> method = clazz.getConstructor(Object[].class);
                List<T> container = collection.stream().map(elem -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T mappedElem = (T) method.newInstance((Object) elem);
                        return mappedElem;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                this.container = container;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<T> getContainer(){
        return this.container;
    }

    private Class<?> getTypeOfT() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                return (Class<?>) typeArguments[0];
            }
        }
        throw new IllegalStateException("No se puede determinar el tipo de T.");
    }

}
