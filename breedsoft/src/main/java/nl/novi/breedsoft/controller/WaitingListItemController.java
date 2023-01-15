package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.service.WaitingListItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;

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

    @GetMapping("/findbypersonid/{id}")
    public ResponseEntity<Iterable<WaitingListItemOutputDto>> getAllWaitingListItemsForPersonId(@PathVariable("id") Long id){
        return ResponseEntity.ok(waitingListItemService.getWaitingListItemByPersonID(id));
    }

}
