package io.ennov.ticket_management.domain.user;

import io.ennov.ticket_management.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    private String id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> ticketList = new ArrayList<>();
}
