package com.cg.ibs.cardmanagement.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.cg.ibs.cardmanagement.bean.AccountBean;
import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.CustomerBean;
import com.cg.ibs.cardmanagement.bean.DebitCardBean;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.exceptionhandling.IBSException;
import com.cg.ibs.cardmanagement.ui.CardManagementUI;

public class CardManagementDaoImpl implements CustomerDao, BankDao {
	static Logger log = Logger.getLogger(CardManagementUI.class.getName());

	CaseIdBean caseIdObj = new CaseIdBean();
	DebitCardBean bean = new DebitCardBean();
	CreditCardBean bean1 = new CreditCardBean();
	CustomerBean bean2 = new CustomerBean();
	AccountBean bean3 = new AccountBean();

	@Override
	public void newDebitCard(CaseIdBean caseIdObj, BigInteger accountNumber) {

		String sql = SqlQueries.APPLY_NEW_DEBIT_CARD;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, caseIdObj.getCaseIdTotal());
			preparedStatement.setDate(2, java.sql.Date.valueOf(caseIdObj.getCaseTimeStamp().toLocalDate()));
			preparedStatement.setString(3, caseIdObj.getStatusOfQuery());
			preparedStatement.setBigDecimal(4, new BigDecimal(caseIdObj.getAccountNumber()));
			preparedStatement.setBigDecimal(5, new BigDecimal(caseIdObj.getUCI()));
			preparedStatement.setString(6, caseIdObj.getDefineQuery());

