package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.common.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool,Integer> {
}
