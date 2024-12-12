package com.alumni.blog.controllers;

import com.alumni.blog.entities.Event;
import com.alumni.blog.repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepo eventRepository;

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    // Create a new event
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(    @RequestPart("event") Event event,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) {
        String imageDirectory = "E:\\Blog-Api\\images-event";
        if (image != null && !image.isEmpty()) {
            // Generate a unique image name
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String imagePath = imageDirectory + File.separator + imageName;

            // Ensure the directory exists
            File directory = new File(imageDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file to the directory
            try {
                // Save the file
                Files.copy(image.getInputStream(), Paths.get(imagePath));
                event.setImageName(imageName);
                event.setImageData(image.getBytes());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save the image file: " + ex.getMessage(), ex);
            }
            event.setImageName(imageName);
        } else {
            event.setImageName("default.png"); // Use a default image if no file is uploaded
        }
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    // Get a single event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return ResponseEntity.ok(event);
    }

    // Update an existing event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestBody Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        event.setEventName(updatedEvent.getEventName());
        event.setEventLocation(updatedEvent.getEventLocation());
        event.setEventTimings(updatedEvent.getEventTimings());
        event.setEventVenue(updatedEvent.getEventVenue());
        event.setEventOrganizer(updatedEvent.getEventOrganizer());
        event.setEventDescription(updatedEvent.getEventDescription());


        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    // Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        eventRepository.delete(event);
        return ResponseEntity.noContent().build();
    }
}
