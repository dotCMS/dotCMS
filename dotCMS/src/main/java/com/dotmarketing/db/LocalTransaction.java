package com.dotmarketing.db;


import java.sql.Connection;

import com.dotcms.util.ReturnableDelegate;
import com.dotcms.util.VoidDelegate;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;

import io.vavr.control.Try;

public class LocalTransaction {

  private static enum TransactionErrorEnum {
    NOTHING,
    LOG,
    THROW;
  }


    private final static String WARN_MESSAGE = "Connection that started the transaction is not the same as the one that is committing. Commit process will be executed by the outer transaction";
  
    /**
     *
     * @param delegate {@link ReturnableDelegate}
     * @return T result of the {@link ReturnableDelegate}
     * @throws DotDataException
     *
     * This class can be used to wrap methods in a "local transaction" pattern including the listeners (commit and rollback listeners)
     * this pattern will check to see if the method is being called in an existing transaction.
     * if it is being called in a transaction, it will do nothing.  If it is not being called in a transaction
     * it will checkout a db connection,start a transaction, do the work, commit the transaction, return the result
     * and  finally close the db connection.  If the SQL call fails, it will rollback the work, close  the db connection
     * and throw the error up the stack.
     *
     * In addition the connection will be closed, only if the current transaction is opening it.
     *
     *  How to use:
     *
     *	return new LocalTransaction().wrapReturnWithListeners(() ->{
     *		return myDBMethod(args);
     *  });
     */
    static public <T> T wrapReturnWithListeners(final ReturnableDelegate<T> delegate) throws Exception {

        final boolean isNewConnection    = !DbConnectionFactory.connectionExists();
        final boolean isLocalTransaction = HibernateUtil.startLocalTransactionIfNeeded();

        T result = null;

        try {
            final StackTraceElement[] threadStack = Thread.currentThread().getStackTrace();
            final Connection conn = DbConnectionFactory.getConnection();
            result= delegate.execute();

            //commits a local transaction only when it is not interrupting others transactions
            if (isLocalTransaction && handleTransactionInterruption(conn, threadStack)) {
                HibernateUtil.commitTransaction();
            }
        } catch (Throwable e) {

            if (isLocalTransaction) {
                HibernateUtil.rollbackTransaction();
            }

            throwException(e);
        } finally {
            
            if (isLocalTransaction && isNewConnection) {
                HibernateUtil.closeSessionSilently();
            }
            
        }

        return result;
    } // wrapReturn.


    /**
     *
     * @param delegate {@link ReturnableDelegate}
     * @return T result of the {@link ReturnableDelegate}
     * @throws DotDataException
     *
     * This class can be used to wrap methods in a "local transaction" pattern
     * this pattern will check to see if the method is being called in an existing transaction.
     * if it is being called in a transaction, it will do nothing.  If it is not being called in a transaction
     * it will checkout a db connection,start a transaction, do the work, commit the transaction, return the result
     * and  finally close the db connection.  If the SQL call fails, it will rollback the work, close  the db connection
     * and throw the error up the stack.
     *
     *  How to use:
     *
     *	return new LocalTransaction().wrapReturn(() ->{
     *		return myDBMethod(args);
     *  });
     */
    static public <T> T wrapReturn(final ReturnableDelegate<T> delegate) throws Exception {

        final boolean isNewConnection    = !DbConnectionFactory.connectionExists();
        final boolean isLocalTransaction = DbConnectionFactory.startTransactionIfNeeded();

        T result = null;

        try {
            final StackTraceElement[] threadStack = Thread.currentThread().getStackTrace();
            final Connection conn = DbConnectionFactory.getConnection();
            result= delegate.execute();
            //commits a local transaction only when it is not interrupting others transactions
            if (isLocalTransaction && handleTransactionInterruption(conn,threadStack)) {
              DbConnectionFactory.commit();
            }
        } catch (Throwable e) {

            handleException(isLocalTransaction, e);
        } finally {
            
            if (isLocalTransaction) {
                DbConnectionFactory.setAutoCommit(true);
                if (isNewConnection) {
                    DbConnectionFactory.closeConnection();
                }
            }
        }

        return result;
    } // wrapReturn.

    /**
     *
     * @param delegate {@link VoidDelegate}
     * @throws DotDataException
     *
     * this will accept a method that does not need to return anything and wrap it in a transaction
     * if it is not in one already.  At the end of the call, it will
     * return the db connection to the connection pool
     *
     * 	 *  How to use:
     *
     *	 new LocalTransaction().wrap(() ->{
     *		 myDBMethod(args);
     *      return null;
     *  });
     */
    static public void wrap(final VoidDelegate delegate) throws Exception {

        final boolean isNewConnection    = !DbConnectionFactory.connectionExists();
        final boolean isLocalTransaction = DbConnectionFactory.startTransactionIfNeeded();
        
        try {
            final StackTraceElement[] threadStack = Thread.currentThread().getStackTrace();
            final Connection conn = DbConnectionFactory.getConnection();
            delegate.execute();

            //commits a local transaction only when it is not interrupting others transactions
            if (isLocalTransaction && handleTransactionInterruption(conn,threadStack)) {
              DbConnectionFactory.commit();
            }
        } catch (Exception e) {

            handleException(isLocalTransaction, e);
        } finally {
            
            if (isLocalTransaction) {

                DbConnectionFactory.setAutoCommit(true);
                if (isNewConnection) {
                    DbConnectionFactory.closeConnection();
                }
            }
        }
    } // wrap.
    
    static public void wrapNoException(final VoidDelegate delegate)  {
        try {
            wrap(delegate);
        }
        catch(Exception e) {
            throw new DotRuntimeException(e);
        }
    } // wrapNoException.
    
    private static void handleException(final boolean isLocalTransaction,
                                        final Throwable  e) throws Exception {
        if(isLocalTransaction){
            DbConnectionFactory.rollbackTransaction();
        }

        throwException(e);
    } // handleException.

    
    private static boolean handleTransactionInterruption(final Connection conn, final StackTraceElement[] threadStack) throws DotDataException {
      if (DbConnectionFactory.getConnection() != conn) {
        final String action = Config.getStringProperty("LOCAL_TRANSACTION_INTERRUPTED_ACTION", TransactionErrorEnum.LOG.name());
        if (TransactionErrorEnum.LOG.name().equalsIgnoreCase(action)) {
          
          Logger.warn(LocalTransaction.class, WARN_MESSAGE);
        } else if (TransactionErrorEnum.THROW.name().equalsIgnoreCase(action)) {
          throw new DotDataException(WARN_MESSAGE);
        }
        return false;
      }

      return true;
    }
    
    
    private static void throwException ( final Throwable  e) throws Exception {

        if (e instanceof Exception) {

            throw Exception.class.cast(e);
        }

        Throwable t = e;
        while(t.getCause()!=null){
            t=t.getCause();
        }
        if(t instanceof DotDataException){
            throw (DotDataException) t;
        }
        throw new DotDataException(t.getMessage(),t);
    }

    
    
    private final static String LocalTransationName= LocalTransaction.class.getCanonicalName();
    static public boolean inLocalTransaction() {
      final StackTraceElement[] stes =  Thread.currentThread().getStackTrace();
      for(int i=2;i<stes.length;i++) {
        final int stackNumber=i;
        String steName = Try.of(()->stes[stackNumber].getClassName()).getOrNull();
        if(LocalTransationName.equals(steName)) {
          return true;
        }
      }
      return false;
    }
    
    
    
    
} // E:O:F:LocalTransaction.
