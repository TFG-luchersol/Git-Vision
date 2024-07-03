package org.springframework.samples.gitvision.commit.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommitsByTimePeriod {
    
    private TimePeriod timePeriod;
    private Map<Integer, Integer> cont;

    public CommitsByTimePeriod(List<Integer[]> list, TimePeriod timePeriod){
        this.timePeriod = timePeriod;
        this.cont = list.stream().collect(Collectors.toMap(array -> array[0], array -> array[1]));
        switch(timePeriod){
            case HOUR -> {
                for(int i = 0; i <= 23; i++) 
                    this.cont.putIfAbsent(i, 0);
            }
            case DAY_OF_WEEK -> {
                for(int i = 1; i <= 7; i++) 
                    this.cont.putIfAbsent(i, 0);
            }
            case MONTH -> {
                for(int i = 1; i <= 12; i++) 
                    this.cont.putIfAbsent(i, 0);
            }
            case YEAR -> {
                List<Integer> sortedYears = this.cont.keySet().stream().collect(Collectors.toList());
                sortedYears.sort(Comparator.naturalOrder());
                Integer min, max, n = sortedYears.size();
                min = sortedYears.get(0);
                max = sortedYears.get(n - 1);
                for(int i = min; i <= max; i++) 
                    this.cont.putIfAbsent(i, 0);
            }
        }
    }

    public static CommitsByTimePeriod of(List<Integer[]> list, TimePeriod timePeriod){
        return new CommitsByTimePeriod(list, timePeriod);
    }

    public Integer get(Integer key){
        return this.cont.get(key);
    }
}
