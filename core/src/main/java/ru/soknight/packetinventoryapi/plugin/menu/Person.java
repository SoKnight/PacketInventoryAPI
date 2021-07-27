package ru.soknight.packetinventoryapi.plugin.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public final class Person {

    private final String realName;
    private final String nickname;
    private final int age;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(realName, person.realName) &&
                Objects.equals(nickname, person.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(realName, nickname, age);
    }

    @Override
    public String toString() {
        return "Person{" +
                "realName='" + realName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }

}
