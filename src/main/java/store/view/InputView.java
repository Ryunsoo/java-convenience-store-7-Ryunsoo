package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.view.dto.YN;
import store.domain.product.Product;
import store.view.dto.PurchaseInfo;

import java.util.Arrays;
import java.util.List;

public class InputView {

    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    public List<PurchaseInfo> readPurchaseInfo() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        System.out.println();

        return Arrays.asList(input.split(",")).stream()
                .map(this::parseToProductNameAndQuantity)
                .toList();
    }

    private PurchaseInfo parseToProductNameAndQuantity(String input) {
        try {
            boolean startWithFormat = input.startsWith("[");
            boolean endWithFormat = input.endsWith("]");

            if (!startWithFormat || !endWithFormat) {
                throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다.");
            }

            String[] nameAndQuantity = input
                    .substring(1, input.length() - 1)
                    .split("-");

            if (nameAndQuantity.length != 2) {
                throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다.");
            }

            String productName = nameAndQuantity[PRODUCT_NAME_INDEX];
            int quantity = Integer.parseInt(nameAndQuantity[QUANTITY_INDEX]);

            if (quantity < 1) {
                throw new IllegalArgumentException("수량은 1개 이상으로 입력하세요.");
            }

            return new PurchaseInfo(productName, quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지 않은 형식으로 입력했습니다.");
        }
    }

    public YN recheckAddOneMore(Product product) {
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", product.name());
        System.out.println();
        String input = Console.readLine();
        System.out.println();
        return YN.of(input);
    }

    public YN recheckContinueWithoutDiscount(Product product, int quantity) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", product.name(), quantity);
        System.out.println();
        String input = Console.readLine();
        System.out.println();
        return YN.of(input);
    }

    public YN recheckContinueAfterGuideOutOfPromotionPeriod(Product product) {
        System.out.printf("현재 %s은(는) 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", product.name());
        System.out.println();
        String input = Console.readLine();
        System.out.println();
        return YN.of(input);
    }

    public YN checkMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = Console.readLine();
        System.out.println();
        return YN.of(input);
    }

    public YN askStopPurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String input = Console.readLine();
        System.out.println();
        return YN.of(input);
    }
}