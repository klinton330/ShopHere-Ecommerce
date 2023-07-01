package com.shophere.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shophere.admin.Repository.RoleRepository;
import com.shophere.admin.Repository.UserRepository;
import com.shophere.admin.exception.ResourceNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {

	public static final int USER_PER_PAGE=5;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}

	public Page<User>listByPage(int pageNum)
	{
		Pageable pageable=PageRequest.of(pageNum-1, USER_PER_PAGE);
		return userRepository.findAll(pageable);
	}
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}

	public User saveUser(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			// Update Logic
			// existingUser ->Having the Existing user Details.
			// user-Having the update details
			User existingUser = userRepository.findById(user.getId()).get();
			// Check if user is updating password
			if (user.getPassword().isEmpty()) {
				// No means set existing User Password
				user.setPassword(existingUser.getPassword());
			} else
				encodePassword(user);
		} else {
			encodePassword(user);
		}
		return userRepository.save(user);
	}

	private void encodePassword(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		// If our email id is unique and not db
		if (userByEmail == null)
			return true;
		boolean iscreatingNew = (id == null);
		// While creating if we try to create with other user email id
		if (iscreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			// This check for if we try to update other user email id with ours
			if (userByEmail.getId() != id)
				return false;
		}
		return true;
	}

	public User getUser(Integer id) {
		try {
			User user = userRepository.findById(id).get();
			return user;
		} catch (Exception ex) {
			throw new UsernameNotFoundException("Could not find the User with Id:" + id);
		}
	}
	
	public void delete(Integer id)
	{
		Long countById=userRepository.countById(id);
		if(countById==null||countById==0)
			throw new UsernameNotFoundException("Could not find the User with Id:" + id);
		else
			userRepository.deleteById(id);
	}
	
	public void updateUserEnabledService(Integer id,boolean enabled)
	{
		userRepository.updateEnabledStatus(id, enabled);
	}
}
