package com.incubadora.incubadora.dev.service;


import com.incubadora.incubadora.dev.dto.TechnologyDto;
import com.incubadora.incubadora.dev.mapper.TechnologyMapper;
import com.incubadora.incubadora.dev.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnologyService {
    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    public TechnologyService(TechnologyRepository technologyRepository, TechnologyMapper technologyMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyMapper = technologyMapper;
    }

    @Transactional(readOnly = true)
    public List<TechnologyDto> getAllTechnologies() {
        return technologyRepository.findAll().stream()
                .map(technologyMapper::toDto) // Reemplaza la lógica de mapeo manual con el mapper
                .collect(Collectors.toList());
    }

}