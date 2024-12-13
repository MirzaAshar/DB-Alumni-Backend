package com.alumni.blog.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventId;

    @Column(name = "event_name", nullable = false, length = 100)
    private String eventName;

    @Column(name = "event_location", nullable = false)
    private String eventLocation;

    @Column(name = "event_timings", nullable = false)
    private String eventTimings;

    @Column(name = "event_venue", nullable = false)
    private String eventVenue;

    @Column(name = "event_organizer", nullable = false)
    private String eventOrganizer;

    @Column(name = "event_description", length = 1000)
    private String eventDescription;



    @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "eventId"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> attendees = new HashSet<>();

    @Lob // Large Object Annotation for storing large data like images
    private byte[] imageData;
    private String imageName;
}
