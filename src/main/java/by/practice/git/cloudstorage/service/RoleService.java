package by.practice.git.cloudstorage.service;

import by.practice.git.cloudstorage.model.Role;
import by.practice.git.cloudstorage.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    public Role createRoleForNewUser() {
        String roleName = "ROLE_USER";
        Role role = findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        return role;
    }
}