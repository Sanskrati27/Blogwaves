package com.blogwaves.main;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.blogwaves.main.config.ConstantValues;
import com.blogwaves.main.entities.Role;
import com.blogwaves.main.repositories.RoleRepository;

@SpringBootApplication
public class BlogwavesApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogwavesApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		
		try {
			Role role1 = new Role();
			role1.setId(ConstantValues.ROLE_ADMIN);
			role1.setName(ConstantValues.ADMIN);
			
			Role role2 = new Role();
			role2.setId(ConstantValues.ROLE_USER);
			role2.setName(ConstantValues.USER);
			
			List<Role> roles = List.of(role1, role2);
			roleRepository.saveAll(roles);
			
//			 List<Role> role = roleRepository.findAll();
//			    System.out.println("Roles in DB: " + role.size());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
