package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.common.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Integer> {
}
