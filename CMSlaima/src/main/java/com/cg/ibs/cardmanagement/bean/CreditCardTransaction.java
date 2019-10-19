package com.cg.ibs.cardmanagement.bean;

 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

 

public class CreditCardTransaction {
    
    private String transactionid;
    private BigInteger UCI;
    private BigInteger creditCardNumber;
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;
    
    
    

 

    public CreditCardTransaction() {
        super();
    }

 

    @Override
    public String toString() {
        return "CreditCardTransaction [transactionid=" + transactionid + ", UCI=" + UCI + ", creditCardNumber="
                + creditCardNumber + ", date=" + date + ", amount=" + amount + ", description=" + description + "]";
    }

 

    public  CreditCardTransaction(String transactionid, BigInteger uCI, BigInteger creditCardNumber, LocalDateTime date,
            BigDecimal amount, String description) {
        
        this.transactionid = transactionid;
        this.UCI = uCI;
        this.creditCardNumber = creditCardNumber;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
    
    public String getTransactionid() {
        return transactionid;
    }
    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }
    public BigInteger getUCI() {
        return UCI;
    }
    public void setUCI(BigInteger UCI) {
        this.UCI = UCI;
    }
    public BigInteger getCreditCardNumber() {
        return creditCardNumber;
    }
    public void setCreditCardNumber(BigInteger creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

 

   
}




//
//package com.cg.ibs.cardmanagement.bean;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.time.LocalDateTime;
//
//public class CreditCardTransaction {
//	
//	private String transactionid;
//	private BigInteger UCI;
//	private BigInteger creditCardNumber;
//	private LocalDateTime date;
//	private BigDecimal amount;
//	private String description;
//	
//
//	@Override
//	public String toString() {
//		return "CreditCardTransaction [transactionid=" + transactionid + ", UCI=" + UCI + ", creditCardNumber="
//				+ creditCardNumber + ", date=" + date + ", amount=" + amount + ", description=" + description + "]";
//	}
//
//	public  CreditCardTransaction() {
//		
//		this.transactionid = transactionid;
//		this.UCI = UCI;
//		this.creditCardNumber = creditCardNumber;
//		this.date = date;
//		this.amount = amount;
//		this.description = description;
//	}
//	
//	public String getTransactionid() {
//		return transactionid;
//	}
//	public void setTransactionid(String transactionid) {
//		this.transactionid = transactionid;
//	}
//	public BigInteger getUCI() {
//		return UCI;
//	}
//	public void setUCI(BigInteger UCI) {
//		this.UCI = UCI;
//	}
//	public BigInteger getCreditCardNumber() {
//		return creditCardNumber;
//	}
//	public void setCreditCardNumber(BigInteger creditCardNumber) {
//		this.creditCardNumber = creditCardNumber;
//	}
//	public LocalDateTime getDate() {
//		return date;
//	}
//	public void setDate(LocalDateTime date) {
//		this.date = date;
//	}
//	public BigDecimal getAmount() {
//		return amount;
//	}
//	public void setAmount(BigDecimal amount) {
//		this.amount = amount;
//	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public void setDateOfTran(LocalDateTime localDateTime) {
//		
//	}
//   
//}
