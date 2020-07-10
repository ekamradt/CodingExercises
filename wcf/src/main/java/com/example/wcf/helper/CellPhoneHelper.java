package com.example.wcf.helper;

import com.example.wcf.model.CellPhone;

public class CellPhoneHelper {

    public static boolean mergeCellPhone(CellPhone existingCellPhone, CellPhone updatedCellPhone) {
        final MergeHelper helper = new MergeHelper();
        existingCellPhone.setEmployeeId(helper.update(existingCellPhone.getEmployeeId(), updatedCellPhone.getEmployeeId()))
                .setEmployeeName(helper.update(existingCellPhone.getEmployeeName(), updatedCellPhone.getEmployeeName()))
                .setModel(helper.update(existingCellPhone.getModel(), updatedCellPhone.getModel()))
                .setPurchaseDate(helper.update(existingCellPhone.getPurchaseDate(), updatedCellPhone.getPurchaseDate()));
        return helper.isChanged();
    }
}
