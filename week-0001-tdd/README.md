# [WEEK-0001] TDD WIL


## 🌱 0. 1주차 회고를 들어가며

눈 깜짝할 사이에 1주차가 지나갔다.
항해를 시작하고 나서 일주일의 패턴이 간결해졌다.

>출근 -> 업무 -> 퇴근 -> 항해 모각코 (과제 수행) -> 취침

이러다보니 일주일이 더 빠르게 지나간 것 같기도 하다.

1주차 과제는 TDD 로 간단한 포인트 서비스를 만들어 보는 것이었다.

### 1주차를 들어가며 내 상태 점검

**테스트코드를 작성해본 경험이 없음**
나는 부끄럽지만, 아직 실무에서 제대로 테스트코드를 작성해본 경험이 없다.
테스트코드에 대한 중요성에 대해서는 많이 들어봤다.
하지만 테스트코드를 왜 작성해야하고, 필요성을 직접적으로 체감한 적은 없었다.
왜그런가 생각해보면, 테스트코드를 작성하고 그것에 따른 이득을 경험해본적이 없었기 때문인 것 같다.

**테스트코드를 위해 공부할 것들이 많아 보였음**
테스트코드를 작성해본 경험이 없으니, 테스트코드를 작성하는 법부터 공부를 해야했다.
_'테스트코드가 `given, when, then` 을 기본 골자로 `mock` 이라는 걸 쓰고 어쩌고...'_ 까지는 들어봤으나 실제 해본적은 없었다.

**테스트코드가 필요해 보이는 순간이 있었지만 외면했음**
실무에서 리팩토링이나 요구사항 수정이 있을 때마다 테스트코드의 필요성을 느낀적은 있었다.
하지만, 그럴 때 마다 일일이 그냥 다시 확인을 하는 방법을 택하여 간극을 매꾸는 것을 선택했고, 당연하다고 생각했었다.
그리고 그럴 때 마다 그런 확인들이 시간이 많이 걸렸던 경험이 있다.

**해보지 않아서 겁이 났음**
대부분의 개발자들은 테스트코드의 중요성을 안다고 생각한다.
나도 당연히 그 중 하나였으나, 해보지 않은 것을 시도하는 것에 두려움이 앞섰다.

**항해에서 찐하게 경험해봐야겠다.**
이번 항해의 커리큘럼에서 내가 가장 기대하는 것이 바로 TDD 였다.

<br>

## 🍇 1. 첫날, 멘붕..

### 이게 맞나...
과제를 처음 받았을 때는 API 구현 자체는 어려워 보이지 않았다.
간단한 Point 시스템을 개발하는 것이었다.
그런데, **테스트코드들이 내 발목을 잡았다.**

퇴근 후, 책상에 앉아서 테스트코드를 짜보려고 하는데 도저히 어떻게 시작을 해야할지 몰랐다.🫠
백엔드 개발자 역량으로 가장 중요한 테스트코드인데..
테스트코드 자체를 사용하는 법에 대해 익숙하지 않아서 간단한 API 를 테스트하는 코드를 짜기까지 오래 걸렸다.
몇번이나 확인해보고, 수정하고, 썼다가 지우고.. 이것을 얼마나 반복했는지 모른다.

모키토, 목, 스텁, 목빈 등등 들어는 봤으나 낯선 녀석들을 사용해야 한다는 것이 가장 큰 부담이었다.
게다가 그냥 테스트 코드를 짜는 것도 아니라, TDD 방법론으로 고민하면서 코드를 짜는 것이 첫날에 많은 부담을 주었다.
테스트코드를 짜는 것을 먼저 고민하기보다 **구현 관점에서 먼저 고민이 되니까 손이 자꾸 구현에 먼저 갔다.**
이것을 습관을 잡는 것이 첫날 가장 어려웠다.

첫날에 후회를 했다.
**"아.. 이게 맞나..?😰"**
퇴근 후 익숙하지도 않은 주제로 과제를 하려니 답답하기만하고 괜시리 조급해진 마음에 첫날 부터 항해를 시작한 것에 후회를 했다.

<br> 

## 🍉 2. 1주차 항해 여정 회고

### 테스트코드에 익숙해지다.
다행히도 둘째날부터 조금씩 테스트코드가 익숙해지기 시작했다.
_~~역시 삽질은 배신하지 않는다.~~_
**시간을 들이고 익숙해질 때 까지 반복하는 행위를 반복하면 무엇이든 습득할 수 있다는 것**을 다시 한번 체감했다.
백엔드 개발자 공부중 가장 중요한 부분중 하나인 테스트코드를 해내고 있다는 것에 성취가 있었다.

그리고 그렇게 되기까지는, 항해에서 조직해준 조원들과 함께 매일 저녁에 **코어타임을 정해 공부할 수 있는 시간이 큰 역할**을 한 것 같다.
혼자였다면, 익숙하지 않은 테스트코드를 짜기 위해서 이렇게까지 삽질과 노력을 하지 않았을 것 같다.

### 코어타임 만세 ~~_(백엔드 5기 16조 만세)_~~
우리조는 평일 저녁 8시부터 11시 (항해플러스는 재직자를 대상으로 한다.) 를 코어타임으로 정하고, 그 시간에 게더에 모여서 각자 모각코를 하기로 정했다.

