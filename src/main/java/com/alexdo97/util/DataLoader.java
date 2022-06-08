package com.alexdo97.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Identity;
import com.alexdo97.model.Product;
import com.alexdo97.model.Role;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductRepository;
import com.alexdo97.repository.RoleRepository;

@Component
public class DataLoader implements ApplicationRunner {

	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private RoleRepository roleRepository;

	private static final String ADMIN_ROLE = "ADMIN";
	private static final String USER_ROLE = "USER";

	@Autowired
	public DataLoader(CustomerRepository customerRepository, ProductRepository productRepository,
			RoleRepository roleRepository) {
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public void run(ApplicationArguments args) {

		roleRepository.save(new Role(ADMIN_ROLE, "This is admin role"));
		roleRepository.save(new Role(USER_ROLE, "This is user role"));

		List<Role> allRoles = roleRepository.findAll();

		Role adminRole = roleRepository.findByRoleName(ADMIN_ROLE);
		List<Role> adminRoleList = new ArrayList<>();
		adminRoleList.add(adminRole);

		Role userRole = roleRepository.findByRoleName(USER_ROLE);
		List<Role> userRoleList = new ArrayList<>();
		userRoleList.add(userRole);

		// Add customers
		customerRepository.save(new Customer("Alexandru", "Dobrin", "alexdo97@yahoo.com", "0754672152",
				new Identity("alexdo97", "admin", allRoles), new Cart()));
		customerRepository.save(new Customer("David", "Dragomir", "david88@yahoo.com", "0754672322",
				new Identity("david123", "test", userRoleList), new Cart()));
		customerRepository.save(new Customer("Alin", "Badulea", "badulea66@yahoo.com", "0754652159",
				new Identity("alinut", "alinnn123", adminRoleList), new Cart()));
		customerRepository.save(new Customer("Mattia", "Baiguini", "mattia.baiguinii@gmail.com", "0756772122",
				new Identity("mattia", "mattia07", allRoles), new Cart()));

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

}
