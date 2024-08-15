package tutorin.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorin.com.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
}
