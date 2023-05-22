package dao;

import domainModel.membership.Membership;

public interface MembershipDAO {

    /**
     * Returns the membership of the customer with the given fiscal code
     *
     * @param fiscalCode The fiscal code of the customer to get the membership for
     * @return The membership of the customer with the given fiscal code
     * @throws Exception when the customer doesn't exist or if there are problems to access the data source
     */
    Membership getOfCustomer(String fiscalCode) throws Exception;

    /**
     * Adds a membership for the given customer
     *
     * @param fiscalCode The fiscal code of the customer to add the membership for
     * @param membership The membership to add
     * @throws Exception when the customer doesn't exist, or if it already has a membership, or if there are problems to access the data source
     */
    void insertOfCustomer(String fiscalCode, Membership membership) throws Exception;

    /**
     * Updates the membership of the customer with the given fiscal code
     *
     * @param fiscalCode The fiscal code of the customer to update the membership for
     * @param membership The membership to update
     * @throws Exception when the customer doesn't exist, or if it doesn't have a membership, or if there are problems to access the data source
     */
    void updateOfCustomer(String fiscalCode, Membership membership) throws Exception;

    /**
     * Removes the membership of the customer with the given fiscal code
     *
     * @param fiscalCode The fiscal code of the customer to remove the membership for
     * @return true if successful, false otherwise (i.e. customer not found or customer doesn't have a membership)
     * @throws Exception if there are problems to access the data source
     */
    boolean deleteOfCustomer(String fiscalCode) throws Exception;
}
