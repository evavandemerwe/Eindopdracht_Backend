package nl.novi.breedsoft.service;

import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingListItemService {

    private final WaitingListItemRepository waitingListItemRepository;

    public WaitingListItemService(WaitingListItemRepository waitingListItemRepository) {
        this.waitingListItemRepository = waitingListItemRepository;
    }

    public List<WaitingListItem> getAllWaitingListItems(){
        List<WaitingListItem> waitingListItemsList = waitingListItemRepository.findAll();
        return waitingListItemsList;
    }
}
