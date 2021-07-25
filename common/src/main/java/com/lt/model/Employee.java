package com.lt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Employee {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String name;
    private String dept;
}
