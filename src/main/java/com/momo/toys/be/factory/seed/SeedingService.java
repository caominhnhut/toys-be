package com.momo.toys.be.factory.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SeedingService {

    @Autowired
    private List<SeedDataService> seedDataServices;

    @Value("${mongodb.seed.ddl-auto}")
    private boolean isSeedData;

    @PostConstruct
    public void execute() {

        if (!isSeedData) {
            return;
        }

        for (SeedDataService seedDataService : seedDataServices) {
            seedDataService.seed();
        }
    }
}
