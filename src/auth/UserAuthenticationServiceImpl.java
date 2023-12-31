package auth;

import objectpool.UserPool;
import singleton.UserManager;
import user.User;
import user.UserIdGenerator;
import user.UserType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserManager userManager;
    private final UserPool userPool;
    private final Set<String> loggedInUsers = new HashSet<>();

    public UserAuthenticationServiceImpl(UserPool userPool, UserManager userManager) {
        this.userPool = userPool;
        this.userManager = userManager;
    }

    @Override
    public void registerUser(String username, String password, boolean isAdmin) {
        if (userManager.getUsers().containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String userId = UserIdGenerator.generateUniqueUserId();
        User user = userPool.acquireUser(userId, username, password, isAdmin);
        userManager.getUsers().put(username, user);
    }

    @Override
    public User login(String username, String password) {
        User user = userManager.getUsers().get(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUsers.add(username);
            return user;
        }
        return null;
    }

    @Override
    public void logout(User user) {
        loggedInUsers.remove(user.getUsername());
    }

    public List<User> getLoggedInUsers(boolean isAdmin) {
        List<User> loggedInUserList = new ArrayList<>();
        for (String username : loggedInUsers) {
            User user = userManager.getUsers().get(username);
            if (user != null) {
                if (isAdmin && user.getUserType() == UserType.REGULAR_USER) {
                    continue;
                } else if (!isAdmin && user.getUserType() == UserType.ADMIN_USER){
                    continue;
                }
                loggedInUserList.add(user);
            }
        }
        return loggedInUserList;
    }
}
