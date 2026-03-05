package com.nuria.cvplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CvService {

    private static final String CV_PATH = "static/cv/CV-NuriaOlivares.pdf";

    public Resource getCvFile() {
        Resource resource = new ClassPathResource(CV_PATH);

        if (!resource.exists()) {
            log.error("CV file not found at path: {}", CV_PATH);
            throw new RuntimeException("CV file not found");
        }

        return resource;
    }
}