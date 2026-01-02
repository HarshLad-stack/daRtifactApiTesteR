package com.example.dartifactapitester.Controller;

import com.example.dartifactapitester.DTO.StressTestRequest;
import com.example.dartifactapitester.DTO.VerdictResponse;
import com.example.dartifactapitester.Service.StressTestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stress-test")
public class StressTestController {

    private final StressTestService stressTestService;

    public StressTestController(StressTestService stressTestService) {
        this.stressTestService = stressTestService;
    }
    private void validate(StressTestRequest req){
        if (req.getUrl() == null || req.getUrl().isBlank()) {
            throw new IllegalArgumentException("URL must not be empty");
        }
        if (req.getConcurrency() <= 0) {
            throw new IllegalArgumentException("Concurrency must be > 0");
        }
        if (req.getDurationSeconds() <= 0) {
            throw new IllegalArgumentException("Duration must be > 0");
        }
    }

    @PostMapping
    public VerdictResponse runStressTest(@RequestBody StressTestRequest request){
        validate(request);
        return  stressTestService.execute(request);


    }
}
