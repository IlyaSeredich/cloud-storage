package by.practice.git.cloudstorage.service.impl;

import by.practice.git.cloudstorage.model.Role;
import by.practice.git.cloudstorage.repository.RoleRepository;
import by.practice.git.cloudstorage.service.IRoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRoleForNewUser() {
        String roleName = "ROLE_USER";
        Optional<Role> role = findByName(roleName);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            roleRepository.save(newRole);
            return newRole;
        }
        return role.get();
    }

    private Optional<Role> findByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}