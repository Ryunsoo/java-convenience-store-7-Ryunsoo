# 🛒 편의점
> 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템

## 👉 재고 관리
1. 각 상품의 **재고 수량**에 따라 결제 가능 여부를 결정한다.
2. 결제된 수량만큼 상품의 재고를 차감하여 관리한다.
3. 최신 재고 상태를 유지하여, 다음 고객 구매 시 정확한 재고 정보를 제공한다.

## 👉 프로모션 할인
1. 프로모션은 기간 별로 적용된다.
2. 프로모션은 `N`개 구매 시 `1`개 무료 증정의 형태이다. (`1 + 1` 또는 `2 + 1`)
3. 각 상품마다 적용되는 프로모션은 다르며, 동일한 상품에 **여러 프로모션이 적용될 수 없다.**
4. 프로모션 혜택을 적용할 수 있는 재고는 별도 관리한다.
5. 프로모션 기간이 아니라면, 일반 재고를 우선적으로 차감한다. (추가)
6. 프로모션 기간 중이라면, 프로모션 재고를 우선적으로 차감한다.
7. 프로모션 재고가 프로모션 적용이 불가능한 만큼 남았다면 혜택 적용없이 우선 차감한다. (추가)
8. 프로모션 재고가 부족하다면, 일반 재고를 사용한다.
9. 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 수량 추가 시 혜택을 받을 수 있음을 안내한다.
10. 프로모션 재고가 부족하여 일부 수량을 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가 결제됨을 안내한다.

## 👉 멤버십 할인
1. 멤버십 회원은 할인을 받을 수 있다.
2. 할인 금액은 프로모션 미적용 금액의 `30%`이다. (일반 재고에 대해서만 적용)
3. 할인 적용은 프로모션 적용 후 남은 금액에 적용한다.
4. 멤버십 할인의 최대 한도는 `8,000원`이다.

## 👉 영수증
1. 고객의 구매 내역과 할인을 요약한다.
2. 영수증 항목
   - 구매 상품 내역 : 구매한 상품명, 수량, 가격
   - 증정 상품 내역 : 프로모션에 따라 무료로 제공된 증정 상품의 목록
   - 금액 정보
     * 총구매액 : 구매한 상품의 총 수량과 총 금액
     * 행사할인 : 프로모션에 의해 할인된 금액
     * 멤버십할인 : 멤버십에 의해 추가로 할인된 금액
     * 내실돈 : 최종 결제 금액

---
## 💻 입출력 흐름

### 1. 상품 목록 및 행사 목록 📝
- 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
- 상품 목록 : `src/main/resources/products.md`
- 행사 목록 : `src/main/resources/promotions.md`

### 2. 상품 안내 💻
- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다.
- 재고가 0개라면 `재고 없음`을 출력한다.
- 프로모션 상품만 존재하는 경우, 일반 상품은 `재고 없음`으로 출력한다. (추가)

```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개
```

### 3. 상품 및 수량 입력 💻📝
- 구매할 상품 및 수량을 입력받는다.
- 상품명, 수량은 `-`으로, 개별 상품은 `[]`로 묶어 `,`로 구분한다.
- 형식이 올바르지 않은 경우 `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`를 출력한다.
- 존재하지 않는 상품을 입력한 경우 `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`를 출력한다.
- 구매 수량이 재고 수량을 초과한 경우 `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`를 출력한다.

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
```
```
[콜라-3],[에너지바-5]
```

### 4. 수량 추가 여부 💻📝
- 프로모션 적용 가능 상품에 대해 해당 수량보다 적게 가져온 경우, 혜택에 대한 안내 메시지를 출력한다.
- 수량 추가 여부를 입력받는다.
  * `Y` : 증정 받을 수 있는 상품을 추가한다.
  * `N` : 증정 받을 수 있는 상품을 추가하지 않는다.

```
현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
```

### 5. 정가 결제 여부 💻📝
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야하는 경우, 해당 수량을 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
- 정가 결제 여부를 입력받는다.
  * `Y` : 일부 수량에 대해 정가로 결제한다.
  * `N` : 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

```
현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
```

### 6. 프로모션 기간 외 💻📝 (추가)
- 프로모션 적용 날짜가 아니라면, 정가로 결제할 지 여부에 대한 안내 메시지를 출력한다.
- 정가 결제 여부를 입력받는다.
  * `Y` : 정가로 결제한다.
  * `N` : 구매 상품에서 제외한다.

```
현재 {상품명}은(는) 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
```

### 7. 멤버십 할인 💻📝
- 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
- 멤버십 할인 적용 여부를 입력 받는다.
  * `Y` : 멤버십 할인을 적용한다.
  * `N` : 멤버십 할인을 적용하지 않는다.

```
멤버십 할인을 받으시겠습니까? (Y/N)
```

### 8. 영수증 출력 💻
- 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.

```
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

### 9. 추가 구매 💻📝
- 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
- 추가 구매 여부를 입력 받는다.
  * Y : 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
  * N : 구매를 종료한다.

```
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
```

---
## ❌ 공통 예외 처리
- 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시킨다.
- `[ERROR]`로 시작하는 에러 메시지를 출력 후 그 부분부터 다시 입력받는다.


- 기타 (구매 상품, 수량 제외) 잘못된 입력의 경우, `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`를 출력한다.

---
## 📜 기능 구현 목록

### 상품
- [x] 상품은 상품명을 갖는다.
  - [x] 상품명은 이름을 갖는다.
  - [x] 상품명은 null 또는 empty 일 수 없다.
  - [x] 이름이 같으면 상품명은 같다.
  - [x] 이름이 다르면 상품명은 다르다.
- [x] 상품은 가격을 갖는다.
  - [x] 가격은 값을 갖는다.
  - [x] 가격은 `0원` 이상이다.
  - [ ] 가격은 소수를 갖지 않는다.
  - [x] 금액이 같으면 상품 가격은 같다.
  - [x] 금액이 다르면 상품 가격은 다르다.
- [x] 상품명과 가격은 null 일 수 없다.
- [x] 상품명과 가격이 동일하면 같은 상품이다.
- [x] 상품명과 가격이 다르면 다른 상품이다.

### 재고
- [x] 재고는 수량을 갖는다.
- [ ] 수량은 증가할 수 있다.
- [ ] 수량은 감소할 수 있다.
- [ ] 수량은 0 이상의 정수이다.
- [ ] 수량은 차감 가능 여부를 반환한다.

### 프로모션
- [ ] 프로모션은 이름, 혜택, 적용 기간을 갖는다.
  - [ ] 이름이 같은 프로모션은 같은 프로모션이다.
  - [ ] 이름이 다른 프로모션은 다른 프로모션이다.
  - [ ] 혜택은 적용 구매 수량을 갖는다.
  - [ ] 적용 기간은 시작 날짜와 종료 날짜를 갖는다.
  - [ ] 적용 기간은 특정 날짜를 받았을 때 적용 여부를 반환한다.

### 상품 정보
- 상품 정보는 프로모션 정보, 일반 재고, 프로모션 재고를 갖는다.

### 상품 목록
- [ ] 상품을 추가할 수 있다.
  - [ ] 이미 등록된 상품이라면, 재고를 추가한다.
  - [ ] 같은 상품에 2가지 이상의 프로모션이 있다면 예외가 발생한다.
  - [ ] 가격이 다르면서 같은 상품명의 상품이 추가될 경우 예외가 발생한다.

### 멤버십 할인
- [ ] `30%` 할인 금액이 소수점일 경우 소수 첫째자리에서 반올림한다.
