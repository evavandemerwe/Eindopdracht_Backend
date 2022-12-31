package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.DogImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogImageRepository extends JpaRepository<DogImage, Long> {
        Optional<DogImage> findByImageName(String imageName);
    }

