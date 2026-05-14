package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seeds") // Ye API ka base address hai
@CrossOrigin(origins = "*") // Isse frontend se request aane mein problem nahi hogi
public class SeedController {

    @Autowired
    private SeedRepository seedRepository;

    // 1. Nayi guthli donate karne ke liye (POST Request)
    @PostMapping("/donate")
    public Seed donateSeed(@RequestBody Seed seed) {
        return seedRepository.save(seed);
    }

    // 2. Saari guthliyon ki list dekhne ke liye (GET Request)
    @GetMapping("/all")
    public List<Seed> getAllSeeds() {
        return seedRepository.findAll();
    }
}