><img src="https://velog.velcdn.com/images/joshuara7235/post/82be437b-b7fb-48d1-ba80-3289766f91f4/image.png" width="40%" height="n%">
- 16조 화이팅..!

개인적으로 정말 좋았던 것은, _**인간의 나약한 의지를 팀과 함께 이겨낼 수 있도록 하는 것**_이 좋았다.
그리고 코어타임 중 9시부터 10분~20분 정도 함께 고민되는 점등을 나누는 시간을 가지기로 했는데, 이것이 너무 좋았다.
각자가 생각하고 있는 테스트코드, TDD 방법론, 과제의 요구사항에 대한 해석등을 자유롭게 나누고 들으면서 많은 생각을 하게 했다.

팀원 분 중 한 분이 켄트 백의 이야기를 해줬는데, 테스트코드를 짜면서 최대한 작은 단위로 많은 커밋을 만든다는 것을 듣고 그렇게 하는 것도 시도해봤다.
><img src="https://velog.velcdn.com/images/joshuara7235/post/6075df1f-2d6a-4670-815f-1e380d381512/image.png" width="60%" height="n%">
- 팀원의 이야기를 듣고 시도해본 방법. 최대한 테스트케이스를 잘게 쪼개고 그 테스트마다 커밋을 작성했다.


### 멘토 코치
이번 1주차 멘토코치는 하헌우코치님께 받았다.

하헌우 코치님의 멘토링 중 가장 좋았던 부분은, 코치님의 명확한 개발철학이 담긴 이야기를 들을 수 있었다는 점이었다.
기술적으로 테스트코드와 TDD 의 방법론에 대한 멘토링도 너무 좋았지만,
코치님의 경험과 고민이 녹아져있는 개발 철학을 들을 기회가 있었다는 것이 너무 좋았다.

말씀해 주셨던 부분들 중 기억에 가장 남는 것은 두가지다.

#### 1. 가장 좋은 테스트는 빠르게 실패하는 테스트다.
- 하헌우 코치가 생각하는 테스트코드의 존재 이유.
- **실패에 민감하여 빠르게 실패를 감지하고, 빠르게 성공할 수 있게끔 하는 코드**가 좋은 테스트코드다.
- 자신만의 정의가 있어야 한다. 예컨데 테스트코드라는 것에 대한 하헌우 코치님의 정의는 바로 빠르게 실패하는 테스트.

#### 2. 경계선에 대한 경계를 잊지마라
- 개발자는 똑똑한 사람이어야 한다.
- 그 어떤 것이든 흑과 백으로 나눠져 있지 않다. **모든 것이 그레이 영역**이다. 
- 점과 선과 면의 경계가 있을까? 점을 무한대로 확대하면 선이 되고, 면이 되기도한다. 선 역시 확대하면 면이 된다.
- 그렇기 때문에, 그 **그레이 영역을 어떤 기준과 정의로 구별하여 코드로 만들 것인지 고민이 중요**하다.

<br>


## 🍐 3. 1주차 과제의 삽질 기록 및 회고

### 고민 거리
1주차 과제를 수행하면서 참 많은 고민과 삽질을 했다.
해당 내용을 과제의 [PR](https://github.com/mingj7235/hanghae/pull/1)에 질문과 고민거리를 적었다.

<div>
<img src="https://velog.velcdn.com/images/joshuara7235/post/0ecf4ccf-f34e-4c96-9a1f-690b29344307/image.png" width="40%" height="n%">
</div>

<br>

### 내가 했던 시도들
- 테스트코드 자체에 대한 기본지식이 없었으므로 테스트 코드를 작성하는 법에 대해 공부
- 동시성 이슈를 어떻게 테스트 할 것인가에 대한 삽질
- 통합테스트와 유닛 테스트, E2E 테스트를 각각 작성해보고 차이점을 고민해보기


### 힘들었던 점
- 익숙하지 않은 방법인 TDD 로 개발을 하는 것이 힘들었음
- 공부를 병행하면서 과제를 수행해야하는 것이 어려웠음
- 야근 후 집에와서 새벽까지 매일 코딩하는 것이 체력적으로 지쳤음

### 느끼고 배운 점
- 테스트코드 작성법, 어떤 것이 좋은 테스트 코드인가? 에 대한 고민을 시작함.
- 왜 테스트코드를 짜는 것이 좋은지 조금씩 체감중.
- 코드를 리팩토링을 함께 진행하면서 과제를 진행했는데, 테스트코드가 있으니 정말 안정적이라는 것을 느낌

<br>

## 🙏🏻 4. 글을 마치며..
항해 플러스 백엔드 5기 1주차를 마쳤다. 
1주차 과제는 다행히 PASS 를 받았다. 😊

2주차는 TDD 과 클린아키텍처를 기반으로 '특강 수강 신청' 시스템을 개발하는 과제다.
동시성 이슈도 고려한 설계를 하여 어플리케이션을 만들어야한다.

2주차도 치열하고 즐겁게 최선을 다해 해보겠다. 

화이팅!







