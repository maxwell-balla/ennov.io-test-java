package io.ennov.ticket_management.ticket;

import io.ennov.ticket_management.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusTicket status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
