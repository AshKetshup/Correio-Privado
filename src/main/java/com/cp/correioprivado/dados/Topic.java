package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
}
