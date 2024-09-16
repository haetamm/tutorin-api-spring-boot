package tutorin.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorin.com.model.Image;

public interface ImageRepository extends JpaRepository<Image, String> {
}
