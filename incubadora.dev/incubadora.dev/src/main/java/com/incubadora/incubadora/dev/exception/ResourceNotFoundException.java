package com.incubadora.incubadora.dev.exception;

public class ResourceNotFoundException extends RuntimeException  {
    public ResourceNotFoundException(String message) {
        // Se llama al constructor de la clase padre (RuntimeException)
        // para establecer el mensaje de la excepción.
        super(message);
    }
}
