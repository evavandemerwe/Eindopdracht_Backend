package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemInputDto;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.service.WaitingListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("waitinglistitems")
public class WaitingListItemController {

    private final WaitingListItemService waitingListItemService;

    public WaitingListItemController(WaitingListItemService waitingListItemService) {
        this.waitingListItemService = waitingListItemService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItems(){
        return ResponseEntity.ok(waitingListItemService.getAllWaitingListItems());
    }

    @GetMapping("/personid/{id}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForPersonId(@PathVariable("id") Long id){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByPersonID(id));
    }
    @GetMapping("/sex/{sex}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForSex(@PathVariable("sex") String sex){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemBySex(sex));
    }
    @GetMapping("/breed/{breed}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForBreed(@PathVariable("breed") String breed){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByBreed(breed));
    }

    @GetMapping("/kindofhair/{kindofhair}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForKindOfHair(@PathVariable("kindofhair") String kindOfHair){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByKindOfHair(kindOfHair));
    }

    @GetMapping("/criteria")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForKindOfHair(
            @RequestParam("sex") String sex,
            @RequestParam("kindofhair") String kindOfHair,
            @RequestParam("breed") String breed){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByCriteria(sex, kindOfHair, breed));
    }

    @PostMapping("")
    public ResponseEntity<Object> createWaitingListItem(@Valid @RequestBody WaitingListItemInputDto waitingListItemInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            //VeterinarianAppointment is created, return new appointment id
            Long createdId = waitingListItemService.createWaitingListItem(waitingListItemInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/waitinglistitems/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Waiting list item is successfully created!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWaitingListItem(@PathVariable("id") Long id){
        waitingListItemService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
