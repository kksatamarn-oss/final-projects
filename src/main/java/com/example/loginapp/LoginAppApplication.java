package com.example.loginapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoginAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {
            // Add sample books only if the repository is empty
            if (repository.count() == 0) {
                repository.save(new Book("The Red and the Black", "Stendhal", "Fiction"));
                repository.save(new Book("A Brief History of Time", "Stephen Hawking", "Educational"));
                repository.save(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "Non-Fiction"));
                repository.save(new Book("The Art of War", "Sun Tzu", "Non-Fiction"));
                repository.save(new Book("Dune", "Frank Herbert", "Fiction"));
                repository.save(new Book("1984", "George Orwell", "Fiction"));
                repository.save(new Book("Cosmos", "Carl Sagan", "Educational"));
                repository.save(new Book("Thinking, Fast and Slow", "Daniel Kahneman", "Non-Fiction"));
                repository.save(new Book("To Kill a Mockingbird", "Harper Lee", "Fiction"));
                repository.save(new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "Fiction"));
                repository.save(new Book("Educated", "Tara Westover", "Non-Fiction"));
                repository.save(new Book("Atomic Habits", "James Clear", "Non-Fiction"));
                repository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction"));
                repository.save(new Book("The Selfish Gene", "Richard Dawkins", "Educational"));
                repository.save(new Book("Becoming", "Michelle Obama", "Non-Fiction"));
            }
        };
    }
}
