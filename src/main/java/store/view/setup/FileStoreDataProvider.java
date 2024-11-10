package store.view.setup;

import store.exception.StoreSetUpFailException;
import store.view.dto.ProductData;
import store.view.dto.PromotionData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileStoreDataProvider implements StoreDataProvider {

    private static final String PROMOTIONS_FILE_NAME = "promotions.md";
    private static final String PRODUCTS_FILE_NAME = "products.md";

    private static final int PROMOTION_NAME_INDEX = 0;
    private static final int PROMOTION_BUY_INDEX = 1;
    private static final int PROMOTION_GET_INDEX = 2;
    private static final int PROMOTION_START_DATE_INDEX = 3;
    private static final int PROMOTION_END_DATE_INDEX = 4;

    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_QUANTITY_INDEX = 2;
    private static final int PRODUCT_PROMOTION_NAME_INDEX = 3;

    private static final String PRODUCT_PROMOTION_NONE_NAME = "null";

    private static final String DATA_DELIMITER = ",";

    private final FileLineReader fileReader;

    public FileStoreDataProvider() {
        this.fileReader = new FileLineReader();
    }

    @Override
    public List<PromotionData> providePromotionData() {
        return fileReader.readLines(PROMOTIONS_FILE_NAME)
                .stream()
                .skip(1)
                .map(this::parseToPromotionData)
                .toList();
    }

    private PromotionData parseToPromotionData(String line) {
        List<String> data = split(line);

        String name = data.get(PROMOTION_NAME_INDEX);
        int buy = parseToInt(data.get(PROMOTION_BUY_INDEX));
        int get = parseToInt(data.get(PROMOTION_GET_INDEX));
        LocalDate startDate = parseToDate(data.get(PROMOTION_START_DATE_INDEX));
        LocalDate endDate = parseToDate(data.get(PROMOTION_END_DATE_INDEX));

        return new PromotionData(name, buy, get, startDate, endDate);
    }

    @Override
    public List<ProductData> provideProductData() {
        return fileReader.readLines(PRODUCTS_FILE_NAME)
                .stream()
                .skip(1)
                .map(this::parseToProductData)
                .toList();
    }

    private ProductData parseToProductData(String line) {
        List<String> data = split(line);

        String name = data.get(PRODUCT_NAME_INDEX);
        long price = parseToLong(data.get(PRODUCT_PRICE_INDEX));
        int quantity = parseToInt(data.get(PRODUCT_QUANTITY_INDEX));
        Optional<String> promotionName = getPromotionNameInProductData(data);

        return new ProductData(name, price, quantity, promotionName);
    }

    private Optional<String> getPromotionNameInProductData(List<String> productData) {
        String promotionName = productData.get(PRODUCT_PROMOTION_NAME_INDEX);
        if (promotionName.equals(PRODUCT_PROMOTION_NONE_NAME)) {
            return Optional.empty();
        }
        return Optional.of(promotionName);
    }

    private List<String> split(String line) {
        return Arrays.asList(line.split(DATA_DELIMITER));
    }

    private int parseToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new StoreSetUpFailException("수량 정보가 유효하지 않습니다.");
        }
    }

    private long parseToLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            throw new StoreSetUpFailException("가격 정보가 유효하지 않습니다.");
        }
    }

    private LocalDate parseToDate(String string) {
        try {
            return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new StoreSetUpFailException("닐짜 정보가 유효하지 않습니다.");
        }
    }

}
