package com.alexdo97.util;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Identity;
import com.alexdo97.model.Product;
import com.alexdo97.model.Role;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.repository.ProductRepository;
import com.alexdo97.repository.RoleRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private IdentityRepository identityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String ADMIN_ROLE = "ADMIN";
	private static final String USER_ROLE = "USER";

//	@Autowired
//	public DataLoader(CustomerRepository customerRepository, ProductRepository productRepository,
//			RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
//		this.customerRepository = customerRepository;
//		this.productRepository = productRepository;
//		this.roleRepository = roleRepository;
//		this.passwordEncoder = passwordEncoder;
//
//	}

	@Override
	public void run(ApplicationArguments args) {

//		roleRepository.save(new Role(ADMIN_ROLE, "This is admin role"));
//		roleRepository.save(new Role(USER_ROLE, "This is user role"));

		Role adminRole = new Role(ADMIN_ROLE, "This is admin role");
		roleRepository.save(adminRole);

		Role userRole = new Role(USER_ROLE, "This is user role");
		roleRepository.save(userRole);

		Customer newCustomer;
		Identity newIdentity;

		newCustomer = new Customer("alexdo97", "Alexandru", "Dobrin", "alexdo97@yahoo.com", "0754672152", new Cart());
		newIdentity = new Identity("alexdo97", getEncodedPassword("admin"), new ArrayList<>(), newCustomer);
		newIdentity.getRoleList().add(userRole);
		identityRepository.save(newIdentity);

		newCustomer = new Customer("david123", "David", "Dragomir", "david88@yahoo.com", "0754672322", new Cart());
		newIdentity = new Identity("david123", getEncodedPassword("test"), new ArrayList<>(), newCustomer);
		newIdentity.getRoleList().add(adminRole);
		identityRepository.save(newIdentity);

		newCustomer = new Customer("alinut", "Alin", "Badulea", "badulea66@yahoo.com", "0754652159", new Cart());
		newIdentity = new Identity("alinut", getEncodedPassword("test"), new ArrayList<>(), newCustomer);
		newIdentity.getRoleList().add(userRole);
		identityRepository.save(newIdentity);

		// Add customers
//		customerRepository.save(new Customer("Alexandru", "Dobrin", "alexdo97@yahoo.com", "0754672152",
//				new Identity("alexdo97", getEncodedPassword("admin"), adminRoleList), new Cart()));
//		customerRepository.save(new Customer("David", "Dragomir", "david88@yahoo.com", "0754672322",
//				new Identity("david123", getEncodedPassword("test"), userRoleList), new Cart()));
//		customerRepository.save(new Customer("Alin", "Badulea", "badulea66@yahoo.com", "0754652159",
//				new Identity("alinut", getEncodedPassword("alinnn123"), new ArrayList<>()), new Cart()));
//		customerRepository.save(new Customer("Mattia", "Baiguini", "mattia.baiguinii@gmail.com", "0756772122",
//				new Identity("mattia", getEncodedPassword("mattia07"), new ArrayList<>()), new Cart()));

		// Add products
		productRepository.save(new Product("T-shirt", Category.Fashion, 60));
		productRepository.save(new Product("Jeans", Category.Fashion, 150));

		productRepository.save(new Product("Intel Processor", Category.Technology, 4000));
		productRepository.save(new Product("Smart TV", Category.Technology, 3000));

		productRepository.save(new Product("Total war: Warhammer 2", Category.Games, 300));
		productRepository.save(new Product("Vermintide 2", Category.Games, 150));

		productRepository.save(new Product("Doll", Category.Toys, 300));

		productRepository.save(new Product("Table", Category.Furniture, 100));
		productRepository.save(new Product("Chair", Category.Furniture, 50.5));

		productRepository.save(new Product("Vitamin C pills", Category.Medicine, 15));
		productRepository.save(new Product("Bandage", Category.Medicine, 30));

	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

}
