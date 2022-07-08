package recipes;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.dto.User;

@Service
public class UserDetailsServiceIml implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findUserByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return modelMapper.map(userEntity, User.class);
    }
}