			preparedStatement.setString(7, caseIdObj.getCustomerReferenceId());
			preparedStatement.executeUpdate();

		} catch (SQLException | IOException e) {
			log.error(Arrays.toString(e.getStackTrace()));

		}

	}

	@Override
	public List<CaseIdBean> viewAllQueries() {

		String sql = SqlQueries.SELECT_DATA_FROM_QUERY_TABLE;
		List<CaseIdBean> query = new ArrayList();
		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {

					CaseIdBean caseIdObj = new CaseIdBean();
					Timestamp timestamp = resultSet.getTimestamp("case_timestamp");
					LocalDateTime localDateTime = timestamp.toLocalDateTime();

					caseIdObj.setCaseIdTotal(resultSet.getString("query_id"));
					caseIdObj.setCaseTimeStamp(localDateTime);
					caseIdObj.setStatusOfQuery(resultSet.getString("status_of_query"));
					caseIdObj.setAccountNumber(resultSet.getBigDecimal("account_num").toBigInteger());
					caseIdObj.setUCI(resultSet.getBigDecimal("UCI").toBigInteger());
					caseIdObj.setDefineQuery(resultSet.getString("define_query"));
					caseIdObj.setCardNumber(resultSet.getBigDecimal("card_num").toBigInteger());
					caseIdObj.setCustomerReferenceId(resultSet.getString("customer_reference_ID"));

					query.add(caseIdObj);

				}

			} catch (Exception e) {
				log.error(Arrays.toString(e.getStackTrace()));

			}
		} catch (Exception e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return query;

	}

	@Override
	public List<DebitCardBean> viewAllDebitCards() {
		String sql = SqlQueries.SELECT_DATA_FROM_DEBIT_CARD;
		List<DebitCardBean> debitCards = new ArrayList();
		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {

					DebitCardBean deb = new DebitCardBean();

					deb.setDebitCardNumber(resultSet.getBigDecimal("debit_card_number").toBigInteger());
					deb.setNameOnDebitCard(resultSet.getString("name_on_deb_card"));
					deb.setDebitCvvNum(resultSet.getInt("debit_cvv_num"));
					deb.setDebitDateOfExpiry(resultSet.getDate("debit_expiry_date").toLocalDate());
					deb.setDebitCardType(resultSet.getString("debit_card_type"));

					debitCards.add(deb);

				}

			} catch (Exception e) {
				log.error(Arrays.toString(e.getStackTrace()));

			}
		} catch (Exception e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return debitCards;

	}

	public List<CreditCardBean> viewAllCreditCards() {
		String sql = SqlQueries.SELECT_DATA_FROM_CREDIT_CARD;
		List<CreditCardBean> creditCards = new ArrayList();
		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {

					CreditCardBean crd = new CreditCardBean();

					crd.setCreditCardNumber(resultSet.getBigDecimal("credit_card_num").toBigInteger());
					crd.setCreditCardStatus(resultSet.getString("credit_card_status"));
					crd.setNameOnCreditCard(resultSet.getString("name_on_cred_card"));
					crd.setCreditCvvNum(resultSet.getInt("credit_cvv_num"));
					crd.setCreditDateOfExpiry(resultSet.getDate("credit_expiry_date").toLocalDate());
					crd.setCreditCardType(resultSet.getString("credit_card_type"));

					creditCards.add(crd);

				}

			} catch (Exception e) {
				log.error(Arrays.toString(e.getStackTrace()));

			}
		} catch (Exception e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return creditCards;

	}

	@Override
	public List<CreditCardTransaction> getCreditTrans(int days, BigInteger creditCardNumber) {

		List<CreditCardTransaction> creditCardsList = new ArrayList();

		CreditCardTransaction credTran = new CreditCardTransaction();
		try (Connection connection = ConnectionProvider.getInstance().getConnection();

				PreparedStatement preparedStatement = connection
						.prepareStatement(SqlQueries.SELECT_DATA_FROM_CREDIT_TRANSACTION);) {
			LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
			LocalDateTime currentDate = LocalDateTime.now();
			java.sql.Date fromsDate = java.sql.Date.valueOf(fromDate.toLocalDate());
			java.sql.Date tosDate = java.sql.Date.valueOf(currentDate.toLocalDate());
			log.debug("From Date: " + fromsDate);
			log.debug("To Date: " + tosDate);
			preparedStatement.setDate(1, fromsDate);
			preparedStatement.setDate(2, tosDate);
			preparedStatement.setBigDecimal(3, new BigDecimal(creditCardNumber));

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {

					credTran.setCreditCardNumber(resultSet.getBigDecimal("Credit_Card_Num").toBigInteger());
					System.out.println(resultSet.getBigDecimal("Credit_Card_Num").toBigInteger());
					credTran.setAmount(resultSet.getBigDecimal("amount"));
					credTran.setTransactionid(resultSet.getString("credit_Trans_Id"));
					credTran.setDescription(resultSet.getString("description"));

					credTran.setDate(resultSet.getTimestamp("Date_Of_trans").toLocalDateTime());
					credTran.setUCI(resultSet.getBigDecimal("UCI").toBigInteger());

					creditCardsList.add(credTran);

				}

			} catch (Exception e) {
				log.error(e.getMessage());
				log.error(Arrays.toString(e.getStackTrace()));
			}
		} catch (Exception e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}

		return creditCardsList;
	}

	@Override
	public boolean verifyQueryId(String queryId) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_QUERY_ID);) {
			preparedStatement.setString(1, queryId);

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public void setQueryStatus(String queryId, String newStatus) {
	}
	@Override
	public boolean actionANDC(String queryId, DebitCardBean bean) throws SQLException, IOException {
		BigInteger accNum = null;
		boolean result = false;
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_DEBIT_CARD_NEW)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					accNum = resultSet.getBigDecimal("account_number").toBigInteger();
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection.prepareStatement(SqlQueries.ACTION_DEBIT_CARD_NEW)) {
			preparedStatement2.setBigDecimal(1, new BigDecimal(accNum));
			preparedStatement2.setBigDecimal(2, new BigDecimal(bean.getDebitCardNumber()));
			preparedStatement2.setString(3, bean.getDebitCardStatus());
			preparedStatement2.setString(4, bean.getNameOnDebitCard());
			preparedStatement2.setInt(5, bean.getDebitCvvNum());
			preparedStatement2.setInt(6, bean.getDebitCurrentPin());
			preparedStatement2.setDate(7, Date.valueOf(bean.getDebitDateOfExpiry()));
			preparedStatement2.setBigDecimal(8, new BigDecimal(bean.getUCI()));
			preparedStatement2.setString(9, bean.getDebitCardType());
			if (preparedStatement2.executeUpdate() > 0) {
				result = true;
			}

		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return result;
	}

	@Override
	public boolean verifyUCI(BigInteger uci) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_UCI);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(uci));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {
					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public String getNewType(String queryId) throws SQLException, IOException {
		String type = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_CREDIT_CARD_TYPE)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					type = resultSet.getString("define_query");
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return type;
	}

	public BigInteger getNewUCI(String queryId) throws SQLException, IOException {
		BigInteger uci = null;
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_CREDIT_CARD_UCI)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					uci = resultSet.getBigDecimal("uci").toBigInteger();
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return uci;
	}

	@Override
	public String getNewName(BigInteger uci) throws SQLException, IOException {
		String firstname = "";
		String lastname = "";
		String name = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_CREDIT_CARD_NAME)) {
			preparedStatement.setBigDecimal(1, new BigDecimal(uci));
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					firstname = resultSet.getString("first_name");
					lastname = resultSet.getString("last_name");
					name = firstname.concat(" ").concat(lastname);
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return name;
	}

	@Override
	public boolean actionANCC(String queryId, CreditCardBean bean1) throws SQLException, IOException {
		BigInteger uci = null;
		boolean result = false;
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_CREDIT_CARD_NEW)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					uci = resultSet.getBigDecimal("UCI").toBigInteger();
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection.prepareStatement(SqlQueries.ACTION_CREDIT_CARD_NEW)) {
			preparedStatement2.setBigDecimal(1, new BigDecimal(bean1.getCreditCardNumber()));
			preparedStatement2.setString(2, bean1.getCreditCardStatus());
			preparedStatement2.setString(3, bean1.getNameOnCreditCard());
			preparedStatement2.setInt(4, bean1.getCreditCvvNum());
			preparedStatement2.setInt(5, bean1.getCreditCurrentPin());
			preparedStatement2.setDate(6, Date.valueOf(bean1.getCreditDateOfExpiry()));
			preparedStatement2.setBigDecimal(7, new BigDecimal(uci));
			preparedStatement2.setString(8, bean1.getCreditCardType());
			preparedStatement2.setInt(9, bean1.getCreditScore());
			preparedStatement2.setBigDecimal(10, bean1.getCreditLimit());
			preparedStatement2.setDouble(11, bean1.getIncome());
			if (preparedStatement2.executeUpdate() > 0) {
				result = true;
			}

		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		return result;
	}

	@Override
	public void actionBlockDC(String queryId, String status) throws SQLException, IOException {
		BigInteger debitCardNum = null;
		String debitCardStatus = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_CARD_BLOCK)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					debitCardNum = resultSet.getBigDecimal("card_num").toBigInteger();
					debitCardStatus = resultSet.getString("define_query");
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection.prepareStatement(SqlQueries.ACTION_DEBIT_CARD_BLOCK)) {
			preparedStatement2.setString(1, debitCardStatus);
			preparedStatement2.setBigDecimal(2, new BigDecimal(debitCardNum));
			preparedStatement2.executeUpdate();
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public void actionBlockCC(String queryId, String status) throws SQLException, IOException {
		BigInteger creditCardNum = null;
		String creditCardStatus = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_CARD_BLOCK)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					creditCardNum = resultSet.getBigDecimal("card_num").toBigInteger();
					creditCardStatus = resultSet.getString("define_query");
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection.prepareStatement(SqlQueries.ACTION_CREDIT_CARD_BLOCK)) {
			preparedStatement2.setString(1, creditCardStatus);
			preparedStatement2.setBigDecimal(2, new BigDecimal(creditCardNum));
			preparedStatement2.executeUpdate();
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public void actionUpgradeDC(String queryId) throws SQLException, IOException {
		BigInteger debitCardNum = null;
		String debitCardType = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_CARD_UPGRADE)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					debitCardNum = resultSet.getBigDecimal("card_num").toBigInteger();
					debitCardType = resultSet.getString("define_query");
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection.prepareStatement(SqlQueries.ACTION_DEBIT_CARD_UPGRADE)) {
			preparedStatement2.setString(1, debitCardType);
			preparedStatement2.setBigDecimal(2, new BigDecimal(debitCardNum));
			preparedStatement2.executeUpdate();
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public void actionUpgradeCC(String queryId) throws SQLException, IOException {
		BigInteger creditCardNum = null;
		String creditCardType = "";
		Connection connection = ConnectionProvider.getInstance().getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.GET_DETAILS_CARD_UPGRADE)) {
			preparedStatement.setString(1, queryId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					creditCardNum = resultSet.getBigDecimal("card_num").toBigInteger();
					creditCardType = resultSet.getString("define_query");
				}
			}
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
		try (PreparedStatement preparedStatement2 = connection
				.prepareStatement(SqlQueries.ACTION_CREDIT_CARD_UPGRADE)) {
			preparedStatement2.setString(1, creditCardType);
			preparedStatement2.setBigDecimal(2, new BigDecimal(creditCardNum));
			preparedStatement2.executeUpdate();
		} catch (SQLException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public void newCreditCard(CaseIdBean caseIdObjId) {
		// TODO Auto-generated method stub

	}

	@Override
	public BigInteger getAccountNumber(BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getdebitCardType(BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestDebitCardUpgrade(CaseIdBean caseIdObj, BigInteger debitCardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestDebitCardLost(CaseIdBean caseIdObj, BigInteger debitCardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestCreditCardLost(CaseIdBean caseIdObj, BigInteger creditCardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyAccountNumber(BigInteger accountNumber) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(SqlQueries.VERIFY_ACCOUNT_NUM_FROM_ACCOUNT);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(accountNumber));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public boolean verifyDebitCardNumber(BigInteger debitCardNumber) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_DEBIT_CARD_NUM);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(debitCardNumber));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public boolean verifyCreditCardNumber(BigInteger creditCardNumber) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_CREDIT_CARD_NUM);) {
			preparedStatement.setBigDecimal(1, new BigDecimal(creditCardNumber));

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public void setNewDebitPin(BigInteger debitCardNumber, int newPin) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDebitCardPin(BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNewCreditPin(BigInteger creditCardNumber, int newPin) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCreditCardPin(BigInteger creditCardNumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigInteger getUci() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getDebitUci(BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getCreditUci(BigInteger creditCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getcreditCardType(BigInteger creditCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void raiseDebitMismatchTicket(CaseIdBean caseIdObj, String transactionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void raiseCreditMismatchTicket(CaseIdBean caseIdObj, String transactionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DebitCardTransaction> getDebitTrans(int dys, BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestCreditCardUpgrade(CaseIdBean caseIdObj, BigInteger creditCardNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyDebitTransactionId(String transactionId) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_DEBIT_TRANS_ID);) {
			preparedStatement.setString(1, transactionId);

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public boolean verifyCreditTransactionId(String transactionId) {
		boolean result = false;

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.VERIFY_CREDIT_TRANS_ID);) {
			preparedStatement.setString(1, transactionId);

			try (ResultSet resultSet = preparedStatement.executeQuery();) {

				if (resultSet.next()) {

					result = true;
				}

			}
		} catch (Exception e) {

			log.error(Arrays.toString(e.getStackTrace()));

		}

		return result;
	}

	@Override
	public String getCustomerReferenceId(CaseIdBean caseIdObj, String customerReferenceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDebitCardStatus(BigInteger debitCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreditCardStatus(BigInteger creditCardNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getDebitCardNumber(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getDMUci(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getDMAccountNumber(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getCMUci(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getNDCUci(BigInteger accountNumber) {
		// TODO Auto-generated method stub
		return null;
	}



}
