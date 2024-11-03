package lotto.controller;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;
import java.util.stream.IntStream;
import lotto.domain.BonusNumber;
import lotto.domain.CustomerLotto;
import lotto.domain.Lotto;
import lotto.domain.WinningLotto;
import lotto.dto.GeneratedTickets;
import lotto.dto.LottoResults;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoMachineController {
    public static final int TICKET_PRICE = 1000;

    private final InputView inputView;
    private final OutputView outputView;

    public LottoMachineController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        CustomerLotto customerLotto = createCustomerLotto();
        WinningLotto winningLotto = createWinningLotto();
        displayLottoResults(customerLotto, winningLotto);
    }

    private CustomerLotto createCustomerLotto() {
        List<Lotto> tickets = purchaseTickets();
        CustomerLotto customerLotto = CustomerLotto.of(tickets);
        outputView.printGeneratedTickets(GeneratedTickets.from(customerLotto));
        return customerLotto;
    }

    private WinningLotto createWinningLotto() {
        List<Integer> winningLottoNumbers = inputView.readWinningLottoNumbers();
        BonusNumber bonusNumber = readBonusNumber(winningLottoNumbers);
        return createValidWinningLotto(winningLottoNumbers, bonusNumber);
    }

    private void displayLottoResults(CustomerLotto customerLotto, WinningLotto winningLotto) {
        LottoResults lottoResults = customerLotto.compareWinningLotto(winningLotto);
        outputView.printRank(lottoResults);
    }

    private List<Lotto> purchaseTickets() {
        int price = inputView.readLottoPurchasePrice();
        return generateTickets(price / TICKET_PRICE);
    }

    private BonusNumber readBonusNumber(List<Integer> winningLottoNumbers) {
        return attemptToGetBonusNumber(winningLottoNumbers);
    }

    private WinningLotto createValidWinningLotto(List<Integer> winningLottoNumbers, BonusNumber bonusNumber) {
        try {
            return WinningLotto.of(winningLottoNumbers, bonusNumber);
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return createWinningLotto();
        }
    }

    private List<Lotto> generateTickets(int price) {
        return IntStream.range(0, price)
                .mapToObj(i -> Lotto.of(Randoms.pickUniqueNumbersInRange(1, 45, 6)))
                .toList();
    }

    private BonusNumber attemptToGetBonusNumber(List<Integer> winningLottoNumbers) {
        try {
            int bonusNumberValue = inputView.readBonusLottoNumber();
            return BonusNumber.of(bonusNumberValue, winningLottoNumbers);
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return attemptToGetBonusNumber(winningLottoNumbers);
        }
    }
}