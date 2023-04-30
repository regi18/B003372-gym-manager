package dao;

import models.membership.Membership;

public interface MembershipDAO {
    Membership getOfCustomer(String fiscalCode);

    void insertOfCustomer(String fiscalCode, Membership membership);

    void updateOfCustomer(String fiscalCode, Membership membership);

    void deleteOfCustomer(String fiscalCode);
}
