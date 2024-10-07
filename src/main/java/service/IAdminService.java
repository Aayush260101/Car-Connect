package service;

import entity.Admin;


import java.util.List;

public interface IAdminService {
    Admin getAdminById(int adminId);
    Admin getAdminByUsername(String username);
    void registerAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteAdmin(int adminId);
}
