package com.cg.ibs.cardmanagement.dao;

public class SqlQueries {

	public static final String SELECT_DATA_FROM_CREDIT_CARD = "select credit_card_num, credit_card_status, name_on_cred_card, credit_cvv_num, credit_expiry_date, credit_card_type from credit_card";
	public static final String SELECT_DATA_FROM_DEBIT_CARD = "select debit_card_number, debit_card_status, name_on_deb_card, debit_cvv_num, debit_expiry_date, debit_card_type from debit_card";
	public static final String APPLY_NEW_DEBIT_CARD = "insert into query_log(query_ID,case_TimeStamp,status_of_query,account_num,UCI,define_query,customer_reference_ID)values(?,?,?,?,?,?,?)";
	public static final String SELECT_DATA_FROM_CREDIT_TRANSACTION = "select credit_trans_Id,UCI,Credit_Card_Num,Date_Of_Trans,amount,description from Credit_Card_trans WHERE ( Date_Of_Trans between ? And ? ) and credit_card_num=?";
	public static final String SELECT_DATA_FROM_QUERY_TABLE = " Select * from query_log where status_of_query='In Process' or status_of_query='Pending'";
	public static final String VERIFY_ACCOUNT_NUM_FROM_ACCOUNT = "Select account_number from accounts where account_number =?";
	public static final String VERIFY_DEBIT_CARD_NUM = "Select debit_card_number from debit_card where debit_card_number =?";
	public static final String VERIFY_CREDIT_CARD_NUM = "Select credit_card_number from credit_card where credit_card_number =?";
	public static final String VERIFY_DEBIT_TRANS_ID = "Select Debit_Trans_Id from Debit_Card_trans where Debit_Trans_Id=?";
	public static final String VERIFY_CREDIT_TRANS_ID = "Select Credit_Trans_Id from Credit_Card_trans where Credit_Trans_Id=?";
	public static final String VERIFY_QUERY_ID = "Select query_id from Query_Log where query_id =?";
	public static final String ACTION_CREDIT_CARD_UPGRADE = "update Credit_Card SET credit_card_type=?  WHERE credit_card_num =?";
	public static final String ACTION_DEBIT_CARD_UPGRADE = "update Debit_Card SET debit_card_type =? WHERE debit_card_num =?";
	public static final String GET_DETAILS_CARD_UPGRADE = "SELECT card_num,define_query FROM query_log WHERE query_id =?";
	public static final String GET_DETAILS_CARD_BLOCK = "SELECT card_num,define_query FROM query_log WHERE query_id =?";
	public static final String ACTION_CREDIT_CARD_BLOCK = "update Credit_Card SET credit_card_status=?  WHERE credit_card_num =?";
	public static final String ACTION_DEBIT_CARD_BLOCK = "update Debit_Card SET debit_card_status=?  WHERE debtit_card_num =?";
	public static final String VERIFY_UCI = "Select uci from customers where Uci=?";
	public static final String GET_CREDIT_CARD_TYPE = "Select define_query FROM query_log WHERE query_ID=?";
	public static final String GET_CREDIT_CARD_UCI = "Select uci FROM query_log WHERE query_ID=?";
	public static final String GET_CREDIT_CARD_NAME = "Select first_name,last_name FROM customers where uci=?";
	public static final String GET_DETAILS_CREDIT_CARD_NEW = "SELECT uci FROM query_log WHERE query_id=?";
	public static final String ACTION_CREDIT_CARD_NEW = "INSERT INTO credit_card(credit_card_num, credit_card_status, name_on_cred_card, credit_cvv_num,credit_current_pin, credit_expiry_date, uci, credit_card_type, credit_score, credit_limit, income )VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	public static final String GET_DETAILS_DEBIT_CARD_NEW = "SELECT account_number FROM query_log WHERE query_id=? ";
	public static final String ACTION_DEBIT_CARD_NEW = "INSERT INTO debit_card(account_number,debit_card_num, debit_card_status, name_on_deb_card, debit_cvv_num,debit_current_pin, debit_expiry_date, uci, debit_card_type)VALUES(?,?,?,?,?,?,?,?,?)";

}