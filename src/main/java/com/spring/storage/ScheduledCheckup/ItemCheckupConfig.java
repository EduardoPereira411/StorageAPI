package com.spring.storage.ScheduledCheckup;

import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.ItemManagement.repositories.ItemRepository;
import com.spring.storage.ItemManagement.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemCheckupConfig {

    @Autowired
    private ItemRepository itemRepository;

    @Scheduled(cron = "0 0 0 * * *") // This runs the task every day at midnight
    public void performCheckup() {
        List<Item> allItems = itemRepository.findAll();

        for (Item item : allItems) {
            if (item.needsCheckup()) {
                item.setNeedsCheckup(true);
            }
        }
    }
}
