package com.company.gym_system.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class StorageInitializer {

       @Autowired
       private Environment env;

       @Autowired
       private Map<String, Map<Long, Object>> storageMap;

       @PostConstruct
       public void init() throws IOException {
           String filePath = env.getProperty("data.file.path");
           if (filePath != null) {
               try {
                   // Remove classpath: prefix if present
                   if (filePath.startsWith("classpath:")) {
                       filePath = filePath.substring("classpath:".length());
                   }

                   var resource = getClass().getClassLoader().getResourceAsStream(filePath);
                   if (resource != null) {
                       String data = new String(resource.readAllBytes());

                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, List<Object>> parsedData = mapper.readValue(data, new TypeReference<>() {});

                        parsedData.forEach((key, value) -> {
                            storageMap.computeIfAbsent(key, k -> new HashMap<>());
                            value.forEach(entity -> {
                            });
                        });

                       // For now, just log that we found the file
                       log.info("Loaded data from {}", filePath);
                   } else {
                       log.error("Could not find resource: {}", filePath);
                   }
               } catch (Exception e) {
                   log.error("Error loading data from {}: {}", filePath, e.getMessage(), e);
               }
           }
       }
   }
