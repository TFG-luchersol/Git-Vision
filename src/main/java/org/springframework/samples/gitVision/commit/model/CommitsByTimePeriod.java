package org.springframework.samples.gitvision.commit.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommitsByTimePeriod {
    
    private TimePeriod timePeriod;
    private Map<Integer, Integer> cont;

    public CommitsByTimePeriod(Integer[] array, TimePeriod timePeriod){
        this.timePeriod = timePeriod;
        
        switch(timePeriod){
            case DAY_OF_WEEK:
                // Map<Integer, Integer> map = Arrays.asList(array).stream()
                //     .collect(Collectors.toMap(ar -> ar[0], ar -> ar[1]));
                // for(int i = 1; i <= 7; i++) {
                //     map.putIfAbsent(i, 0);
                // }
                break;
            case HOUR:
                break;
            case MONTH:
                break;
            case YEAR:
                break;
        }
    }

    public static List<CommitsByTimePeriod> of(List<Integer[]> list, TimePeriod timePeriod){
        return list.stream().map(array -> new CommitsByTimePeriod(array, timePeriod)).toList();
    }
}
