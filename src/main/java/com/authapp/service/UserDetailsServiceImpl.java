package com.authapp.service;

import com.authapp.jwt.JwtUtils;
import com.authapp.persistance.entity.RoleEntity;
import com.authapp.persistance.entity.UserEntity;
import com.authapp.persistance.repository.RoleRepository;
import com.authapp.persistance.repository.UserRepository;
import com.authapp.presentation.dto.AuthCreateUserDTO;
import com.authapp.presentation.dto.AuthUserLoginDTO;
import com.authapp.presentation.dto.AuthUserProfileResponseDTO;
import com.authapp.presentation.dto.AuthUserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("The user with email " + email + " not exists"));

        List<GrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles()
                .forEach((role) -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))
                ));

        userEntity.getRoles().stream()
                .flatMap((role) -> role.getPermissions().stream())
                .forEach((permission) -> authorities.add(
                        new SimpleGrantedAuthority(permission.getPermissionEnum().name())
                ));

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnable(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorities
        );
    }

    public AuthUserResponseDTO createUser(AuthCreateUserDTO userDTO) {
        String fullNames = userDTO.getFullNames();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        List<String> roles = userDTO.getRoleRequest().getRoles();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roles));

        if (roleEntitySet.isEmpty()) throw new IllegalArgumentException("The role specified does not exists.");

        UserEntity user = UserEntity.builder()
                .fullNames(fullNames)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        user = userRepository.save(user);

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        user.getRoles()
                .forEach((role) -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))
                ));

        user.getRoles().stream()
                .flatMap((role) -> role.getPermissions().stream())
                .forEach((permission) -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionEnum().name())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        String accessToken = jwtUtils.generateToken(authentication);

        return new AuthUserResponseDTO(
                user.getEmail(),
                "User created successfully",
                accessToken,
                true
        );
    }

    public AuthUserResponseDTO loginUser(AuthUserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();

        Authentication authentication = authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);

        return new AuthUserResponseDTO(
                email,
                "User login successfully",
                accessToken,
                true
        );
    }

    public Authentication authentication(String email, String password) {
        UserDetails userDetails = loadUserByUsername(email);

        if (userDetails == null)
            throw new BadCredentialsException("Invalid email or password");

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Invalid email or password");

        return new UsernamePasswordAuthenticationToken(
                email,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    public AuthUserProfileResponseDTO profileUser(String email) {
        UserEntity user = userRepository.findUserEntityByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("The user not exists")
        );

        return new AuthUserProfileResponseDTO(user.getFullNames(), user.getEmail());
    }
}
