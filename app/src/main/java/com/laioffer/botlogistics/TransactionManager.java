package com.laioffer.botlogistics;

import androidx.fragment.app.Fragment;

public interface TransactionManager {

    void doTransactionFragment(Fragment fragment);

    void doActivityTransaction(Class clazz, boolean isFinish);
}
