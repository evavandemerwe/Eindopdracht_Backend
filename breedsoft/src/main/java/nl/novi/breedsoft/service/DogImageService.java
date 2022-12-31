package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogImageDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.management.DogImage;
import nl.novi.breedsoft.repository.DogImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DogImageService {

    private final DogImageRepository dogImageRepository;

    public DogImageService(DogImageRepository dogImageRepository) {
        this.dogImageRepository = dogImageRepository;
    }

    public List<DogImageDto> getAllImages() {
        List<DogImage> dogImageList = dogImageRepository.findAll();
        return (transferDogImageListToDtoList(dogImageList));
    }
    public DogImageDto storeDogImage(MultipartFile file) throws IOException {

            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            DogImage dogImage = new DogImage();
            dogImage.setImageName(fileName);
            dogImage.setImageType(file.getContentType());
            dogImage.setDogImage(file.getBytes());
            dogImageRepository.save(dogImage);

            return (transferToDogImageDto(dogImage));
    }

    public DogImageDto getDogImageById(Long id) {
        if(dogImageRepository.findById(id).isPresent()) {

            DogImage dogImage = dogImageRepository.findById(id).get();
            return (transferToDogImageDto(dogImage));

        } else {

            throw new RecordNotFoundException("No images with given ID found.");

        }
    }

    public void deleteDogImage(Long id){
        if(dogImageRepository.findById(id).isPresent()){
            dogImageRepository.deleteById(id);
        }else{
            throw new  RecordNotFoundException("No images with given ID found.");
        }
    }

    private DogImageDto transferToDogImageDto(DogImage dogImage){

        DogImageDto dogImageDto = new DogImageDto();
        dogImageDto.setId(dogImage.getId());
        dogImageDto.setImageName(dogImage.getImageName());
        dogImageDto.setImageType(dogImage.getImageType());
        dogImageDto.setDogImage(dogImage.getDogImage());

        return dogImageDto;
    }

    private List<DogImageDto> transferDogImageListToDtoList(List<DogImage> dogImages){
        List<DogImageDto> dogImageDtoList= new ArrayList<>();

        for(DogImage dogImage : dogImages){
            DogImageDto dto = transferToDogImageDto(dogImage);
            dogImageDtoList.add(dto);
        }
        return dogImageDtoList;
    }
}