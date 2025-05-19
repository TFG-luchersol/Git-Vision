package org.springframework.samples.gitvision.util;

import java.util.Objects;

/**
 * <p>A <strong>ContainerItem</strong> is a abstract class used to 
 * represent the differents elements of a <strong>Container</strong></p>
 * 
 * <p>The class that extends ContainerItem must have a constructor with 
 * an Object[] parameter where the parsing of the query elements is performed</p>
 * @param <T> Type of the elements in the container
 * 
 * @see Container
 */
public abstract class ContainerItem {

    public ContainerItem(Object[] objects){
        Objects.requireNonNull(objects, "The constructor of a ContainerItem can't receive null as a parameter");
    }
    
}
