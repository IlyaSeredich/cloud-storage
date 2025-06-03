package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.model.Role;
import by.practice.git.cloudstorage.repository.RoleRepository;
import by.practice.git.cloudstorage.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefaultRole() {
        String roleName = "ROLE_USER";
        Optional<Role> role = findByName(roleName);
        return role.orElseGet(() -> createDefaultRole(roleName));
    }

    private Role createDefaultRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
        return role;
    }

    private Optional<Role> findByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}