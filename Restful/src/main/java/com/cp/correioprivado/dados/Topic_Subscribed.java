package com.cp.correioprivado.dados;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic_Subscribed {
    @Id
    @GeneratedValue(strategy = AUTO)
    private String id;
    private String user_id;
    private String topic_id;
}
