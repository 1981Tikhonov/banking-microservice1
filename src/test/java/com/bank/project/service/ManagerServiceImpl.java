package com.bank.project.service;

import com.bank.project.entity.Manager;
import org.springframework.data.domain.Page;

public class ManagerServiceImpl {
    private Manager testManager;

    public Manager createManager(Manager testManager) {
        this.testManager = testManager;
        return null;
    }

    public Manager getManagerById(long l) {
            return null;
    }

    public Page<Manager> getAllManagers(int i, int i1, String[] strings) {
                return null;
    }

    public Manager updateManager(long l, Manager updatedManager) {
                return null;
    }

    public boolean deleteManager(long l) {
                return false;
    }
}
