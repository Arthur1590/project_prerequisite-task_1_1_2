package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Arthur", "Fazilyanov", (byte) 24);
        System.out.println("Пользователь с именем - Arthur добавлен в базу данных");

        userService.saveUser("Fazilyanov", "ar", (byte) 25);
        System.out.println("Пользователь с именем - Fazilyanov добавлен в базу данных");

        userService.saveUser("ar", "Fazilyanovdfff", (byte) 30);
        System.out.println("Пользователь с именем - ar добавлен в базу данных");

        userService.saveUser("qwe", "rty", (byte) 22);
        System.out.println("Пользователь с именем - qwe добавлен в базу данных");

        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

//        userService.cleanUsersTable();
//        userService.dropUsersTable();
    }
}