package store;

import store.controller.ConvenienceStoreController;
import store.view.setup.FileStoreDataProvider;
import store.view.setup.StoreDataProvider;
import store.view.user.ConsoleInputView;
import store.view.user.ConsoleOutputView;
import store.view.user.InputView;
import store.view.user.OutputView;

public class Application {

    public static void main(String[] args) {
        InputView inputView = new ConsoleInputView();
        OutputView outputView = new ConsoleOutputView();

        StoreDataProvider storeDataProvider = new FileStoreDataProvider();

        ConvenienceStoreController controller = new ConvenienceStoreController(inputView, outputView, storeDataProvider);
        controller.execute();
    }

}
