package ru.soknight.packetinventoryapi.util;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class IntRange {

    private final int min;
    private final int max;

    public IntRange(int single) {
        this.min = single;
        this.max = single;
    }
    
    public IntRange(int a, int b) {
        this.min = Math.min(a, b);
        this.max = Math.max(a, b);
    }
    
    public int[] items() {
        return itemsStream().toArray();
    }
    
    public Set<Integer> itemsSet() {
        return itemsStream().boxed().collect(Collectors.toSet());
    }
    
    public IntStream itemsStream() {
        return IntStream.range(min, max + 1);
    }
            
    public boolean contains(int i) {
        return min <= i && i <= max;
    }
    
    public boolean contains(IntRange range) {
        return min <= range.min && range.max <= max;
    }
    
    public boolean between(int i) {
        return min < i && i < max;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        IntRange intRange = (IntRange) o;
        return min == intRange.min && max == intRange.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "IntRange{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }

}
