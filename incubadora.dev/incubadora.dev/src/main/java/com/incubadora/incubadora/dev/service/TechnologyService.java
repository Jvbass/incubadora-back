package com.incubadora.incubadora.dev.service;


import com.incubadora.incubadora.dev.dto.TechnologyDto;
import com.incubadora.incubadora.dev.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyService {
    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Transactional(readOnly = true)
    public List<TechnologyDto> getAllTechnologies() {
        return technologyRepository.findAll().stream()
                .map(tech -> new TechnologyDto(tech.getId(), tech.getName(), tech.getTechColor()))
                .collect(Collectors.toList());
    }

}