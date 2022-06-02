package com.cp.correioprivado;

import com.cp.correioprivado.dados.News;
import com.cp.correioprivado.dados.Role;
import com.cp.correioprivado.dados.Topic;
import com.cp.correioprivado.dados.User;
import com.cp.correioprivado.repo.RoleRepo;
import com.cp.correioprivado.repo.TopicRepo;
import com.cp.correioprivado.repo.UserRepo;
import com.cp.correioprivado.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;

@SpringBootApplication
public class CorreioPrivadoApplication {

    public CorreioPrivadoApplication(UserRepo userRepo, RoleRepo roleRepo, TopicRepo topicRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.topicRepo = topicRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(CorreioPrivadoApplication.class, args);
    }

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final TopicRepo topicRepo;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /*
    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role( "ROLE_USER","User"));
            userService.saveRole(new Role( "ROLE_ADMIN","Administrator"));
            userService.saveRole(new Role( "ROLE_MANAGER","Manager"));
            userService.saveRole(new Role( "ROLE_OFFICER","Officer"));

            userService.saveUser(new User("Fernando Fonseca","fernando","fernando@fonseca.pt","1234", roleRepo.findByName("ROLE_USER").getId()));
            userService.saveUser(new User("Ezequiel Barbosa","barbosa","ezequielbarbosa@valadas.pt","1234", roleRepo.findByName("ROLE_ADMIN").getId()));
            userService.saveUser(new User("Jacinto Meireles","meireles","jacinto@meireles.pt","1234", roleRepo.findByName("ROLE_MANAGER").getId()));
            userService.saveUser(new User("José Lopes da Silva","lopesdasilva","jose@lopesdasilva.pt","1234", roleRepo.findByName("ROLE_OFFICER").getId()));

            userService.addRoleToUser("fernando","ROLE_OFFICER");
            userService.addRoleToUser("meireles","ROLE_USER");
            userService.addRoleToUser("lopesdasilva","ROLE_ADMIN");
            userService.addRoleToUser("barbosa","ROLE_MANAGER");

            userService.saveTopic(new Topic("Desporto","Notícias de desporto"));
            userService.saveTopic(new Topic("Festivais","Festivais por todo o País"));
            userService.saveTopic(new Topic("Metereologia","Notícias do Tempo"));
            userService.saveTopic(new Topic("Romarias e Feiras","Romarias e feiras por todo o país"));

            userService.saveNews(new News("Benfica-Sporting","O jogo de benfica-sporting começará em breve",new Date(2022,5,22), userRepo.findByUsername("fernando").getId(), topicRepo.findByTitle("Desporto").getId()));
            userService.saveNews(new News("Festival de Coimbra","Coimbra terá um novo festival",new Date(2022,5,22), userRepo.findByUsername("meireles").getId(), topicRepo.findByTitle("Festivais").getId()));
            userService.saveNews(new News("Tempestade","Tempestade forte na região norte",new Date(2022,5,22), userRepo.findByUsername("lopesdasilva").getId(), topicRepo.findByTitle("Metereologia").getId()));
            userService.saveNews(new News("Feira Municipal","Feira municipal a decorrer em Odemira",new Date(2022,5,22), userRepo.findByUsername("barbosa").getId(), topicRepo.findByTitle("Romarias e Feiras").getId()));
        };
    }
    */
}
