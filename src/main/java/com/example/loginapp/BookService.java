package com.example.loginapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> filterBooks(String keyword, String category, String status) {
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), pattern)
                ));
            }

            if (category != null && !category.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category.toLowerCase()));
            }

            if (status != null && !status.trim().isEmpty()) {
                try {
                    BookStatus bookStatus = BookStatus.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), bookStatus));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid status values
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(spec);
    }

    public Optional<Book> findRandomBook() {
        return bookRepository.findRandomBook();
    }
}
