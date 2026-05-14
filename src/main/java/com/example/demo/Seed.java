package com.example.demo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String donorName;
    private int quantity;
    private String location;
}
