package tutorin.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorin.com.model.Resume;

public interface ResumeRepository extends JpaRepository<Resume, String> {
}
