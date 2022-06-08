package com.cp.correioprivado;

import com.cp.correioprivado.dados.News;
import com.cp.correioprivado.dados.Role;
import com.cp.correioprivado.dados.Topic;
import com.cp.correioprivado.dados.User;
import com.cp.correioprivado.email.EmailSenderService;
import com.cp.correioprivado.repo.RoleRepo;
import com.cp.correioprivado.repo.TopicRepo;
import com.cp.correioprivado.repo.UserRepo;
import com.cp.correioprivado.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class CorreioPrivadoApplication {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final TopicRepo topicRepo;

    public CorreioPrivadoApplication(EmailSenderService emailSenderService, UserRepo userRepo, RoleRepo roleRepo, TopicRepo topicRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.topicRepo = topicRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(CorreioPrivadoApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role( "Consumidor","Pode ler as notícias."));
            userService.saveRole(new Role( "Produtor","Pode produzir notícias."));

            userService.saveUser(
                new User(
                    "Fernando Fonseca",
                    "fernando",
                    "fernando@fonseca.pt",
                    "1234",
                    roleRepo.findByName("Consumidor")
                )
            );

            userService.saveUser(
                new User(
                    "Ezequiel Barbosa",
                    "barbosa",
                    "ezequielbarbosa@valadas.pt",
                    "1234",
                    roleRepo.findByName("Produtor")
                )
            );

            userService.saveUser(
                new User(
                    "Jacinto",
                    "Meireles",
                    "dsimoes2000@gmail.com",
                    "1234",
                    roleRepo.findByName("Consumidor")
                )
            );

            userService.saveUser(
                new User(
                    "José Lopes da Silva",
                    "lopesdasilva",
                    "diogoc.simoes@gmail.com",
                    "1234",
                    roleRepo.findByName("Produtor")
                )
            );

            userService.saveTopic(new Topic("Desporto","Notícias de desporto"));
            userService.saveTopic(new Topic("Festivais","Festivais por todo o País"));
            userService.saveTopic(new Topic("Metereologia","Notícias do Tempo"));
            userService.saveTopic(new Topic("Romarias e Feiras","Romarias e feiras por todo o país"));

            userService.subscribeTopic(
                userRepo.findByEmail("dsimoes2000@gmail.com").getEmail(),
                topicRepo.findByTitle("Desporto").getTitle()
            );

            userService.subscribeTopic(
                userRepo.findByEmail("diogoc.simoes@gmail.com").getEmail(),
                topicRepo.findByTitle("Festivais").getTitle()
            );

            userService.saveNews(
                new News(
                    "Benfica-Sporting",
                    "O jogo de benfica-sporting começará em breve",
                    new Date(),
                    userRepo.findByEmail("ezequielbarbosa@valadas.pt"),
                    topicRepo.findByTitle("Desporto")
                )
            );

            TimeUnit.SECONDS.sleep(5);

            userService.saveNews(
                new News(
                    "Festival de Coimbra",
                    "Coimbra terá um novo festival",
                    new Date(),
                    userRepo.findByEmail("diogoc.simoes@gmail.com"),
                    topicRepo.findByTitle("Festivais")
                )
            );

            TimeUnit.SECONDS.sleep(5);


            userService.saveNews(
                new News(
                    "Tempestade",
                    "Tempestade forte na região norte",
                    new Date(),
                    userRepo.findByEmail("ezequielbarbosa@valadas.pt"),
                    topicRepo.findByTitle("Metereologia")
                )
            );

            TimeUnit.SECONDS.sleep(5);

            userService.saveNews(
                new News(
                    "Feira Municipal",
                    "Feira municipal a decorrer em Odemira",
                    new Date(),
                    userRepo.findByEmail("dsimoes2000@gmail.com"),
                    topicRepo.findByTitle("Romarias e Feiras")
                )
            );

            // userService.removeTopicSubscribed(userRepo.findByUsername("fernando").getUsername(),topicRepo.findByTitle("Festivais").getTitle());
            log.info("Reached end of pre-filled data...");
        };
    }*/
}
