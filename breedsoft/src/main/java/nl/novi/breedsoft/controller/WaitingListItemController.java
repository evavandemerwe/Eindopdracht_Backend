package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemInputDto;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.service.WaitingListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;

@RestController
@RequestMapping("waitinglistitems")
public class WaitingListItemController {

    private final WaitingListItemService waitingListItemService;

    public WaitingListItemController(WaitingListItemService waitingListItemService) {
        this.waitingListItemService = waitingListItemService;
    }

    /**
     * GET method to get a list of waiting list items from the database
     * @return ResponseEntity with OK http status code and a list with all waiting list items
     */
    @GetMapping("")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItems(){
        return ResponseEntity.ok(waitingListItemService.getAllWaitingListItems());
    }

    /**
     * GET method to get a waiting list item from the database by id
     * @param waitingListItemId ID of the waiting list item for which information is requested
     * @return ResponseEntity with OK http status code and the requested waiting list item
     */
    @GetMapping("/personid/{id}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForPersonId(
            @PathVariable("id") Long waitingListItemId
    ){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByPersonID(waitingListItemId));
    }

    /**
     * GET method to get a list of waiting list items by sex
     * @param sex the sex for which waiting list items are requested
     * @return ResponseEntity with OK http status code and a list with all waiting list items
     */
    @GetMapping("/sex/{sex}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForSex(
            @PathVariable("sex") String sex
    ){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemBySex(sex));
    }

    /**
     * GET method to get a list of waiting list items by breed
     * @param breed the breed for which waiting list items are requested
     * @return ResponseEntity with OK http status code and a list with all waiting list items
     */
    @GetMapping("/breed/{breed}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForBreed(@PathVariable("breed") String breed){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByBreed(breed));
    }

    /**
     * GET method to get a list of waiting list items by kind of hair
     * @param kindOfHair the kind of hair for which waiting list items are requested
     * @return ResponseEntity with OK http status code and a list with all waiting list items
     */
    @GetMapping("/kindofhair/{kindofhair}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForKindOfHair(@PathVariable("kindofhair") String kindOfHair){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByKindOfHair(kindOfHair));
    }

    /**
     * GET method to get a list of waiting list items by criteria sex, kind of hair and breed
     * @param sex the sex for which waiting list items are requested
     * @param kindOfHair the kind of hair for which waiting list items are requested
     * @param breed the breed for which waiting list items are requested
     * @return ResponseEntity with OK http status code and a list with all waiting list items
     */
    @GetMapping("/criteria")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForKindOfHair(
            @RequestParam("sex") String sex,
            @RequestParam("kindofhair") String kindOfHair,
            @RequestParam("breed") String breed){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByCriteria(sex, kindOfHair, breed));
    }

    /**
     * POST method to create a new waiting list item in the database
     * @param waitingListItemInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createWaitingListItem(
            @Valid @RequestBody WaitingListItemInputDto waitingListItemInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            //VeterinarianAppointment is created, return new appointment id
            Long createdId = waitingListItemService.createWaitingListItem(waitingListItemInputDto);
            URI uri = createUri(createdId, "/waitinglistitems/");
            return ResponseEntity.created(uri).body("Waiting list item is successfully created!");
        }
    }

    /**
     * DELETE method to delete a waiting list item by id
     * @param waitingListItemId ID of the waiting list item for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWaitingListItem(@PathVariable("id") Long waitingListItemId){
        waitingListItemService.deleteWaitingListItem(waitingListItemId);
        return ResponseEntity.noContent().build();
    }
}
