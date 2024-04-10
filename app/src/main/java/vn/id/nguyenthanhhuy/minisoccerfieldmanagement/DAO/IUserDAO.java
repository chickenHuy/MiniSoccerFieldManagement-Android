package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.DAO;

import java.util.List;

import vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model.User;

public interface IUserDAO {
    public boolean add(User user);

    public boolean update(User user);

    public boolean update_info(User user);

    public boolean softDelete(String id);

    public User findById(String id);

    public List<User> findUser(String searchparam);

    public List<User> findAll();

    public User verifyLoginData(String username, String password);

    public boolean changeRole(String id, String role);

    public boolean changePassword(String id, String newPassword);
}
