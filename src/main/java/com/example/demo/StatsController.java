package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/summary")
    public StatsSummary getSummary() {
        List<Seed> seeds = seedRepository.findAll();

        long totalSeeds = seeds.stream()
                .mapToLong(Seed::getQuantity)
                .sum();
        long totalTrees = totalSeeds / 20;

        long totalDonors = seeds.stream()
                .map(Seed::getDonorName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .count();

        long totalVolunteers = userRepository.countByRole(UserRole.VOLUNTEER);

        List<TopDonor> topDonors = seeds.stream()
                .filter(seed -> seed.getDonorName() != null && !seed.getDonorName().trim().isEmpty())
                .collect(Collectors.groupingBy(Seed::getDonorName, Collectors.summingLong(Seed::getQuantity)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new TopDonor(entry.getKey(), entry.getValue(), entry.getValue() / 2))
                .collect(Collectors.toList());

        return new StatsSummary(totalSeeds, totalTrees, totalDonors, totalVolunteers, topDonors);
    }
}

class StatsSummary {
    private long totalSeeds;
    private long totalTrees;
    private long totalDonors;
    private long totalVolunteers;
    private List<TopDonor> topDonors;

    public StatsSummary(long totalSeeds, long totalTrees, long totalDonors, long totalVolunteers, List<TopDonor> topDonors) {
        this.totalSeeds = totalSeeds;
        this.totalTrees = totalTrees;
        this.totalDonors = totalDonors;
        this.totalVolunteers = totalVolunteers;
        this.topDonors = topDonors;
    }

    public long getTotalSeeds() {
        return totalSeeds;
    }

    public long getTotalTrees() {
        return totalTrees;
    }

    public long getTotalDonors() {
        return totalDonors;
    }

    public long getTotalVolunteers() {
        return totalVolunteers;
    }

    public List<TopDonor> getTopDonors() {
        return topDonors;
    }
}

class TopDonor {
    private String donorName;
    private long seedsDonated;
    private long treesPlanted;

    public TopDonor(String donorName, long seedsDonated, long treesPlanted) {
        this.donorName = donorName;
        this.seedsDonated = seedsDonated;
        this.treesPlanted = treesPlanted;
    }

    public String getDonorName() {
        return donorName;
    }

    public long getSeedsDonated() {
        return seedsDonated;
    }

    public long getTreesPlanted() {
        return treesPlanted;
    }
}
