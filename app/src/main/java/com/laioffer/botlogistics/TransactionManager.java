package com.laioffer.botlogistics;

import androidx.fragment.app.Fragment;

public interface TransactionManager {

    void doTransactionFragment(Fragment fragment, boolean isAnimate, boolean isAddBackStack);

    void doActivityTransaction(Class clazz, boolean isFinish);

    void doCleanBackStack();

    void doBackStack();
}
