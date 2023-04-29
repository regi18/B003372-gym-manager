package dao;

import models.membership.Membership;

import java.sql.SQLException;

public interface MembershipDAO {
    Membership getOfCustomer(String fiscalCode) throws SQLException;

    void insertOfCustomer(String fiscalCode, Membership membership) throws SQLException;

    void updateOfCustomer(String fiscalCode, Membership membership) throws SQLException;

    void deleteOfCustomer(String fiscalCode) throws SQLException;
}
