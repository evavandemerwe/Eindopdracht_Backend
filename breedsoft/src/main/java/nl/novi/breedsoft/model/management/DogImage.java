package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class DogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    private String imageType;

    @Column(length = 1000)
    private byte[] dogImages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getDogImage() {
        return dogImages;
    }

    public void setDogImage(byte[] dogImages) {
        this.dogImages = dogImages;
    }
}
