package io.ennov.ticket_management.domain.ticket;

import io.ennov.ticket_management.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    private String id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusTicket statusTicket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
