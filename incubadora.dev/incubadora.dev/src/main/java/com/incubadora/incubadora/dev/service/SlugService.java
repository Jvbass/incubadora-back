package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class SlugService {

    private final ProjectRepository projectRepository;

    public SlugService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String generateUniqueSlug(String title) {
        String baseSlug = toSlug(title);
        String uniqueSlug = baseSlug;
        int counter = 2;

        //Si existe el slug le añadimos un numero al final
        while (projectRepository.findBySlug(uniqueSlug).isPresent()) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }
        return uniqueSlug;
    }

    // Metodo  para convertir cualquier título a un slug válido.
    private String toSlug(String input) {
        if (input == null) {
            throw new IllegalArgumentException("El título no puede ser nulo para generar un slug.");
        }
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}

