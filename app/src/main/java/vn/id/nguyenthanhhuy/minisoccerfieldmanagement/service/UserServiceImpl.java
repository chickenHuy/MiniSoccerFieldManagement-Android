package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.service;

import android.content.Context;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.IUserDAO;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO.UserDAOImpl;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;
import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.utils.PasswordUtils;

public class UserServiceImpl implements IUserService{
    IUserDAO userDAO;
    Context context;
    public UserServiceImpl(Context context) {
        userDAO = new UserDAOImpl(context);
        this.context = context;
    }

    @Override
    public boolean add(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        return userDAO.add(user);
    }

    @Override
    public boolean update(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
        return userDAO.update(user);
    }

    @Override
    public boolean update_info(User user) {
        return userDAO.update_info(user);
    }

    @Override
    public boolean softDelete(String id) {
        return userDAO.softDelete(id);
    }

    @Override
    public User findById(String id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> findUser(String searchparam) {
        return userDAO.findUser(searchparam);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User verifyLoginData(String username, String password) {
        // Mã hóa mật khẩu người dùng nhập vào
        String hashedPassword = PasswordUtils.hashPassword(password);
        // Gọi hàm verifyLoginData trong DAO để so sánh chuỗi mã hóa
        return userDAO.verifyLoginData(username, hashedPassword);
    }

    @Override
    public User verifyLoginWithHashedPassword(String username, String hashedPassword) {
        return userDAO.verifyLoginData(username, hashedPassword);
    }

    @Override
    public boolean changeRole(String id, String role) {
        return userDAO.changeRole(id, role);
    }

    @Override
    public boolean changePassword(String id, String newPassword) {
        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        return userDAO.changePassword(id, hashedPassword);
    }
}
