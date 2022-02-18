package com.quicktutorials.learnmicroservices.AccountMicroservice.services;

import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.AccountDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.OperationDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.daos.UserDao;
import com.quicktutorials.learnmicroservices.AccountMicroservice.entities.Account;
import com.quicktutorials.learnmicroservices.AccountMicroservice.entities.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

// L'annotation @Service e altre vanno inserite sopra le classi implementate e non interfacce.
@Service @Transactional
public class OperationServiceImpl implements OperationService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    OperationDao operationDao;

    @Override
    public List<Operation> getAllOperationPerAccount(String accountId){
        return operationDao.findAllOperationsByAccount((accountId));
    }

    @Override
    public List<Account> getAllAccountPerUser(String userId){
        return accountDao.getAllAccountsPerUser(userId);
    }

    @Override
    public Operation saveOperation(Operation operation){
        if(operation.getDate() == null) {
            operation.setDate(new Date());
        }
        return operationDao.save(operation);
    }
}
