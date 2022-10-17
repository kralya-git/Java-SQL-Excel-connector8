package com.test.idea;

public class Worker {

    private String name;
    private int age;
    private int salary;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getSalary() {
        return salary;
    }

    public Worker setName(String name) {
        this.name = name;
        return this;
    }

    public Worker setAge(int age) {
        this.age = age;
        return this;
    }

    public Worker setSalary(int salary) {
        this.salary = salary;
        return this;
    }

    @Override
    public String toString() {
        return "Worker{" + "name=" + name + ", age=" + age + ", salary=" + salary + '}';
    }

}