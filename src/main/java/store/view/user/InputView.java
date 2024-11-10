package store.view.user;

import store.domain.common.YN;
import store.domain.product.Product;
import store.view.dto.PurchaseInfo;

import java.util.List;

public interface InputView {
    List<PurchaseInfo> readPurchaseInfo();
    YN recheckAddOneMore(Product product);
    YN recheckContinueWithoutDiscount(Product product, int quantity);
    YN checkMembershipDiscount();
    YN askStopPurchase();
}
