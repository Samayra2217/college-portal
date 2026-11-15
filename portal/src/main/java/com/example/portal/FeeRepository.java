
package com.example.portal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    Optional<Fee> findByStudentId(Long studentId);
}

