package com.example.wcf.service;

import com.example.wcf.helper.CellPhoneHelper;
import com.example.wcf.model.CellPhone;
import com.example.wcf.repo.CellPhoneRepo;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CellPhoneService {

    private final CellPhoneRepo cellPhoneRepo;

    @Autowired
    public CellPhoneService(CellPhoneRepo cellPhoneRepo) {
        this.cellPhoneRepo = cellPhoneRepo;
    }

    public CellPhone updateCellPhone(@NotNull Integer cellPhoneId, @NotNull CellPhone cellPhone) {
        final CellPhone existingCellPhone = findExistingCellPhone(cellPhoneId, cellPhone);
        if (CellPhoneHelper.mergeCellPhone(existingCellPhone, cellPhone)) {
            cellPhoneRepo.save(existingCellPhone);
        }
        return existingCellPhone;
    }

    private CellPhone findExistingCellPhone(Integer cellPhoneId, CellPhone cellPhone) {
        if (cellPhoneId == null) {
            throw new IllegalArgumentException("Can not update CellPhone with null id.");
        }
        final Optional<CellPhone> existingCellPhone = cellPhoneRepo.findById(cellPhoneId);
        if (existingCellPhone.isEmpty()) {
            throw new NoSuchElementException("Can not update CellPhone, id not found.");
        }
        return existingCellPhone.get();
    }
}
