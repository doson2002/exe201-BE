package com.exe201.exe201be.services;

import com.example.swp.components.JwtTokenUtils;
import com.example.swp.dtos.ChangePasswordDTO;
import com.example.swp.dtos.DataMailDTO;
import com.example.swp.dtos.UserDTO;
import com.example.swp.entities.*;
import com.example.swp.exceptions.DataNotFoundException;
import com.example.swp.repositories.*;
import com.example.swp.responses.UserResponse;
import com.example.swp.services.sendmails.IMailService;
import com.example.swp.utils.Const;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final IMailService mailService;
    private final CounterRepository counterRepository;
    private final TokenRepository tokenRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    @Transactional
    public Users createUser(UserDTO userDTO) throws Exception {
        //register user
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("email existed");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Fail Role"));

        Long counterId = userDTO.getCounterId();

        Counters counter = null;
        if (counterId != null) {
            counter = counterRepository.findById(counterId)
                    .orElseThrow(() -> new DataNotFoundException("Counter not found with ID: " + counterId));
        }



//        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
//            throw new Exception("Không được phép đăng ký tài khoản Admin");
//        }
//        Users currentUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String randomPassword = generateRandomPassword(6,9);

        Users newUser = Users.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .active(true)
                .firstLogin(true)
                .role(role)
                .counter(counter)
                .build();
        String encodedPassword = passwordEncoder.encode(randomPassword);
        newUser.setPassword(encodedPassword);
        if (create(userDTO.getFullName(), userDTO.getEmail(), randomPassword))
            return userRepository.save(newUser);
        else
            return null;
    }

    @Override
    public Boolean create(String name, String email, String password) {
        try {
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(email);
            dataMail.setSubject(Const.SEND_MAIL_SUBJECT.USER_REGISTER);
            Map<String, Object> props = new HashMap<>();
            props.put("name", name);
            props.put("email", email);
            props.put("password", password);
            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.USER_REGISTER);
            return true;
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<UserResponse> getAllUsers(String keyword, PageRequest pageRequest) {
        Page<Users> usersPage;
        usersPage = userRepository.searchUsers(keyword, pageRequest);
        return usersPage.map(UserResponse::fromUser);
    }

    public Users getUser(Long id) throws DataNotFoundException {
        return userRepository.findById(id).orElseThrow(()->new DataNotFoundException("User not found"));
    }
    public List<Users> getUserByRoleAndCounter(Long roleId, Long counterId) throws DataNotFoundException {
        if (!roleRepository.existsById(roleId)) {
            throw new DataNotFoundException("Role not found with id " + roleId);
        }
        if (counterId!=null && !counterRepository.existsById(counterId)) {
            throw new DataNotFoundException("Counter not found with id " + counterId);
        }
        return userRepository.findByRoleIdAndCounterId(roleId, counterId);
    }

    @Override
    public Users updateUser(long id, UserDTO userDTO) throws Exception {
        Users existingUser = userRepository.findUserById(id);
        if (existingUser == null) {
            throw new DataNotFoundException("User not found with id:" + id);
        }
        Counters existingCounter = null;
        if ((!userDTO.getRoleId().equals(1L)) &&(!userDTO.getRoleId().equals(2L)) ) {
            existingCounter = counterRepository.findById(userDTO.getCounterId())
                    .orElseThrow(() -> new DataNotFoundException("Counter not found with id:" + userDTO.getCounterId()));
        }
        Role existingRole = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("Role not found with:"+ userDTO.getRoleId()));

            existingUser.setFullName(userDTO.getFullName());
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setCounter(existingCounter);
            existingUser.setRole(existingRole);

            return userRepository.save(existingUser);

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        List<Token> tokens = tokenRepository.findByUser_Id(userId);
        List<Orders> orders = orderRepository.findByUser_Id(userId);
        orderDetailRepository.deleteByOrderIn(orders);
        tokenRepository.deleteAll(tokens);
        orderRepository.deleteAll(orders);
        optionalUser.ifPresent(userRepository::delete);
    }
    @Override
    @Transactional
    public void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        existingUser.setActive(active);
        userRepository.save(existingUser);
    }


    public String login(String email, String password, Long roleId) throws Exception {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid user account / password");
        }
        Users existingUser = optionalUser.get();
        //check password
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new DataNotFoundException("INVALID EMAIL OR PASSWORD");

        }
        if (!optionalUser.get().isActive()) {
            throw new DataNotFoundException("YOUR ACCOUNT HAS BEEN BLOCKED");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password,
                existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser);
    }
    @Override
    public Users getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new DataNotFoundException("Token is expired");
        }
        String email = jwtTokenUtils.extractEmail(token);
        Optional<Users> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }
    @Override
    public void updatePassword(String email, String password) throws DataNotFoundException {
        Users existingUser = userRepository.findByEmail(email)
                        .orElseThrow(()->new DataNotFoundException("User not found with email: "+ email));
        userRepository.updatePassword(email, password);
        existingUser.setFirstLogin(false);
        userRepository.save(existingUser);


    }

    @Override
    public Users changePassword (Long id, ChangePasswordDTO changePasswordDTO) throws DataNotFoundException {
        Users existingUser = getUser(id);
        if (existingUser != null) {
            if (!passwordEncoder.matches(changePasswordDTO.oldPassword(), existingUser.getPassword())) {
                throw new DataNotFoundException("The old password is incorrect!!");
            }
            if (!changePasswordDTO.password().equals(changePasswordDTO.retypePassword())) {
                throw new DataNotFoundException("New password and retype password do not match!!!");
            }
            String newPasswordEncode = passwordEncoder.encode(changePasswordDTO.password());
            existingUser.setPassword(newPasswordEncode);
            existingUser.setFirstLogin(false);
            return userRepository.save(existingUser);
        }
        return null;
    }
    @Override
    public String generateRandomPassword(int minLen, int maxLen) {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*";
        String dictionary = letters + letters.toUpperCase() + numbers + symbols;

        SecureRandom random = new SecureRandom();
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(dictionary.charAt(random.nextInt(dictionary.length())));
        }
        // Đảm bảo mật khẩu đáp ứng tiêu chuẩn bằng cách thêm ít nhất một ký tự của mỗi loại
        password.setCharAt(random.nextInt(length), letters.charAt(random.nextInt(letters.length())));
        password.setCharAt(random.nextInt(length), letters.toUpperCase().charAt(random.nextInt(letters.length())));
        password.setCharAt(random.nextInt(length), numbers.charAt(random.nextInt(numbers.length())));
        password.setCharAt(random.nextInt(length), symbols.charAt(random.nextInt(symbols.length())));
        return password.toString();
    }

}
