package com.alerts.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alerts.entities.User;

public class UserRepository {
    HashMap<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUserName(), user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public HashMap<String, User> getUsersMap() {
        return users;
    }

    public void saveUsers(Map<String, User> usuariosActualizados) {
        users = new HashMap<>(usuariosActualizados);
    }

    public User getUser(String nombreUsuario) {
        return users.get(nombreUsuario);
    }
}
