package com.cp.correioprivado.dados;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    private String isRead;
    @ManyToOne
    private News news;
    @ManyToOne
    private User user;
}
