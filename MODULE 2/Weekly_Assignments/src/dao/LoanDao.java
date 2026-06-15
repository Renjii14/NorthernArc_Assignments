package dao;

import entity.Loan;
import java.sql.SQLException;
import java.util.Collection;

public interface LoanDao {

    int save(Loan loan) throws SQLException;

    Loan findById(int loanId) throws SQLException;

    Collection<Loan> findAll() throws SQLException;

    Collection<Loan> findByStatus(String status) throws SQLException;

    Collection<Loan> findByType(String type) throws SQLException;

    Collection<Loan> findByAmountGreaterThan(int amount) throws SQLException;

    Collection<Loan> findByInterestLessThan(double interest) throws SQLException;

    Collection<Loan> findByTypeAndStatus(String type,String status) throws SQLException;


    Collection<Loan> sortByAmount() throws SQLException;

    Collection<Loan> sortByAmountDesc() throws SQLException;

    Collection<Loan> sortByInterest() throws SQLException;

    Collection<Loan> sortByAmountAndInterest() throws SQLException;


    void updateInterestById(int loanId,double interest) throws SQLException;

    void updateLoanStatus(int loanId,String status) throws SQLException;

    void updateInterestByType(String type,double interest) throws SQLException;


    void deleteById(int loanId) throws SQLException;

    void deleteByStatus(String status) throws SQLException;

    void deleteAll() throws SQLException;


    boolean existsById(int loanId) throws SQLException;


    int countLoans() throws SQLException;

    int countByStatus(String status) throws SQLException;

    int getMaxLoanAmount() throws SQLException;

    int getMinLoanAmount() throws SQLException;

    double getAverageLoanAmount() throws SQLException;

    int getTotalLoanAmount() throws SQLException;

    void groupByStatus() throws SQLException;

    void groupByType() throws SQLException;


    void groupByTypeHavingCountGreaterThan(int count) throws SQLException;


    Collection<Loan> topNLoans(int n) throws SQLException;


}
